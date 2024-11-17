package edu.sustech.interaction.controller;

import edu.sustech.common.result.Result;
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
     * @return 课程评价列表("reviews", 课程评价列表, "total", 总数, "score", 全部评价的平均分)
     */
    @GetMapping("/{courseId}")
    @ApiOperation("获取课程评价列表")
    public Result<Map<String, Object>> getCourseReviewList(
            @PathVariable Integer courseId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return Result.success(courseReviewService.getCourseReviewList(courseId, page, pageSize));
    }

}
