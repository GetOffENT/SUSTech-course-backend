package edu.sustech.course.controller;

import edu.sustech.common.result.Result;
import edu.sustech.course.entity.dto.ChapterInfoDTO;
import edu.sustech.course.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-14
 */
@RestController
@RequestMapping("/course/chapter")
@Slf4j
@Api(tags = "章节相关接口")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    /**
     * 新增章节
     *
     * @param chapterInfoDTO 章节信息
     * @return 章节id
     */
    @PostMapping
    @ApiOperation("新增章节")
    public Result<Map<String, Long>> addChapter(@RequestBody ChapterInfoDTO chapterInfoDTO) {
        log.info("新增章节 chapterInfoDTO:{}", chapterInfoDTO);
        return Result.success(chapterService.addChapter(chapterInfoDTO));
    }
}
