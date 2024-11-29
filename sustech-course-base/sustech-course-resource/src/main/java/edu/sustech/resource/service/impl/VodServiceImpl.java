package edu.sustech.resource.service.impl;

import cn.hutool.core.util.StrUtil;
import edu.sustech.api.client.CourseClient;
import edu.sustech.api.entity.dto.VideoResourceDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.ResourceOperationException;
import edu.sustech.common.result.Result;
import edu.sustech.common.util.UserContext;
import edu.sustech.resource.service.VodService;
import edu.sustech.resource.utils.AliVodUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-19 23:38
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VodServiceImpl implements VodService {

    private final AliVodUtil aliVodUtil;

    private final CourseClient courseClient;

    /**
     * 上传视频到阿里云
     *
     * @param file 视频文件
     * @return 视频播放地址
     */
    @Override
    public String uploadVideo(MultipartFile file, Long id) {
        checkUser();

        String videoSourceId;
        String filename;
        Long size = file.getSize();
        try {
            filename = file.getOriginalFilename();
            assert filename != null;
            String title = filename.substring(0, filename.lastIndexOf("."));
            videoSourceId = aliVodUtil.uploadStream(title, filename, file.getInputStream());
        } catch (Exception e) {
            log.error("上传视频失败", e);
            throw new ResourceOperationException(MessageConstant.UPLOAD_VIDEO_FAILED);
        }
        log.info("上传视频成功，videoSourceId: {}", videoSourceId);

        // 同时更新数据库视频资源信息
        VideoResourceDTO videoResourceDTO = VideoResourceDTO.builder()
                .id(id)
                .videoSourceId(videoSourceId)
                .videoOriginalName(filename)
                .size(size)
                .build();
        Result<Object> objectResult = courseClient.addVideoResource(videoResourceDTO);
        if (objectResult.getCode() != 20000) {
            throw new ResourceOperationException(objectResult.getMessage());
        }

        // 上传时获取一次视频播放地址
        String url;
        try {
            url = aliVodUtil.getPlayInfo(videoSourceId);
        } catch (Exception e) {
            throw new ResourceOperationException(MessageConstant.GET_VIDEO_URL_FAILED);
        }
        return url;
    }

    /**
     * 批量删除视频
     *
     * @param ids 视频阿里云id列表，逗号分隔
     */
    @Override
    public void removeVideo(String ids) {
        try {
            aliVodUtil.deleteVideo(ids);
            log.info("删除视频成功");
        }catch(Exception e) {
            log.error("删除视频失败", e);
            throw new ResourceOperationException(MessageConstant.DELETE_VIDEO_FAILED);
        }
    }

    /**
     * 批量删除视频
     *
     * @param videoIdList 视频阿里云id列表
     */
    @Override
    public void removeVideoBatch(List<String> videoIdList) {
        try {
            aliVodUtil.deleteVideo(StrUtil.join(",", videoIdList));
            log.info("批量删除视频成功");
        } catch (Exception e) {
            log.error("批量删除视频失败", e);
            throw new ResourceOperationException(MessageConstant.DELETE_VIDEO_FAILED);
        }
    }

    /**
     * 获取视频播放信息
     *
     * @param videoSourceId 视频源id
     * @return 视频播放地址
     */
    @Override
    public String getPlayInfo(String videoSourceId) {
        try {
            return aliVodUtil.getPlayInfo(videoSourceId);
        } catch (Exception e) {
            log.error("获取视频播放地址失败", e);
            throw new ResourceOperationException(MessageConstant.GET_VIDEO_URL_FAILED);
        }
    }

    private void checkUser() {
        if (UserContext.getUser() == null) {
            throw new ResourceOperationException(MessageConstant.NOT_LOGIN);
        }
    }
}
