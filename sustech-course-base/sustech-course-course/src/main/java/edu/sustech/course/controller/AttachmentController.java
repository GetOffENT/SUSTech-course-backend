package edu.sustech.course.controller;

import edu.sustech.api.entity.dto.AttachmentDTO;
import edu.sustech.common.result.Result;
import edu.sustech.course.entity.vo.AttachmentVO;
import edu.sustech.course.service.AttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 添加附件
     *
     * @param attachmentDTO 附件DTO
     * @return 附件ID
     */
    @PostMapping
    @ApiOperation("添加附件")
    public Result<Long> addAttachment(@RequestBody AttachmentDTO attachmentDTO) {
        log.info("添加附件 attachmentDTO:{}", attachmentDTO);
        return Result.success(attachmentService.addAttachment(attachmentDTO));
    }

    /**
     * 删除附件
     *
     * @param attachmentId 附件ID
     * @return 响应对象
     */
    @DeleteMapping("/{attachmentId}")
    @ApiOperation("删除附件")
    public Result<Object> deleteAttachment(@PathVariable Long attachmentId) {
        log.info("删除附件 attachmentId:{}", attachmentId);
        attachmentService.deleteAttachment(attachmentId);
        return Result.success();
    }

    /**
     * 根据UUID删除附件所有版本
     *
     * @param attachmentId 附件ID
     * @return 无
     */
    @DeleteMapping("/uuid/{attachmentId}")
    @ApiOperation("删除附件所有版本")
    public Result<Object> deleteAttachmentByUuid(@PathVariable Long attachmentId) {
        log.info("删除附件所有版本 uuid:{}", attachmentId);
        attachmentService.deleteAttachmentByUuid(attachmentId);
        return Result.success();
    }

    /**
     * 更新附件下载权限
     *
     * @param attachmentId 附件ID
     * @param isDownload   是否可以下载
     * @return 无
     */
    @PostMapping("/download/{attachmentId}")
    @ApiOperation("更新附件下载权限")
    public Result<Object> updateAttachment(@PathVariable Long attachmentId, @RequestParam Byte isDownload) {
        log.info("更新附件下载权限 attachmentId:{} isDownload:{}", attachmentId, isDownload);
        attachmentService.updateAttachment(attachmentId, isDownload);
        return Result.success();
    }
}
