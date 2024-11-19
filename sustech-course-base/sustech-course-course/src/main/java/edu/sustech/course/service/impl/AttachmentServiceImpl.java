package edu.sustech.course.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.api.entity.dto.AttachmentDTO;
import edu.sustech.course.entity.Attachment;
import edu.sustech.course.entity.vo.AttachmentVO;
import edu.sustech.course.mapper.AttachmentMapper;
import edu.sustech.course.service.AttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 课程视频对应附件 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-18
 */
@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements AttachmentService {

    /**
     * 获取视频最新版附件列表
     *
     * @param videoId 视频ID
     * @return 附件列表
     */
    @Override
    public List<AttachmentVO> getAttachments(Long videoId) {
        List<Attachment> attachments = baseMapper.selectLatestAttachments(videoId);
        if (CollUtil.isNotEmpty(attachments)) {
            return BeanUtil.copyToList(attachments, AttachmentVO.class);
        }
        return List.of();
    }

    /**
     * 获取附件历史版本列表
     *
     * @param uuid 附件UUID
     * @return 附件历史版本列表
     */
    @Override
    public List<AttachmentVO> getAttachmentHistory(String uuid) {
        List<Attachment> attachments = this.list(
                new LambdaQueryWrapper<Attachment>()
                        .eq(Attachment::getUuid, uuid)
                        .orderByDesc(Attachment::getGmtCreate)
        );
        if (CollUtil.isNotEmpty(attachments)) {
            return BeanUtil.copyToList(attachments, AttachmentVO.class);
        }
        return List.of();
    }

    /**
     * 添加附件
     *
     * @param attachmentDTO 附件DTO
     * @return 附件ID
     */
    @Override
    public Long addAttachment(AttachmentDTO attachmentDTO) {
        Attachment attachment = BeanUtil.copyProperties(attachmentDTO, Attachment.class);
        attachment.setUuid(UUID.randomUUID().toString());
        this.save(attachment);
        return attachment.getId();
    }
}
