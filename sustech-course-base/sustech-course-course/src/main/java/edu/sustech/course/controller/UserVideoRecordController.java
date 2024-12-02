package edu.sustech.course.controller;

import edu.sustech.common.result.Result;
import edu.sustech.course.entity.vo.UserVideoRecordVO;
import edu.sustech.course.service.UserVideoRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户观看记录 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-14
 */
@RestController
@RequestMapping("/course/user-video-record")
@Slf4j
@Api(tags = "用户视频记录相关接口")
@RequiredArgsConstructor
public class UserVideoRecordController {

    private final UserVideoRecordService userVideoRecordService;

    /**
     * 添加观看记录(前端使用navigator.sendBeacon在页面关闭或刷新前发送, 无需返回值)
     *
     * @param recordJSON 观看记录对应JSON
     */
    @PostMapping
    @ApiOperation("添加观看记录")
    public void addRecord(@RequestBody String recordJSON) {
        log.info("添加观看记录: {}", recordJSON);
        userVideoRecordService.addRecord(recordJSON);
    }

    /**
     * 获取用户指定课程观看记录
     *
     * @param courseId 课程ID
     */
    @GetMapping("/course")
    @ApiOperation("获取用户所有课程观看记录")
    public Result<List<UserVideoRecordVO>> getCourseRecords(@RequestParam Long courseId) {
        log.info("获取用户所有课程观看记录: {}", courseId);
        return Result.success(userVideoRecordService.getCourseRecords(courseId));
    }

}
