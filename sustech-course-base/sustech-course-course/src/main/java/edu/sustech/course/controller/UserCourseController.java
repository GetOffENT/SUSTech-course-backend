package edu.sustech.course.controller;

import edu.sustech.common.result.Result;
import edu.sustech.common.util.UserContext;
import edu.sustech.course.entity.UserCourse;
import edu.sustech.course.entity.dto.CourseJoinStatusDTO;
import edu.sustech.course.service.UserCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程用户关系 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-15
 */
@RestController
@RequestMapping("/course/user-course")
@Slf4j
@Api(tags = "用户课程记录相关接口")
@RequiredArgsConstructor
public class UserCourseController {

    private final UserCourseService userCourseService;

    /**
     * 获取用户课程记录
     *
     * @param id 课程ID
     * @return 用户课程记录
     */
    @GetMapping("/{id}")
    @ApiOperation("获取用户课程记录")
    public Result<UserCourse> getUserCourse(@PathVariable Long id) {
        log.info("获取用户【{}】的课程记录", id);
        return Result.success(userCourseService.getUserCourse(id));
    }

    /**
     * 点赞或点踩，返回更新后的信息
     *
     * @param id     课程ID
     * @param isLike 设置赞还是踩 true赞 false踩
     * @return 更新后的信息
     */
    @PostMapping("/like-or-not/{id}")
    @ApiOperation("点赞或点踩")
    public Result<UserCourse> likeOrNot(@PathVariable Long id, @RequestParam Boolean isLike) {
        log.info("用户【{}】设置课程【{}】的【{}】状态", UserContext.getUser(), id, isLike ? "点赞" : "点踩");
        return Result.success(userCourseService.likeOrNot(id, isLike));
    }

    /**
     * 申请或取消申请加入课程
     *
     * @param id 课程ID
     * @return 用户课程记录
     */
    @PostMapping("/apply/{id}")
    @ApiOperation("申请或取消申请加入课程")
    public Result<UserCourse> applyCourse(@PathVariable Long id) {
        log.info("用户【{}】申请或取消申请加入课程【{}】", UserContext.getUser(), id);
        return Result.success(userCourseService.applyCourse(id));
    }


    /**
     * 更新加入状态
     *
     * @param courseJoinStatusDTO 课程加入状态DTO
     * @return 无
     */
    @PostMapping("/update")
    @ApiOperation("更新加入状态")
    public Result<Object> updateJoinStatus(@RequestBody CourseJoinStatusDTO courseJoinStatusDTO) {
        log.info("教师【{}】更新课程【{}】学生【{}】的加入状态为【{}】", UserContext.getUser(), courseJoinStatusDTO.getCourseId(), courseJoinStatusDTO.getUserId(), courseJoinStatusDTO.getStatus());
        userCourseService.updateJoinStatus(courseJoinStatusDTO);
        return Result.success();
    }
}
