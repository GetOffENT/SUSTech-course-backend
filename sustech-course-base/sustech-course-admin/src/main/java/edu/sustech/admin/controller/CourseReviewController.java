package edu.sustech.admin.controller;

import edu.sustech.admin.service.CourseReviewService;
import edu.sustech.api.entity.dto.CoursePageQueryDTO;
import edu.sustech.common.result.PageResult;
import edu.sustech.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
