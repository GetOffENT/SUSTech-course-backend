package edu.sustech.message.controller;

import edu.sustech.common.constant.AuthorizationConstant;
import edu.sustech.common.result.Result;
import edu.sustech.common.util.UserContext;
import edu.sustech.message.service.MailService;
import edu.sustech.message.util.EmailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-06 22:02
 */
@RestController
@RequestMapping("/email")
@Slf4j
@Api(tags = "邮箱相关接口")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    private final EmailUtil emailUtil;

    /**
     * 发送验证码
     *
     * @param email 邮箱
     * @return Result
     */
    @PostMapping("/sendCaptcha")
    @ApiOperation("发送验证码")
    public Result sendCaptcha(@RequestParam String email) {
        log.info("发送验证码到邮箱：{}", email);
        mailService.sendCaptcha(email);
        return Result.success();
    }


    @PostMapping
    public Result test(@RequestPart("file") MultipartFile file) {

        emailUtil.sendMail(List.of("12212618@mail.sustech.edu.cn"), "测试图片", "测试图片",
                "测试图片", false,
                List.of("12212613@mail.sustech.edu.cn"),
                null, List.of(file));

        return Result.success();
    }


    @GetMapping
    public Result testToken(@RequestHeader(value = "Authorization",required = false) String authorization) {
        System.out.println(authorization);
        System.out.println(UserContext.getUser());
        return Result.success(1);
    }
}
