package edu.sustech.admin.service;

import edu.sustech.admin.entity.dto.CourseStatusDTO;
import edu.sustech.api.entity.dto.ChapterDTO;
import edu.sustech.api.entity.dto.CoursePageQueryDTO;
import edu.sustech.api.entity.enums.CourseStatus;
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

    /**
     * 获取课程目录
     *
     * @param courseId 课程id
     * @return 课程目录
     */
    List<ChapterDTO> getCatalog(Long courseId);

    /**
     * 获取视频播放地址
     *
     * @param videoSourceId 课程id
     * @return 视频播放地址
     */
    String getPlayInfo(String videoSourceId);

    /**
     * 审核课程
     *
     * @param courseStatusDTO 课程状态信息
     */
    void reviewCourse(CourseStatusDTO courseStatusDTO);
}
