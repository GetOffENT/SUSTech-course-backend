package edu.sustech.interaction.mapper;

import edu.sustech.interaction.entity.VideoComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 评论 Mapper 接口
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
public interface VideoCommentMapper extends BaseMapper<VideoComment> {


    /**
     * 递归查询所有要删除的评论id
     *
     * @param id 评论id
     * @return 评论id列表
     */
    List<Long> selectIdsToDeleteRecursively(Long id);

    /**
     * 批量删除评论
     *
     * @param commentIds 评论id列表
     * @return 删除数量
     */
    Integer deleteBatchByIds(List<Long> commentIds);

    /**
     * 更新点赞数和点踩数
     *
     * @param id           视频评论id
     * @param likeCount    点赞变化数
     * @param dislikeCount 点踩变化数
     */
    @Update("update video_comment set like_count = like_count + #{likeCount}, dislike_count = dislike_count + #{dislikeCount}, gmt_modified = now() where id = #{id}")
    void updateLikeCountAndDislikeCount(Long id, Integer likeCount, Integer dislikeCount);

    /**
     * 获取对应视频的根评论列表
     *
     * @param vid    视频id
     * @param limit  限制数量
     * @param offset 偏移量
     * @return 根评论列表
     */
    List<VideoComment> selectComments(
            @Param("vid") Long vid,
            @Param("rootId") Long rootId,
            @Param("limit") Long limit,
            @Param("offset") Long offset
    );
}
