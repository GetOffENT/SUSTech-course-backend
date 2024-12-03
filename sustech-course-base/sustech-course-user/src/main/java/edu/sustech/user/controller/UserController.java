package edu.sustech.user.controller;

import edu.sustech.api.entity.dto.StudentDTO;
import edu.sustech.common.result.Result;
import edu.sustech.common.util.UserContext;
import edu.sustech.user.entity.dto.FoundByEmailDTO;
import edu.sustech.user.entity.dto.LoginByEmailDTO;
import edu.sustech.user.entity.dto.RegisterByEmailDTO;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-08 3:37
 */
@RestController
@RequestMapping("/user/user")
@Slf4j
@Api(tags = "用户相关接口")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 判断邮箱是否已经注册
     *
     * @param email 邮箱
     * @return 用户邮箱
     */
    @ApiOperation("判断邮箱是否已经注册")
    @GetMapping("/{type}/{email}")
    public Result<String> getByEmail(@PathVariable String type, @PathVariable String email) {
        log.info("判断邮箱是否已经注册: {}", email);
        return userService.judgeEmail(email, type);
    }

    /**
     * 用户注册
     *
     * @param registerByEmailDTO 注册信息
     * @return 成功响应结果
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<Object> register(@RequestBody @Validated RegisterByEmailDTO registerByEmailDTO) {
        log.info("用户注册: {}", registerByEmailDTO);
        userService.register(registerByEmailDTO);
        return Result.success();
    }

    /**
     * 用户登录
     *
     * @param loginByEmailDTO 登录信息
     * @return 用户信息
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginByEmailDTO loginByEmailDTO) {
        log.info("用户登录: {}", loginByEmailDTO);
        return Result.success(userService.login(loginByEmailDTO));
    }

    /**
     * 用户找回密码
     *
     * @param foundByEmailDTO 找回密码信息
     * @return 成功响应结果
     */
    @ApiOperation("用户找回密码")
    @PostMapping("/found")
    public Result<Object> foundPassword(@RequestBody @Validated FoundByEmailDTO foundByEmailDTO) {
        log.info("用户找回密码: {}", foundByEmailDTO);
        userService.foundPassword(foundByEmailDTO);
        return Result.success();
    }

    /**
     * 获取用户信息(包括其发布的课程数据汇总，提供远程调用)
     *
     * @param uid 用户id
     * @return 用户信息
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/info/{uid}")
    public Result<UserDTO> getUserAndCoursesDataById(@PathVariable Long uid) {
        log.info("获取用户信息: {}", uid);
        return Result.success(userService.getUserAndCoursesById(uid));
    }

    /**
     * 更新用户头像
     *
     * @param file 头像文件
     * @return 头像地址
     */
    @ApiOperation("更新用户头像")
    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> updateUserAvatar(@RequestPart("file") MultipartFile file) {
        log.info("更新用户头像: {}", file);
        return Result.success(userService.updateUserAvatar(file));
    }


    /**
     * 获取用户信息(提供远程调用)
     *
     * @param uid 用户id
     * @return 用户信息(不包含该用户发布的课程数据)
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/{uid}")
    public Result<UserDTO> getUserInfoById(@PathVariable Long uid) {
        log.info("获取用户信息(不包含课程信息): {}", uid);
        return Result.success(userService.getUserById(uid));
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    @ApiOperation("获取当前登录用户信息")
    @GetMapping
    public Result<UserDTO> getUserInfo() {
        log.info("根据token获取用户信息");
        return Result.success(userService.getUserById(UserContext.getUser()));
    }

    /**
     * 获取学生信息
     *
     * @param studentIds 学生id列表
     * @return 学生信息列表
     */
    @ApiOperation("获取学生信息")
    @GetMapping("/student")
    public Result<List<StudentDTO>> getStudentList(@RequestParam List<Long> studentIds) {
        log.info("获取学生信息: {}", studentIds);
        return Result.success(userService.getStudentList(studentIds));
    }

    /**
     * 根据搜索条件获取学生信息
     *
     * @param keyword 搜索条件
     * @return 学生信息列表
     */
    @ApiOperation("根据搜索条件获取学生信息")
    @GetMapping("/search")
    public Result<List<StudentDTO>> getSearchStudentList(@RequestParam String keyword) {
        log.info("根据搜索条件获取学生信息: {}", keyword);
        return Result.success(userService.getSearchStudentList(keyword));
    }
}
