package edu.sustech.admin.controller;

import edu.sustech.admin.entity.dto.CourseStatusDTO;
import edu.sustech.admin.service.CourseReviewService;
import edu.sustech.api.entity.dto.ChapterDTO;
import edu.sustech.api.entity.dto.CoursePageQueryDTO;
import edu.sustech.api.entity.enums.CourseStatus;
import edu.sustech.common.result.PageResult;
import edu.sustech.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-27 11:22
 */
@RestController
@RequestMapping("/admin/course")
@Slf4j
@Api(tags = "管理端课程审核相关接口")
@RequiredArgsConstructor
public class CourseReviewController {

    private final CourseReviewService courseReviewService;

    /**
     * 分页动态条件获取课程列表
     *
     * @param coursePageQueryDTO 课程分页查询DTO
     * @return 课程列表
     */
    @PostMapping("/list")
    @ApiOperation("分页动态条件获取课程列表")
    public Result<PageResult<Map<String, Object>>> getCourseList(@RequestBody CoursePageQueryDTO coursePageQueryDTO) {
        log.info("分页动态条件获取课程列表 coursePageQueryDTO: {}", coursePageQueryDTO);
        return Result.success(courseReviewService.getCourseList(coursePageQueryDTO));
    }


    /**
     * 获取课程目录
     *
     * @param courseId 课程id
     * @return 课程目录
     */
    @GetMapping("/catalog/{courseId}")
    @ApiOperation("获取课程目录")
    public Result<List<ChapterDTO>> getCatalog(@PathVariable Long courseId) {
        log.info("获取课程目录 courseId: {}", courseId);
        return Result.success(courseReviewService.getCatalog(courseId));
    }

    /**
     * 获取课程视频地址
     *
     * @param videoSourceId 课程id
     * @return 课程视频地址
     */
    @GetMapping("/video/{videoSourceId}")
    @ApiOperation("获取课程视频地址")
    public Result<String> getVideoUrl(@PathVariable String videoSourceId) {
        log.info("获取课程视频地址 courseId: {}", videoSourceId);
        return Result.success(courseReviewService.getPlayInfo(videoSourceId));
    }

    /**
     * 审核课程
     *
     * @param courseStatusDTO 课程状态信息
     * @return 审核结果
     */
    @PostMapping("/review")
    @ApiOperation("审核课程")
    public Result<Object> reviewCourse(@RequestBody CourseStatusDTO courseStatusDTO) {
        log.info("审核课程 courseStatusDTO: {}", courseStatusDTO);
        courseReviewService.reviewCourse(courseStatusDTO);
        return Result.success();
    }
}
