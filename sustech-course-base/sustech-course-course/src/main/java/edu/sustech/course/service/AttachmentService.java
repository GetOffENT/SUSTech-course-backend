package edu.sustech.course.service;

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
}
