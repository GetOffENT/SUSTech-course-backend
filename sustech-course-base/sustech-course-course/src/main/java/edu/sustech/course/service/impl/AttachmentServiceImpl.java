package edu.sustech.course.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.api.entity.dto.AttachmentDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.ResourceOperationException;
import edu.sustech.common.util.UserContext;
import edu.sustech.course.entity.Attachment;
import edu.sustech.course.entity.Course;
import edu.sustech.course.entity.vo.AttachmentVO;
import edu.sustech.course.mapper.AttachmentMapper;
import edu.sustech.course.mapper.CourseMapper;
import edu.sustech.course.service.AttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.course.util.CommonUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements AttachmentService {

    private final CourseMapper courseMapper;

    private final CommonUtil commonUtil;

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
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new ResourceOperationException(MessageConstant.NOT_LOGIN);
        }

        Long courseId = attachmentDTO.getCourseId();
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new ResourceOperationException(MessageConstant.COURSE_NOT_EXIST);
        }
        if (!course.getUserId().equals(userId)) {
            throw new ResourceOperationException(MessageConstant.NO_PERMISSION);
        }

        Attachment attachment = BeanUtil.copyProperties(attachmentDTO, Attachment.class);
        attachment.setUuid(UUID.randomUUID().toString()).setUserId(userId);
        boolean saved = this.save(attachment);
        if (!saved) {
            throw new ResourceOperationException(MessageConstant.ATTACHMENT_ADD_FAILED);
        }
        return attachment.getId();
    }

    /**
     * 删除附件
     *
     * @param attachmentId 附件ID
     */
    @Override
    public void deleteAttachment(Long attachmentId) {
        checkAttachment(attachmentId);
        boolean removeById = this.removeById(attachmentId);
        if (!removeById) {
            throw new ResourceOperationException(MessageConstant.ATTACHMENT_DELETE_FAILED);
        }
    }

    /**
     * 更新附件下载状态
     *
     * @param attachmentId 附件ID
     * @param isDownload   是否可以下载
     */
    @Override
    public void updateAttachment(Long attachmentId, Byte isDownload) {
        Attachment attachment = checkAttachment(attachmentId);
        attachment.setIsDownload(isDownload);
        boolean updated = this.updateById(attachment);
        if (!updated) {
            throw new ResourceOperationException(MessageConstant.ATTACHMENT_UPDATE_FAILED);
        }
    }

    private Attachment checkAttachment(Long attachmentId) {
        Long userId = commonUtil.checkUser();
        Attachment attachment = this.getById(attachmentId);
        if (attachment == null) {
            throw new ResourceOperationException(MessageConstant.ATTACHMENT_NOT_EXIST);
        }
        if (!attachment.getUserId().equals(userId)) {
            throw new ResourceOperationException(MessageConstant.NO_PERMISSION);
        }
        return attachment;
    }
}
