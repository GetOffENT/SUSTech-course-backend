package edu.sustech.course.controller;

import edu.sustech.common.result.Result;
import edu.sustech.course.entity.vo.AttachmentVO;
import edu.sustech.course.service.AttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 附件 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-19 22:31
 */
@RestController
@RequestMapping("/course/attachment")
@Slf4j
@Api(tags = "附件相关接口")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    /**
     * 获取视频最新版附件列表
     *
     * @param videoId 视频ID
     * @return 附件列表
     */
    @GetMapping("/list")
    @ApiOperation("获取视频附件")
    public Result<List<AttachmentVO>> getAttachments(@RequestParam Long videoId) {
        log.info("获取视频附件 videoId:{}", videoId);
        return Result.success(attachmentService.getAttachments(videoId));
    }

    /**
     * 获取指定附件历史版本列表
     *
     * @param uuid 附件uuid
     * @return 指定附件历史版本列表
     */
    @GetMapping("/history")
    @ApiOperation("获取指定附件历史版本")
    public Result<List<AttachmentVO>> getAttachmentHistory(@RequestParam String uuid) {
        log.info("获取指定附件历史版本 uuid:{}", uuid);
        return Result.success(attachmentService.getAttachmentHistory(uuid));
    }
}
