package edu.sustech.course.controller;

import cn.hutool.core.bean.BeanUtil;
import edu.sustech.api.entity.dto.VideoDTO;
import edu.sustech.common.result.Result;
import edu.sustech.course.entity.vo.AttachmentVO;
import edu.sustech.course.service.AttachmentService;
import edu.sustech.course.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    private final AttachmentService attachmentService;

    /**
     * 获取单个视频信息
     *
     * @return 单个视频信息(包含发布用户信息)
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
     * 获取视频最新版附件列表
     *
     * @param videoId 视频ID
     * @return 附件列表
     */
    @GetMapping("/attachment/list")
    @ApiOperation("获取视频附件")
    public Result<List<AttachmentVO>> getAttachments(@RequestParam Long videoId) {
        log.info("获取视频附件 videoId:{}", videoId);
        return Result.success(attachmentService.getAttachments(videoId));
    }

    /**
     * 获取指定附件历史版本列表
     *
     * @param uuid 附件uuid
     * @return 指定附件历史版本列表
     */
    @GetMapping("/attachment/history")
    @ApiOperation("获取指定附件历史版本")
    public Result<List<AttachmentVO>> getAttachmentHistory(@RequestParam String uuid) {
        log.info("获取指定附件历史版本 uuid:{}", uuid);
        return Result.success(attachmentService.getAttachmentHistory(uuid));
    }
}
