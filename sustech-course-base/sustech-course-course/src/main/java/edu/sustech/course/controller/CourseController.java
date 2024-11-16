package edu.sustech.course.controller;

import edu.sustech.common.result.MapResult;
import edu.sustech.common.result.Result;
import edu.sustech.api.entity.dto.UserCourseInfoDTO;
import edu.sustech.course.entity.vo.ChapterVO;
import edu.sustech.course.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     *
     * @return 随机推荐课程
     */
    @GetMapping("/random")
    @ApiOperation("获取随机推荐课程")
    public Result<List<Map<String, Object>>> getRandomRecommendCourses() {
        log.info("获取随机推荐课程...");
        return Result.success(courseService.getRandomRecommendCourses());
    }

    /**
     * 累加获取更多课程
     *
     * @param courseIds 已经获取的课程id列表
     * @return 返回十门新课程，以及其id列表，并标注是否有更多课程可以获取
     */
    @GetMapping("/cumulative")
    @ApiOperation("累加获取更多课程")
    public MapResult getCumulativeCourses(@RequestParam List<Long> courseIds) {
        log.info("累加获取更多课程... \n 已经获取的课程id列表:{}", courseIds);
        return MapResult.success().data(courseService.getCumulativeCourses(courseIds));
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

    /**
     * 获取课程目录, 如果已经登录，则还会获取用户每一个小节是否学习
     *
     * @param courseId 课程id
     * @return 课程目录(包括小节 : title id isLearned isPublic)
     */
    @GetMapping("/catalog/{courseId}")
    @ApiOperation("获取课程目录")
    public Result<List<ChapterVO>> getCatalog(@PathVariable Long courseId) {
        log.info("获取课程目录 courseId:{}", courseId);
        return Result.success(courseService.getCatalog(courseId));
    }


}
