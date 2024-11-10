package edu.sustech.admin.controller;

import edu.sustech.admin.entity.vo.CarouselVO;
import edu.sustech.admin.service.CarouselService;
import edu.sustech.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 轮播图表 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-10
 */
@RestController
@RequestMapping("/admin/carousel")
@Slf4j
@Api(tags = "轮播图相关接口")
@RequiredArgsConstructor
public class CarouselController {

    private final CarouselService carouselService;

    /**
     * 获取需要展示的轮播图列表
     * @return 轮播图列表
     */
    @ApiOperation("获取需要展示的轮播图列表")
    @GetMapping("/list")
    public Result<List<CarouselVO>> getCarouselList() {
        log.info("获取需要展示的轮播图列表");
        return Result.success(carouselService.getCarouselList());
    }

}
