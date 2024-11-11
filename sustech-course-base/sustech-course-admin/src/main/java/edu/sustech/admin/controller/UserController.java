package edu.sustech.admin.controller;

import edu.sustech.admin.entity.dto.UserLoginDTO;
import edu.sustech.admin.entity.vo.UserVO;
import edu.sustech.common.result.MapResult;
import edu.sustech.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-10 22:41
 */
@RestController
@RequestMapping("/admin/user")
@Slf4j
@Api(tags = "用户相关接口")
@RequiredArgsConstructor
public class UserController {

    /**
     * 登录
     * @return 登录token
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public MapResult login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("登录, userLoginDTO: {}", userLoginDTO);
        return MapResult.success().data("token", "admin-token");
    }

    /**
     * 获取登录用户信息
     * @return 用户信息
     */
    @GetMapping("/info")
    @ApiOperation("获取登录用户信息")
    public Result<UserVO> info(String token) {
        log.info("获取登录用户信息, token: {}", token);
        UserVO userVO = UserVO.builder()
                .roles(List.of("admin"))
                .introduction("I am a super administrator")
                .avatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
                .name("Super Admin")
                .build();
        return Result.success(userVO);
    }
}
