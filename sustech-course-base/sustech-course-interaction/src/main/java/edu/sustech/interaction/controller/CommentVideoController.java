package edu.sustech.interaction.controller;

import edu.sustech.common.result.Result;
import edu.sustech.interaction.service.CommentVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
@RestController
@RequestMapping("/interaction/comment/video")
@Slf4j
@Api(tags = "评论相关接口")
@RequiredArgsConstructor
public class CommentVideoController {

    private final CommentVideoService commentVideoService;

    /**
     * 获取对应视频的评论树
     *
     * @param vid    视频id
     * @param offset 已经获取的评论树数量
     * @param type   排序类型 1热度 2时间
     * @return 评论树列表
     */
    @GetMapping("/tree")
    @ApiOperation("获取对应视频的评论树")
    public Result<Map<String, Object>> getCommentTree(@RequestParam("vid") Long vid,
                                 @RequestParam("offset") Long offset,
                                 @RequestParam("type") Integer type) {
        log.info("获取视频{}的评论树，按照{}排序", vid, type == 1 ? "热度" : "时间");
        Map<String, Object> map = commentVideoService.getCommentTree(
                vid, offset, type
        );
        return Result.success(map);
    }
}
