package edu.sustech.admin.service;

import edu.sustech.api.entity.dto.CoursePageQueryDTO;
import edu.sustech.common.result.PageResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-27 11:28
 */
public interface CourseReviewService {
    /**
     * 分页动态条件获取课程列表
     *
     * @param coursePageQueryDTO 课程分页查询DTO
     * @return 课程列表
     */
    PageResult<Map<String, Object>> getCourseList(CoursePageQueryDTO coursePageQueryDTO);
}
