package edu.sustech.course.controller;

import edu.sustech.common.result.Result;
import edu.sustech.api.entity.dto.UserCourseInfoDTO;
import edu.sustech.course.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-12
 */
@RestController
@RequestMapping("/course/course")
@Slf4j
@Api(tags = "课程相关接口")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    /**
     * 获取随机推荐课程
     * @return 随机推荐课程
     */
    @GetMapping("/random")
    @ApiOperation("获取随机推荐课程")
    public Result<List<Map<String, Object>>> getRandomRecommendCourses() {
        log.info("获取随机推荐课程...");
        return Result.success(courseService.getRandomRecommendCourses());
    }

    /**
     * 根据用户id查询该用户的所有课程信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据用户id查询该用户的所有课程信息")
    public Result<UserCourseInfoDTO> getUserCoursesInfoByUserId(@PathVariable Long id) {
        log.info("根据用户id查询该用户的所有课程信息 id:{}", id);
        return Result.success(courseService.getUserCoursesInfoByUserId(id));
    }

}
