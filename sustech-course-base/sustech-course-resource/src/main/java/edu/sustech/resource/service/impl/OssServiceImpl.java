package edu.sustech.resource.service.impl;

import edu.sustech.api.client.CourseClient;
import edu.sustech.api.entity.dto.AttachmentDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.enums.ResultCode;
import edu.sustech.common.exception.ResourceOperationException;
import edu.sustech.common.result.Result;
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
import java.util.Objects;
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

    private final CourseClient courseClient;

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
            throw new ResourceOperationException("文件上传失败");
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

    /**
     * 上传附件
     *
     * @param file      文件
     * @param courseId  课程ID
     * @param chapterId 章节ID
     * @param videoId   视频(小节)ID
     * @return 文件路径
     */
    @Override
    public Long uploadAttachment(MultipartFile file, Long courseId, Long chapterId, Long videoId, String uuid, Byte isLecture) {
        checkUser();
        String fileName = null;
        String file_url = null;
        Long file_size = null;
        String file_type = null;
        try {
            // 原始文件名
            fileName = file.getOriginalFilename();
            // 文件大小
            file_size = file.getSize();
            // 文件类型
            // 截取原始文件名的后缀
            assert fileName != null;
            int index = fileName.lastIndexOf(".");
            String extension = "";
            if (index != -1) {
                file_type = fileName.substring(index + 1);
                extension = fileName.substring(index);
            } else {
                file_type = "";
            }
            // 构造新文件名称
            String objectName = courseId + "/" +
                    chapterId + "/" +
                    videoId + "/" +
                    UUID.randomUUID() + extension;
            file_url = aliOssUtil.upload(file.getBytes(), objectName, fileName);
        } catch (IOException e) {
            log.error("文件上传失败: ", e);
            throw new ResourceOperationException("文件上传失败");
        }
        AttachmentDTO attachmentDTO = AttachmentDTO.builder()
                .courseId(courseId)
                .chapterId(chapterId)
                .videoId(videoId)
                .fileName(fileName)
                .fileUrl(file_url)
                .fileSize(file_size)
                .fileType(file_type)
                .build();

        if (uuid != null) {
            attachmentDTO.setUuid(uuid);
        }
        if (isLecture != null) {
            attachmentDTO.setIsLecture(isLecture);
        }
        Result<Long> longResult = courseClient.addAttachment(attachmentDTO);
        if (!Objects.equals(longResult.getCode(), ResultCode.SUCCESS.code())) {
            throw new ResourceOperationException(longResult.getMessage());
        } else {
            return longResult.getData();
        }
    }

    private void checkUser() {
        if (UserContext.getUser() == null) {
            throw new ResourceOperationException(MessageConstant.NOT_LOGIN);
        }
    }
}
