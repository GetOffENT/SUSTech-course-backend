package edu.sustech.course.service;

import edu.sustech.api.entity.dto.AttachmentDTO;
import edu.sustech.course.entity.Attachment;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.course.entity.vo.AttachmentVO;

import java.util.List;

/**
 * <p>
 * 课程视频对应附件 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-18
 */
public interface AttachmentService extends IService<Attachment> {

    /**
     * 获取视频最新版附件列表
     *
     * @param videoId 视频ID
     * @return 附件列表
     */
    List<AttachmentVO> getAttachments(Long videoId);

    /**
     * 获取附件历史版本
     *
     * @param uuid 附件UUID
     * @return 附件历史版本
     */
    List<AttachmentVO> getAttachmentHistory(String uuid);

    /**
     * 添加附件
     *
     * @param attachmentDTO 附件DTO
     * @return 附件ID
     */
    Long addAttachment(AttachmentDTO attachmentDTO);

    /**
     * 删除附件
     *
     * @param attachmentId 附件ID
     */
    void deleteAttachment(Long attachmentId);

    /**
     * 更新附件下载状态
     *
     * @param attachmentId 附件ID
     * @param isDownload   是否可以下载
     */
    void updateAttachment(Long attachmentId, Byte isDownload);

    /**
     * 根据uuid删除附件所有版本
     *
     * @param attachmentId 附件ID
     */
    void deleteAttachmentByUuid(Long attachmentId);
}
