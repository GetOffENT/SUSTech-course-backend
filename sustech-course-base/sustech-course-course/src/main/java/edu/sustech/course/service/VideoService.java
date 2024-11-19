package edu.sustech.course.service;

import edu.sustech.api.entity.dto.VideoResourceDTO;
import edu.sustech.course.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
public interface VideoService extends IService<Video> {

    /**
     * 获取单个视频信息
     *
     * @param id 视频ID
     * @return 单个视频信息(包含发布用户信息)
     */
    Map<String, Object> getVideoWithDataById(Long id);

    /**
     * 更新评论数量
     *
     * @param id    视频ID
     * @param count 评论数量
     */
    void updateCommentCount(Long id, Integer count);

    /**
     * 更新弹幕数量
     *
     * @param id    视频ID
     * @param count 弹幕数量
     */
    void updateDanmuCount(Long id, Integer count);

    /**
     * 添加视频资源
     *
     * @param videoResourceDTO 视频资源信息
     */
    void addVideoResource(VideoResourceDTO videoResourceDTO);
}
