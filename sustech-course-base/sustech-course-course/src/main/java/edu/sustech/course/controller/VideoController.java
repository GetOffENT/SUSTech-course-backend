package edu.sustech.course.controller;

import cn.hutool.core.bean.BeanUtil;
import edu.sustech.api.entity.dto.VideoDTO;
import edu.sustech.api.entity.dto.VideoResourceDTO;
import edu.sustech.common.result.Result;
import edu.sustech.course.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     * @return 单个视频信息(包含发布用户信息 、 登录用户和视频所属课程关系信息 、 视频所属课程信息 、 登录用户和视频记录信息)
     */
    @ApiOperation("获取单个视频信息")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getVideoWithDataById(@PathVariable Long id) {
        // TODO: 完善Apifox接口文档
        log.info("获取单个视频信息(包含发布用户信息)...视频ID: {}", id);
        return Result.success(videoService.getVideoWithDataById(id));
    }

    /**
     * 获取单个视频信息(仅信息)
     *
     * @return 单个视频信息
     */
    @ApiOperation("获取单个视频信息")
    @GetMapping("/info/{id}")
    public Result<VideoDTO> getVideoById(@PathVariable Long id) {
        log.info("获取单个视频信息...视频ID: {}", id);
        return Result.success(BeanUtil.copyProperties(videoService.getById(id), VideoDTO.class));
    }


    /**
     * 更新评论数量
     *
     * @param id    视频ID
     * @param count 评论数量
     * @return 响应对象
     */
    @ApiOperation("更新评论数量")
    @PostMapping("/comment/{id}")
    public Result<Object> updateCommentCount(@PathVariable Long id, @RequestParam Integer count) {
        log.info("更新评论数量...视频ID: {}", id);
        videoService.updateCommentCount(id, count);
        return Result.success();
    }

    /**
     * 更新弹幕数量
     *
     * @param id    视频ID
     * @param count 弹幕数量
     * @return 响应对象
     */
    @ApiOperation("更新弹幕数量")
    @PostMapping("/danmu/{id}")
    public Result<Object> updateDanmuCount(@PathVariable Long id, @RequestParam Integer count) {
        log.info("更新弹幕数量...视频ID: {}", id);
        videoService.updateDanmuCount(id, count);
        return Result.success();
    }

    /**
     * 添加已有小节(video表)视频资源
     *
     * @param videoResourceDTO 视频资源信息
     * @return 响应对象
     */
    @ApiOperation("添加视频资源")
    @PostMapping("/resource")
    public Result<Object> addVideoResource(@RequestBody VideoResourceDTO videoResourceDTO) {
        log.info("添加视频资源...视频资源信息: {}", videoResourceDTO);
        videoService.addVideoResource(videoResourceDTO);
        return Result.success();
    }

    /**
     * 获取视频播放url
     *
     * @return 视频播放url
     */
    @ApiOperation("获取视频播放url")
    @GetMapping("/play/{videoSourceId}")
    public Result<String> getPlayInfo(@PathVariable String videoSourceId) {
        log.info("获取视频播放url...视频源ID: {}", videoSourceId);
        return Result.success(videoService.getPlayInfo(videoSourceId));
    }

    /**
     * 新增视频(小节)
     *
     * @param videoDTO 视频(小节)信息
     * @return 视频(小节)id
     */
    @PostMapping
    @ApiOperation("新增视频")
    public Result<Map<String, Long>> addVideo(@RequestBody VideoDTO videoDTO) {
        log.info("新增视频 videoDTO:{}", videoDTO);
        return Result.success(videoService.addVideo(videoDTO));
    }

    /**
     * 删除小节及其视频资源
     *
     * @param videoId 视频(小节)id
     * @return 无
     */
    @DeleteMapping("/{videoId}")
    @ApiOperation("删除小节及其视频资源")
    public Result<Object> deleteVideo(@PathVariable Long videoId) {
        log.info("删除小节及其视频资源 videoId:{}", videoId);
        videoService.deleteVideo(videoId);
        return Result.success();
    }

    /**
     * 删除小节中的视频资源
     *
     * @param videoId 视频(小节)id
     * @return 无
     */
    @DeleteMapping("/resource/{videoId}")
    @ApiOperation("删除小节中的视频资源")
    public Result<Object> removeVideoResource(@PathVariable Long videoId) {
        log.info("删除小节中的视频资源 videoId:{}", videoId);
        videoService.removeVideoResource(videoId);
        return Result.success();
    }
}
