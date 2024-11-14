package edu.sustech.interaction.service;

import edu.sustech.interaction.entity.CommentVideo;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.interaction.entity.vo.CommentTreeVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
public interface CommentVideoService extends IService<CommentVideo> {

    /**
     * 获取对应视频的评论树
     *
     * @param vid    视频id
     * @param offset 已经获取的评论树数量
     * @param type   排序类型 1热度 2时间
     * @return 评论树列表
     */
    Map<String, Object> getCommentTree(Long vid, Long offset, Integer type);
}
