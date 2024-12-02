package edu.sustech.interaction.mapper;

import edu.sustech.interaction.entity.VideoComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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


    List<Long> selectIdsToDeleteRecursively(Long id);


    Integer deleteBatchByIds(List<Long> commentIds);

    @Update("update video_comment set like_count = like_count + #{likeCount}, dislike_count = dislike_count + #{dislikeCount}, gmt_modified = now() where id = #{id}")
    void updateLikeCountAndDislikeCount(Long id, Integer likeCount, Integer dislikeCount);
}
