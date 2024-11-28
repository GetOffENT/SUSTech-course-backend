package edu.sustech.resource.controller;

import edu.sustech.common.result.Result;
import edu.sustech.resource.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    @ApiOperation("上传视频到阿里云且存到数据库")
    public Result<String> uploadVideo(
            @RequestPart("file") MultipartFile file,
            @RequestParam Long id
    ) {
        log.info("上传视频: {}", file);
        return Result.success(vodService.uploadVideo(file, id));
    }

    /**
     * 删除阿里云视频(只提供远程调用)
     *
     * @param id 视频阿里云id
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除阿里云视频")
    public Result<Object> removeAlyVideo(@PathVariable String id) {
        log.info("删除视频... id: {}", id);
        vodService.removeVideo(id);
        return Result.success();
    }

    /**
     * 批量删除阿里云视频(只提供远程调用)
     *
     * @param videoIdList 视频阿里云id列表
     * @return 无
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除阿里云视频")
    public Result<Object> removeAlyVideoBatch(@RequestParam List<String> videoIdList) {
        log.info("批量删除视频: {}", videoIdList);
        vodService.removeVideoBatch(videoIdList);
        return Result.success();
    }

    /**
     * 获取视频播放地址
     *
     * @param videoSourceId 视频源id
     * @return 视频播放地址
     */
    @GetMapping("/play/{videoSourceId}")
    @ApiOperation("获取视频播放地址")
    public Result<String> getPlayInfo(@PathVariable String videoSourceId) {
        log.info("获取视频播放地址: {}", videoSourceId);
        return Result.success(vodService.getPlayInfo(videoSourceId));
    }
}
