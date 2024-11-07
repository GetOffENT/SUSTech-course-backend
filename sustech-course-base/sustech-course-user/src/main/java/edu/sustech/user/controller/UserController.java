package edu.sustech.user.controller;

import edu.sustech.api.client.MessageClient;
import edu.sustech.common.result.Result;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final MessageClient messageClient;

    @GetMapping
    public Result testToken() {

        return Result.success(messageClient.testToken());
    }
}
