package edu.sustech.user.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.common.constant.AuthorizationConstant;
import edu.sustech.common.exception.RegisterException;
import edu.sustech.common.result.Result;
import edu.sustech.user.entity.User;
import edu.sustech.user.entity.dto.RegisterByEmailDTO;
import edu.sustech.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-08 3:37
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户相关接口")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final CaptchaService captchaService;


    /**
     * 通过邮箱获取用户
     * @param email 邮箱
     * @return 用户邮箱
     */
    @ApiOperation("通过邮箱获取用户")
    @GetMapping("/{email}")
    public Result<String> getByEmail(@PathVariable String email) {
        log.info("通过邮箱获取用户: {}", email);
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (user == null) {
            return Result.success();
        }
        return Result.error("用户已存在");
    }

    /**
     * 用户注册
     * @param registerByEmailDTO 注册信息
     * @return 成功响应结果
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody @Validated RegisterByEmailDTO registerByEmailDTO) {
        log.info("用户注册: {}", registerByEmailDTO);

        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(registerByEmailDTO.getCaptchaVerification());
        ResponseModel response = captchaService.verification(captchaVO);
        if (!response.isSuccess()) {
            String repCode = response.getRepCode();
            log.error("图形验证码校验失败: {}", repCode);
            throw new RuntimeException("验证码校验失败，请重新获取");
        }

        userService.register(registerByEmailDTO);
        return Result.success();
    }

}
