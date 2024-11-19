package edu.sustech.resource.service.impl;

import edu.sustech.common.exception.ResourceOperationException;
import edu.sustech.resource.service.VodService;
import edu.sustech.resource.utils.AliVodUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 上传视频到阿里云
     *
     * @param file 视频文件
     * @return 视频播放地址
     */
    @Override
    public String uploadVideo(MultipartFile file, Long courseId, Long chapterId, Long videoId) {
        String videoSourceId;
        try {
            String filename = file.getOriginalFilename();
            assert filename != null;
            String title = filename.substring(0, filename.lastIndexOf("."));
            videoSourceId = aliVodUtil.uploadStream(title, filename, file.getInputStream());
        } catch (Exception e) {
            log.error("上传视频失败", e);
            throw new ResourceOperationException("上传视频失败");
        }

        log.info("上传视频成功，videoSourceId: {}", videoSourceId);


        // 上传时获取一次视频播放地址
        String url;
        try {
            url = aliVodUtil.getPlayInfo(videoSourceId);
        } catch (Exception e) {
            throw new ResourceOperationException("获取视频失败");
        }
        return url;
    }
}
