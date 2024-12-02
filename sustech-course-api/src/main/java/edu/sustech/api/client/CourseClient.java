package edu.sustech.api.client;

import edu.sustech.api.entity.dto.*;
import edu.sustech.common.result.PageResult;
import edu.sustech.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-12 7:05
 */
@FeignClient("course-service")
public interface CourseClient {

    /**
     * 获取课程信息
     *
     * @param courseId 课程id
     * @return 课程信息
     */
    @GetMapping("/course/course/{courseId}")
    Result<CourseInfoDTO> getCourseById(@PathVariable Long courseId);

    /**
     * 根据用户id查询该用户的所有课程信息
     *
     * @param id 用户id
     * @return 该用户的所有课程信息
     */
    @GetMapping("/course/course/info/{id}")
    Result<UserCourseInfoDTO> getCoursesInfoByUserId(@PathVariable Long id);

    /**
     * 获取单个视频信息
     *
     * @param id 视频id
     * @return 单个视频信息
     */
    @GetMapping("/course/video/info/{id}")
    Result<VideoDTO> getVideoById(@PathVariable Long id);

    /**
     * 更新视频评论数量
     *
     * @param id    视频ID
     * @param count 评论数量
     * @return 响应对象
     */
    @PostMapping("course/video/comment/{id}")
    Result<Object> updateCommentCount(@PathVariable Long id, @RequestParam Integer count);

    /**
     * 更新视频弹幕数量
     *
     * @param id    视频ID
     * @param count 弹幕数量
     * @return 响应对象
     */
    @PostMapping("course/video/danmu/{id}")
    Result<Object> updateDanmuCount(@PathVariable Long id, @RequestParam Integer count);


    /**
     * 添加附件
     *
     * @param attachmentDTO 附件信息
     * @return 附件ID
     */
    @PostMapping("/course/attachment")
    Result<Long> addAttachment(@RequestBody AttachmentDTO attachmentDTO);

    /**
     * 添加视频资源
     *
     * @param videoResourceDTO 视频资源信息
     * @return 响应对象
     */
    @PostMapping("/course/video/resource")
    Result<Object> addVideoResource(@RequestBody VideoResourceDTO videoResourceDTO);


    /**
     * 根据条件查询课程信息
     *
     * @param coursePageQueryDTO 查询条件
     * @return 课程信息
     */
    @PostMapping("/course/course/condition")
    Result<PageResult<Map<String, Object>>> getCoursesByCondition(@RequestBody CoursePageQueryDTO coursePageQueryDTO);

    /**
     * 管理端获取课程目录
     *
     * @param courseId 课程id
     * @return 课程目录(包括小节 : title id isLearned isPublic)
     */
    @GetMapping("/course/course/catalog/{courseId}")
    Result<List<ChapterDTO>> getCatalog(@PathVariable Long courseId);
}
