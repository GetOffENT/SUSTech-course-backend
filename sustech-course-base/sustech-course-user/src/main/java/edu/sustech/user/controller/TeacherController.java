package edu.sustech.user.controller;

import edu.sustech.common.result.Result;
import edu.sustech.user.entity.Teacher;
import edu.sustech.user.entity.dto.TeacherDTO;
import edu.sustech.user.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 教师表 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-16
 */
@RestController
@RequestMapping("/user/teacher")
@Slf4j
@Api(tags = "教师信息相关接口")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    /**
     * 获取教师信息
     *
     * @param teacherId 教师ID
     * @return 教师信息
     */
    @GetMapping("/{teacherId}")
    @ApiOperation("获取教师信息")
    public Result<Teacher> getTeacherInfo(@PathVariable Long teacherId) {
        return Result.success(teacherService.getById(teacherId));
    }

    /**
     * 新增或更新教师信息
     *
     * @param teacherDTO 教师信息
     */
    @PostMapping
    @ApiOperation("新增或更新教师信息")
    public Result<Object> addOrUpdateTeacherInfo(TeacherDTO teacherDTO) {
        log.info("新增或更新教师信息: {}", teacherDTO);
        teacherService.addOrUpdateTeacherInfo(teacherDTO);
        return Result.success();

    }
}
