package edu.sustech.course.controller;

import edu.sustech.api.entity.dto.ChapterDTO;
import edu.sustech.api.entity.dto.CoursePageQueryDTO;
import edu.sustech.api.entity.dto.StudentDTO;
import edu.sustech.common.result.MapResult;
import edu.sustech.common.result.PageResult;
import edu.sustech.common.result.Result;
import edu.sustech.api.entity.dto.UserCourseInfoDTO;
import edu.sustech.course.entity.Course;
import edu.sustech.course.entity.dto.CourseDTO;
import edu.sustech.course.entity.dto.CourseDetailDTO;
import edu.sustech.course.entity.dto.CourseStatusDTO;
import edu.sustech.course.service.CourseDescriptionService;
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

    private final CourseDescriptionService courseDescriptionService;

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
     * 获取课程目录, 如果课程已经发布且用户已经登录，则还会获取用户每一个小节是否学习
     *
     * @param courseId 课程id
     * @return 课程目录(包括小节 : title id isLearned isPublic)
     */
    @GetMapping("/catalog/{courseId}")
    @ApiOperation("获取课程目录")
    public Result<List<ChapterDTO>> getCatalog(@PathVariable Long courseId) {
        log.info("获取课程目录 courseId:{}", courseId);
        return Result.success(courseService.getCatalog(courseId));
    }

    /**
     * 获取课程简介
     *
     * @param courseId 课程id
     * @return 课程简介
     */
    @GetMapping("/description/{courseId}")
    @ApiOperation("获取课程简介")
    public Result<String> getCourseDescription(@PathVariable Long courseId) {
        log.info("获取课程简介 courseId:{}", courseId);
        String description = courseDescriptionService.getById(courseId).getDescription();
        return Result.success(description == null ? "" : description);
    }

    /**
     * 获取课程信息
     *
     * @param courseId 课程id
     * @return 课程信息
     */
    @GetMapping("/{courseId}")
    @ApiOperation("获取课程信息")
    public Result<Course> getCourseById(@PathVariable Long courseId) {
        log.info("获取课程信息 courseId:{}", courseId);
        return Result.success(courseService.getCourseById(courseId));
    }

    /**
     * 新增课程(添加课程基本信息)
     *
     * @param courseDTO 课程信息
     * @return 课程id
     */
    @PostMapping
    @ApiOperation("新增课程")
    public Result<Map<String, Long>> addCourse(@RequestBody CourseDTO courseDTO) {
        log.info("新增课程 courseDTO:{}", courseDTO);
        return Result.success(courseService.addCourse(courseDTO));
    }

    /**
     * 新增课程详情
     *
     * @param courseDetailDTO 课程详情
     * @return 课程详情
     */
    @PostMapping("/detail")
    @ApiOperation("新增课程详情")
    public Result<Object> addCourseDetail(@RequestBody CourseDetailDTO courseDetailDTO) {
        log.info("新增课程详情 courseDetailDTO:{}", courseDetailDTO);
        courseService.addCourseDetail(courseDetailDTO);
        return Result.success();
    }

    /**
     * 根据动态条件获取课程
     *
     * @param coursePageQueryDTO 动态条件
     * @return 课程列表
     */
    @ApiOperation("根据动态条件获取课程")
    @PostMapping("/condition")
    public Result<PageResult<Map<String, Object>>> getCoursesByCondition(@RequestBody CoursePageQueryDTO coursePageQueryDTO) {
        log.info("获取课程列表 coursePageQueryDTO:{}", coursePageQueryDTO);
        return Result.success(courseService.getCoursesByCondition(coursePageQueryDTO));
    }

    /**
     * 获取课程学生 (根据加入状态)
     *
     * @param courseId 课程id
     * @return 课程学生
     */
    @GetMapping("/student/{courseId}")
    @ApiOperation("获取课程学生")
    public Result<List<StudentDTO>> getCourseStudentList(@PathVariable Long courseId, @RequestParam Integer joinState) {
        log.info("获取课程学生 courseId:{}", courseId);
        return Result.success(courseService.getCourseStudentList(courseId, joinState));
    }

    /**
     * 更新课程状态
     *
     * @param courseStatusDTO 课程状态信息
     */
    @PostMapping("/status")
    @ApiOperation("更新课程状态")
    public Result<Object> updateCourseStatusByTeacher(@RequestBody CourseStatusDTO courseStatusDTO) {
        log.info("更新课程状态 courseStatusDTO:{}", courseStatusDTO);
        courseService.updateCourseStatusByTeacher(courseStatusDTO);
        return Result.success();
    }
}
