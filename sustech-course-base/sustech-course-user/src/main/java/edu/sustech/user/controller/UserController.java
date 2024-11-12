package edu.sustech.user.controller;

import edu.sustech.common.result.Result;
import edu.sustech.user.entity.dto.FoundByEmailDTO;
import edu.sustech.user.entity.dto.RegisterByEmailDTO;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result register(@RequestBody @Validated RegisterByEmailDTO registerByEmailDTO) {
        log.info("用户注册: {}", registerByEmailDTO);
        userService.register(registerByEmailDTO);
        return Result.success();
    }

    /**
     * 用户找回密码
     *
     * @param foundByEmailDTO 找回密码信息
     * @return 成功响应结果
     */
    @ApiOperation("用户找回密码")
    @PostMapping("/found")
    public Result foundPassword(@RequestBody @Validated FoundByEmailDTO foundByEmailDTO) {
        log.info("用户找回密码: {}", foundByEmailDTO);
        userService.foundPassword(foundByEmailDTO);
        return Result.success();
    }

    /**
     * 获取用户信息(包括其发布的课程信息汇总)
     *
     * @param uid 用户id
     * @return 用户信息
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/{uid}")
    public Result<UserDTO> getUserAndCoursesById(@PathVariable Long uid) {
        log.info("获取用户信息: {}", uid);
        return Result.success(userService.getUserAndCoursesById(uid));
    }


    /**
     * 获取用户信息(提供远程调用)
     *
     * @param uid 用户id
     * @return 用户信息(不包含该用户发布的课程信息)
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/info/{uid}")
    public Result<UserDTO> getUserInfo(@PathVariable Long uid) {
        log.info("获取用户信息(不包含课程信息): {}", uid);
        return Result.success(userService.getUserById(uid));
    }


}
