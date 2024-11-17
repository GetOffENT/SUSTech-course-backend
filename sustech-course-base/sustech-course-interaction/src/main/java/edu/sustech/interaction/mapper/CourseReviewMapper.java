package edu.sustech.interaction.mapper;

import edu.sustech.interaction.entity.CourseReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

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
    @Select("SELECT AVG(score) FROM course_review WHERE course_id = #{courseId}")
    Double selectAverageScore(Integer courseId);
}
