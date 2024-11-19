package edu.sustech.course.controller;

import edu.sustech.course.service.UserVideoRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 添加观看记录
     *
     * @param recordJSON 观看记录对应JSON
     */
    @PostMapping
    @ApiOperation("添加观看记录")
    public void addRecord(@RequestBody String recordJSON) {
        log.info("添加观看记录: {}", recordJSON);
        userVideoRecordService.addRecord(recordJSON);
    }

}
