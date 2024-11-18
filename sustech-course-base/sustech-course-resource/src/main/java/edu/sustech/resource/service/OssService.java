package edu.sustech.resource.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-18 4:13
 */
public interface OssService {


    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件路径
     */
    String upload(MultipartFile file);

    /**
     * 多文件上传
     *
     * @param files 文件列表
     * @return 文件路径列表
     */
    List<String> uploadFiles(List<MultipartFile> files);
}
