package edu.sustech.course.controller;

import edu.sustech.common.result.Result;
import edu.sustech.course.entity.vo.CategoryVO;
import edu.sustech.course.service.CategoryService;
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
 * 课程分类 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-12
 */
@RestController
@RequestMapping("/course/category")
@Slf4j
@Api(tags = "课程分类相关接口")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 获取全部分类
     * @return 全部分类
     */
    @GetMapping
    @ApiOperation("获取全部分类")
    public Result<List<CategoryVO>> getAllCategories() {
        log.info("获取全部分类...");
        return Result.success(categoryService.getAllCategories());
    }

}
