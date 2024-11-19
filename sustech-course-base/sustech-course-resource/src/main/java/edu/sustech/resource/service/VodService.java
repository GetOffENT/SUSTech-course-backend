package edu.sustech.resource.service;

import org.springframework.web.multipart.MultipartFile;

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
}
