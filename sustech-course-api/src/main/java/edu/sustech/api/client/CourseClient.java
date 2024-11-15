package edu.sustech.api.client;

import edu.sustech.api.entity.dto.UserCourseInfoDTO;
import edu.sustech.api.entity.dto.VideoDTO;
import edu.sustech.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
     * 根据用户id查询该用户的所有课程信息
     * @param id 用户id
     * @return 该用户的所有课程信息
     */
    @GetMapping("/course/course/info/{id}")
    Result<UserCourseInfoDTO> getCoursesInfoByUserId(@PathVariable Long id);

    /**
     * 获取单个视频信息
     * @param id 视频id
     * @return 单个视频信息
     */
    @GetMapping("/course/video/info/{id}")
    Result<VideoDTO> getVideoById(@PathVariable Long id);
}
