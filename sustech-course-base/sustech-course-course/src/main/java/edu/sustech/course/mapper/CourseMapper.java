package edu.sustech.course.mapper;

import edu.sustech.course.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

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
     * 获取已经发布的课程id列表
     *
     * @return 已经发布的课程id列表
     */
    @Select("select id from course where status = 1")
    List<Long> selectPublishedCourseIds();
}
