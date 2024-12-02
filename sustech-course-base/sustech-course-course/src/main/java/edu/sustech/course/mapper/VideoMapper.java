package edu.sustech.course.mapper;

import edu.sustech.course.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.sustech.course.entity.dto.VideoBaseForCatalogDTO;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 课程视频 Mapper 接口
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
public interface VideoMapper extends BaseMapper<Video> {

    /**
     * 更新评论数量
     *
     * @param id    视频ID
     * @param count 评论数量
     */
    @Update("update video set comment = comment + #{count}, gmt_modified = now() where id = #{id}")
    void updateCommentCount(Long id, Integer count);


    /**
     * 更新弹幕数量
     *
     * @param id    视频ID
     * @param count 弹幕数量
     */
    @Update("update video set danmu = danmu + #{count}, gmt_modified = now() where id = #{id}")
    void updateDanmuCount(Long id, Integer count);

    /**
     * 根据章节id查询视频资源id列表
     *
     * @param chapterId 章节id
     * @return 视频资源id列表
     */
    @Select("select video_source_id from video where chapter_id = #{chapterId}")
    List<String> selectVideoSourceIdListByChapterId(Long chapterId);

    /**
     * 获取课程视频列表(基本信息)
     *
     * @param courseId 课程id
     * @return 课程视频列表(基本信息)
     */
    @Select("SELECT id, chapter_id, title, duration, min_watch_time FROM video WHERE course_id = #{courseId} and is_delete = 0 order by sort")
    List<VideoBaseForCatalogDTO> selectBaseInfoListByCourseId(Long courseId);
}
