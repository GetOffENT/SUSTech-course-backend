package edu.sustech.interaction.service;

import edu.sustech.interaction.entity.VideoComment;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.interaction.entity.dto.CommentDTO;
import edu.sustech.interaction.entity.vo.CommentTreeVO;

import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
public interface VideoCommentService extends IService<VideoComment> {

    /**
     * 获取对应视频的评论树
     *
     * @param vid    视频id
     * @param offset 已经获取的评论树数量
     * @param type   排序类型 1热度 2时间
     * @return 评论树列表
     */
    Map<String, Object> getCommentTree(Long vid, Long offset, Integer type);

    /**
     * 获取指定id根评论的评论树
     *
     * @param id 根评论id
     * @return 单棵评论树
     */
    CommentTreeVO getCommentTreeById(Long id);

    /**
     * 新增评论
     *
     * @param commentDTO 评论表单
     * @return 单棵评论树
     */
    CommentTreeVO saveComment(CommentDTO commentDTO);

    /**
     * 删除评论
     *
     * @param id 评论id
     */
    void deleteComment(Long id);
}
