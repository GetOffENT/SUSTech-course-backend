package edu.sustech.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import edu.sustech.admin.entity.Carousel;
import edu.sustech.admin.entity.vo.CarouselAdminVO;
import edu.sustech.admin.entity.vo.CarouselVO;
import edu.sustech.admin.service.CarouselService;
import edu.sustech.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     *
     * @return 轮播图列表
     */
    @ApiOperation("获取需要展示的轮播图列表")
    @GetMapping("/list")
    public Result<List<CarouselVO>> getCarouselList() {
        log.info("获取需要展示的轮播图列表");
        return Result.success(carouselService.getCarouselList());
    }

    /**
     * 获取所有轮播图列表
     *
     * @return 所有轮播图列表
     */
    @ApiOperation("获取所有轮播图列表")
    @GetMapping("/all")
    public Result<List<CarouselAdminVO>> getAllCarouselList() {
        log.info("获取所有轮播图列表");
        return Result.success(carouselService.getAllCarouselList());
    }

    /**
     * 新增轮播图记录
     *
     * @param carouselVO 前端提交的轮播图
     * @return 新增记录
     */
    @ApiOperation("新增轮播图记录")
    @PostMapping("/save")
    public Result<Carousel> save(@RequestBody CarouselVO carouselVO) {
        log.info("新增轮播图记录");
        Carousel carousel = BeanUtil.copyProperties(carouselVO, Carousel.class);
        carouselService.save(carousel);
        return Result.success(carousel);
    }

    /**
     * 修改轮播图记录
     *
     * @param carouselVO 前端提交的轮播图
     * @return 修改后的记录
     */
    @ApiOperation("修改轮播图记录")
    @PutMapping
    public Result<Carousel> update(@RequestBody CarouselVO carouselVO) {
        log.info("更新轮播图记录");
        Carousel carousel = BeanUtil.copyProperties(carouselVO, Carousel.class);
        carouselService.updateById(carousel);
        return Result.success(carousel);
    }

    /**
     * 删除轮播图
     *
     * @param id 轮播图id
     * @return Result
     */
    @ApiOperation("删除轮播图")
    @DeleteMapping("/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        log.info("删除轮播图 {}", id);
        carouselService.removeById(id);
        return Result.success();
    }

    /**
     * 修改展示状态
     *
     * @param id 轮播图id
     * @return 修改后的轮播图
     */
    @ApiOperation("修改展示状态")
    @PostMapping("/show/{id}")
    public Result<CarouselAdminVO> showOrHide(@PathVariable Long id) {
        log.info("修改[{}]展示状态", id);
        Carousel carousel = carouselService.showOrHide(id);
        return Result.success(BeanUtil.copyProperties(carousel, CarouselAdminVO.class));
    }

}
