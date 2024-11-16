package edu.sustech.course.mapper;

import edu.sustech.course.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-12
 */
public interface CourseMapper extends BaseMapper<Course> {
    /**
     * 获取已发布且公开或半公开的课程id列表
     *
     * @return 已发布且公开或半公开的课程id列表
     */
    @Select("select id from course where status = 1 and open_state != 0")
    List<Long> selectPublishedCourseIds();

    /**
     * 更新课程的点赞数
     *
     * @param id    课程ID
     * @param count 点赞数
     */
    @Update("update course set like_count = like_count + #{count}, gmt_modified = now() where id = #{id}")
    void updateLikeCount(Long id, Integer count);

    /**
     * 更新课程的点踩数
     *
     * @param id    课程ID
     * @param count 点踩数
     */
    @Update("update course set dislike_count = dislike_count + #{count}, gmt_modified = now() where id = #{id}")
    void updateDislikeCount(Long id, Integer count);

    /**
     * 更新课程的点赞数和点踩数
     * @param id 课程ID
     * @param likeCount 点赞数
     * @param dislikeCount 点踩数
     */
    @Update("update course set like_count = like_count + #{likeCount}, dislike_count = dislike_count + #{dislikeCount}, gmt_modified = now() where id = #{id}")
    void updateLikeCountAndDislikeCount(Long id, Integer likeCount, Integer dislikeCount);
}
