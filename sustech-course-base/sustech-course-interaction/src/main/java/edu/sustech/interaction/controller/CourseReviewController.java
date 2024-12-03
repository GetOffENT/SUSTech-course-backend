package edu.sustech.interaction.controller;

import edu.sustech.common.result.Result;
import edu.sustech.common.util.UserContext;
import edu.sustech.interaction.entity.vo.CourseReviewLikeVO;
import edu.sustech.interaction.entity.vo.CourseReviewVO;
import edu.sustech.interaction.service.CourseReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程评价 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-17
 */
@RestController
@RequestMapping("/interaction/review")
@Slf4j
@Api(tags = "课程评价相关接口")
@RequiredArgsConstructor
public class CourseReviewController {

    private final CourseReviewService courseReviewService;


    /**
     * 分页获取课程评价列表
     *
     * @param courseId 课程id
     * @param page     页码
     * @param pageSize 每页大小
     * @return 课程评价列表(" reviews ", 课程评价列表, " total ", 总数, " score ", 全部评价的平均分)
     */
    @GetMapping("/list/{courseId}")
    @ApiOperation("获取课程评价列表")
    public Result<Map<String, Object>> getCourseReviewList(
            @PathVariable Integer courseId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return Result.success(courseReviewService.getCourseReviewList(courseId, page, pageSize));
    }


    /**
     * 添加课程评价
     *
     * @param courseReviewVO 课程评价
     * @param courseId       课程id
     * @return 当前评价
     */
    @PostMapping("/{courseId}")
    @ApiOperation("添加课程评价")
    public Result<CourseReviewVO> addCourseReview(@RequestBody CourseReviewVO courseReviewVO, @PathVariable Long courseId) {
        log.info("添加对课程{}的评价：{}", courseId, courseReviewVO);
        return Result.success(courseReviewService.addCourseReview(courseReviewVO, courseId));
    }

    /**
     * 获取当前用户对课程的评价
     *
     * @param courseId 课程id
     * @return 课程评价
     */
    @GetMapping("/{courseId}")
    @ApiOperation("获取当前用户对课程的评价")
    public Result<CourseReviewVO> getCourseReview(@PathVariable Long courseId) {
        log.info("获取当前用户对课程{}的评价", courseId);
        return Result.success(courseReviewService.getCourseReview(courseId));
    }

    /**
     * 删除用户对课程的评价
     *
     * @param reviewId 评价id
     * @return 成功或失败
     */
    @DeleteMapping("/{reviewId}")
    @ApiOperation("删除用户对课程的评价")
    public Result<Object> deleteCourseReview(@PathVariable Long reviewId) {
        log.info("删除用户对课程{}的评价", reviewId);
        courseReviewService.deleteCourseReview(reviewId);
        return Result.success();
    }

    /**
     * 点赞或点踩某条评价
     *
     * @param id     评价id
     * @param isLike 设置赞还是踩 true赞 false踩
     */
    @PostMapping("/like/{id}")
    @ApiOperation("点赞或点踩某条评价")
    public Result<Object> likeOrNot(@PathVariable Long id, @RequestParam Boolean isLike) {
        log.info("用户【{}】设置评论【{}】的【{}】状态", UserContext.getUser(), id, isLike ? "点赞" : "点踩");
        courseReviewService.likeOrNot(id, isLike);
        return Result.success();
    }

    /**
     * 获取用户的课程评价点赞点踩记录列表(课程评价ID和点赞点踩状态)
     *
     * @return 点赞点踩记录列表
     */
    @GetMapping("/list")
    @ApiOperation("获取用户的课程评价点赞点踩记录列表")
    public Result<List<CourseReviewLikeVO>> listLikeOrDislikeRecord() {
        log.info("获取用户【{}】的课程评价点赞点踩记录列表", UserContext.getUser());
        return Result.success(courseReviewService.listLikeOrDislikeRecord());
    }

}
