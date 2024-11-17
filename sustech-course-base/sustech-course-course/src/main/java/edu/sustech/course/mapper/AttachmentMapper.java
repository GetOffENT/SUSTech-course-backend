package edu.sustech.course.mapper;

import edu.sustech.course.entity.Attachment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 课程视频对应附件 Mapper 接口
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-18
 */
public interface AttachmentMapper extends BaseMapper<Attachment> {

    /**
     * 获取视频最新版附件
     * @param videoId 视频ID
     * @return 附件列表
     */
    List<Attachment> selectLatestAttachments(Long videoId);
}
