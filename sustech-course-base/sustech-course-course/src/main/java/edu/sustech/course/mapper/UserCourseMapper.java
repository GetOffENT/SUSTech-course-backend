package edu.sustech.course.mapper;

import edu.sustech.course.entity.UserCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 课程用户关系 Mapper 接口
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-15
 */
public interface UserCourseMapper extends BaseMapper<UserCourse> {

    /**
     * 根据课程id查询不同加入状态用户id
     *
     * @param courseId 课程id
     * @return 用户id列表
     */
    @Select("select user_id from user_course where course_id = #{courseId} and join_state = #{joinState} and is_delete = 0")
    List<Long> selectUserIdsByCourseId(Long courseId, Integer joinState);
}
