package edu.sustech.interaction.mapper;

import edu.sustech.interaction.entity.CourseReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 课程评价 Mapper 接口
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-17
 */
public interface CourseReviewMapper extends BaseMapper<CourseReview> {

    /**
     * 获取课程平均分
     * @param courseId 课程id
     * @return 课程平均分
     */
    @Select("SELECT AVG(score) FROM course_review WHERE course_id = #{courseId} and is_delete = 0")
    Double selectAverageScore(Integer courseId);

    /**
     * 更新点赞数和点踩数
     *
     * @param id           课程评价id
     * @param likeCount    点赞变化数
     * @param dislikeCount 点踩变化数
     */
    @Update("update course_review set like_count = like_count + #{likeCount}, dislike_count = dislike_count + #{dislikeCount}, gmt_modified = now() where id = #{id}")
    void updateLikeCountAndDislikeCount(Long id, Integer likeCount, Integer dislikeCount);
}
