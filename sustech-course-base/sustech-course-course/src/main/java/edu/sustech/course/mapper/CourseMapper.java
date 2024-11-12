package edu.sustech.course.mapper;

import edu.sustech.course.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-12
 */
public interface CourseMapper extends BaseMapper<Course> {

    @Select("select id from course where status = 1")
    Set<Integer> selectIds();
}
