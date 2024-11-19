package edu.sustech.resource.controller;

import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.ResourceUploadException;
import edu.sustech.common.result.Result;
import edu.sustech.common.util.UserContext;
import edu.sustech.resource.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-18 4:11
 */
@RestController
@RequestMapping("/resource/oss")
@Slf4j
@Api(tags = "阿里云OSS相关接口")
@RequiredArgsConstructor
public class OssController {

    private final OssService ossService;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件路径
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(@RequestPart("file") MultipartFile file) {
        log.info("文件上传: {}", file);
        if (UserContext.getUser() == null) {
            throw new ResourceUploadException(MessageConstant.NOT_LOGIN);
        }
        return Result.success(ossService.upload(file));
    }

    /**
     * 多文件上传
     *
     * @param files 文件列表
     * @return 文件路径列表
     */
    @PostMapping("/files")
    @ApiOperation("多文件上传")
    public Result<List<String>> uploadFiles(@RequestPart("files") List<MultipartFile> files) {
        log.info("多文件上传: {}", files);
        return Result.success(ossService.uploadFiles(files));
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
    @PostMapping("/attachment")
    @ApiOperation("上传附件")
    public Result<String> uploadAttachment(
            @RequestPart("file") MultipartFile file,
            @RequestParam Long courseId,
            @RequestParam Long chapterId,
            @RequestParam Long videoId
    ) {
        log.info("视频附件上传: {}", file);
        return Result.success(ossService.uploadAttachment(file, courseId, chapterId, videoId));
    }
}
