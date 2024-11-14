package edu.sustech.interaction.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.common.result.Result;
import edu.sustech.interaction.entity.Danmu;
import edu.sustech.interaction.service.DanmuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 弹幕表 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-15
 */
@RestController
@RequestMapping("/interaction/danmu")
@Slf4j
@Api(tags = "弹幕相关接口")
@RequiredArgsConstructor
public class DanmuController {

    private final DanmuService danmuService;

    /**
     * 获取对应视频的弹幕列表
     *
     * @param vid 视频ID
     * @return 弹幕列表
     */
    @GetMapping("/{vid}")
    @ApiOperation("获取对应视频的弹幕列表")
    public Result<List<Danmu>> getDanmuList(@PathVariable("vid") String vid) {
        log.info("获取视频{}的弹幕列表", vid);
        List<Danmu> danmuList = danmuService.list(
                new LambdaQueryWrapper<Danmu>()
                        .eq(Danmu::getVideoId, vid)
        );
        return Result.success(danmuList);
    }
}
