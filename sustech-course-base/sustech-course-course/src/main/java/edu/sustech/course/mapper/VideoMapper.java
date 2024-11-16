package edu.sustech.course.mapper;

import edu.sustech.course.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

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
}
