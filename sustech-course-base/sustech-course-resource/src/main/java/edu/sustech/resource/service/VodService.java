package edu.sustech.resource.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-19 23:37
 */
public interface VodService {
    /**
     * 上传视频到阿里云
     *
     * @param file 视频文件
     * @return 视频播放地址
     */
    String uploadVideo(MultipartFile file, Long id);

    /**
     * 删除视频
     *
     * @param id 视频阿里云id
     */
    void removeVideo(String id);

    /**
     * 批量删除视频
     *
     * @param videoIdList 视频阿里云id列表
     */
    void removeVideoBatch(List<String> videoIdList);

    /**
     * 获取视频播放信息
     *
     * @param videoSourceId 视频源id
     * @return 视频播放地址
     */
    String getPlayInfo(String videoSourceId);
}
