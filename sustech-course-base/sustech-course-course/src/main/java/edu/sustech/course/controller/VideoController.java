package edu.sustech.course.controller;

import edu.sustech.common.result.Result;
import edu.sustech.course.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
@RestController
@RequestMapping("/course/video")
@Slf4j
@Api(tags = "视频相关接口")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    /**
     * 获取单个视频信息
     *
     * @return 单个视频信息(包含发布用户信息)
     */
    @ApiOperation("获取单个视频信息")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getVideoWithDataById(@PathVariable Long id) {
        // TODO: 完善Apifox接口文档
        log.info("获取单个视频信息...视频ID: {}", id);
        return Result.success(videoService.getVideoWithDataById(id));
    }
}
