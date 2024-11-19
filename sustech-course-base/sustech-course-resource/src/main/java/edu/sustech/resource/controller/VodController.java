package edu.sustech.resource.controller;

import edu.sustech.common.result.Result;
import edu.sustech.resource.service.VodService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 阿里云VOD相关接口
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-19 23:36
 */
@RestController
@RequestMapping("/resource/vod")
@Slf4j
@Api(tags = "阿里云VOD相关接口")
@RequiredArgsConstructor
public class VodController {


    private final VodService vodService;

    /**
     * 上传视频到阿里云
     *
     * @param file 视频文件
     * @return 视频播放地址
     */
    @PostMapping("/video")
    public Result<String> uploadVideo(
            @RequestPart("file") MultipartFile file,
            @RequestParam Long courseId,
            @RequestParam Long chapterId,
            @RequestParam Long videoId
    ) {
        log.info("上传视频: {}", file);
        return Result.success(vodService.uploadVideo(file, courseId, chapterId, videoId));
    }
}
