package edu.sustech.course.mapper;

import edu.sustech.course.entity.Chapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.sustech.course.entity.dto.ChapterBaseForCatalogDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-14
 */
public interface ChapterMapper extends BaseMapper<Chapter> {

    /**
     * 获取课程章节列表(基本信息)
     *
     * @param courseId 课程id
     * @return 课程章节列表(基本信息)
     */
    @Select("SELECT id, title FROM chapter WHERE course_id = #{courseId} and is_delete = 0 order by sort")
    List<ChapterBaseForCatalogDTO> selectBaseInfoListByCourseId(Long courseId);
}
