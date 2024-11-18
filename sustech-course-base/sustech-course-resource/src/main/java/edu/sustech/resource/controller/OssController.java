package edu.sustech.resource.controller;

import edu.sustech.common.result.Result;
import edu.sustech.resource.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

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
        return Result.success(ossService.upload(file));
    }

}
