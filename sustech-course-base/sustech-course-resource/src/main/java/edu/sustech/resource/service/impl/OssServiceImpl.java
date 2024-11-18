package edu.sustech.resource.service.impl;

import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.ResourceUploadException;
import edu.sustech.common.util.UserContext;
import edu.sustech.resource.service.OssService;
import edu.sustech.resource.utils.AliOssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-18 4:13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OssServiceImpl implements OssService {

    private final AliOssUtil aliOssUtil;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件路径
     */
    @Override
    public String upload(MultipartFile file) {
        try {
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            // 截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 构造新文件名称
            String objectName = UUID.randomUUID() + extension;

//            String datePath = LocalDate.now().toString().replace("-", "/");
//
//            objectName = datePath + "/" + objectName;

            return aliOssUtil.upload(file.getBytes(), objectName, originalFilename);
        } catch (IOException e) {
            log.error("文件上传失败: {}", e);
            throw new RuntimeException("文件上传失败");
        }
    }

    /**
     * 多文件上传
     *
     * @param files 文件列表
     * @return 文件路径列表
     */
    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        checkUser();
        List<String> result = new ArrayList<>();
        for (MultipartFile file : files) {
            result.add(upload(file));
        }
        return result;
    }

    private void checkUser() {
        if (UserContext.getUser() == null) {
            throw new ResourceUploadException(MessageConstant.NOT_LOGIN);
        }
    }
}
