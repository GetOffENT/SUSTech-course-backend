package edu.sustech.interaction.controller;

import edu.sustech.common.result.Result;
import edu.sustech.common.util.UserContext;
import edu.sustech.interaction.entity.dto.CommentDTO;
import edu.sustech.interaction.entity.vo.CommentTreeVO;
import edu.sustech.interaction.entity.vo.VideoCommentLikeVO;
import edu.sustech.interaction.service.VideoCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
@Api(tags = "视频评论相关接口")
@RequiredArgsConstructor
public class VideoCommentController {

    private final VideoCommentService videoCommentService;

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
        Map<String, Object> map = videoCommentService.getCommentTree(
                vid, offset, type
        );
        return Result.success(map);
    }

    /**
     * 获取指定id根评论的评论树
     *
     * @param id 根评论id
     * @return 单棵评论树
     */
    @GetMapping("tree/{id}")
    @ApiOperation("获取指定id根评论的评论树")
    public Result<CommentTreeVO> getCommentTreeById(@PathVariable Long id) {
        log.info("获取指定id: {}根评论的评论树", id);
        return Result.success(videoCommentService.getCommentTreeById(id));
    }

    /**
     * 新增评论
     *
     * @return 评论
     */
    @PostMapping
    @ApiOperation("新增评论")
    public Result<CommentTreeVO> saveComment(@RequestBody CommentDTO commentDTO) {
        log.info("新增评论: {}", commentDTO);
        return Result.success(videoCommentService.saveComment(commentDTO));
    }

    /**
     * 删除评论
     *
     * @param id 评论id
     * @return 成功或异常
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除评论")
    public Result<Object> deleteComment(@PathVariable Long id) {
        log.info("删除评论: {}", id);
        videoCommentService.deleteComment(id);
        return Result.success();
    }

    /**
     * 点赞或点踩某条评论
     *
     * @param id     评论id
     * @param isLike 设置赞还是踩 true赞 false踩
     */
    @PostMapping("/like/{id}")
    @ApiOperation("点赞或点踩某条评论")
    public Result<Object> likeOrNot(@PathVariable Long id, @RequestParam Boolean isLike) {
        log.info("用户【{}】设置评论【{}】的【{}】状态", UserContext.getUser(), id, isLike ? "点赞" : "点踩");
        videoCommentService.likeOrNot(id, isLike);
        return Result.success();
    }

    /**
     * 获取用户的点赞点踩记录列表(ID和点赞点踩状态)
     *
     * @return 点赞点踩记录列表
     */
    @GetMapping("/list")
    @ApiOperation("获取用户的点赞点踩记录列表")
    public Result<List<VideoCommentLikeVO>> listLikeOrDislikeRecord() {
        log.info("获取用户【{}】的点赞点踩记录列表", UserContext.getUser());
        return Result.success(videoCommentService.listLikeOrDislikeRecord());
    }
}
