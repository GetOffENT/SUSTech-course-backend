package edu.sustech.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.common.result.Result;
import edu.sustech.user.entity.User;
import edu.sustech.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户相关接口")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{email}")
    public Result<String> getByEmail(@PathVariable String email) {
        log.info("通过邮箱获取用户: {}", email);
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (user == null) {
            return Result.success();
        }
        return Result.error("用户已存在");
    }


}
