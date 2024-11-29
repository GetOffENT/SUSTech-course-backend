package edu.sustech.course.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.api.client.ResourceClient;
import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.api.entity.dto.VideoDTO;
import edu.sustech.api.entity.dto.VideoResourceDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CourseException;
import edu.sustech.common.exception.VideoException;
import edu.sustech.common.result.Result;
import edu.sustech.common.enums.ResultCode;
import edu.sustech.common.util.UserContext;
import edu.sustech.course.entity.*;
import edu.sustech.api.entity.enums.CourseOpenStatus;
import edu.sustech.api.entity.enums.CourseStatus;
import edu.sustech.course.entity.enums.JoinEnum;
import edu.sustech.course.mapper.*;
import edu.sustech.course.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.course.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    private final UserClient userClient;

    private final CategoryMapper categoryMapper;

    private final CourseMapper courseMapper;

    private final UserCourseMapper userCourseMapper;

    private final UserVideoRecordMapper userVideoRecordMapper;

    private final ResourceClient resourceClient;

    private final CommonUtil commonUtil;

    private final RabbitTemplate rabbitTemplate;

    /**
     * 获取单个视频信息
     *
     * @param id 视频ID
     * @return 单个视频信息(包含发布用户信息)
     */
    @Override
    public Map<String, Object> getVideoWithDataById(Long id) {

        // TODO: Redis缓存优化


        Video video = baseMapper.selectById(id);
        if (video == null) {
            throw new VideoException(MessageConstant.VIDEO_NOT_EXIST);
        }
        video.setVideoSourceId(getPlayInfo(video.getVideoSourceId()));

        // 先查询课程公开状态和审核状态
        Course course = courseMapper.selectOne(
                new LambdaQueryWrapper<Course>()
                        .eq(Course::getId, video.getCourseId())
        );
        if (course == null) {
            throw new VideoException(MessageConstant.VIDEO_NOT_EXIST);
        }

        Long userId = UserContext.getUser();

        UserCourse userCourse = null;
        if (userId != null) {
            userCourse = userCourseMapper.selectOne(
                    new LambdaQueryWrapper<UserCourse>()
                            .eq(UserCourse::getUserId, userId)
                            .eq(UserCourse::getCourseId, video.getCourseId())
            );
        }

        if (course.getOpenState() == CourseOpenStatus.NOT_OPEN) {
            if (userId == null) {
                throw new VideoException(MessageConstant.VIDEO_NOT_EXIST);
            }

            // 非发布用户查看视频
            if (!userId.equals(video.getUserId())) {
                // 已登录的用户是否加入了课程
                if (userCourse == null || userCourse.getJoinState() != JoinEnum.JOINED) {
                    throw new VideoException(MessageConstant.VIDEO_NOT_EXIST);
                }
            }
        }

        // 课程没有通过审核或者视频未公开
        if (course.getStatus() != CourseStatus.PASSED || video.getIsPublic() == 0) {
            // 登录用户不是发布者 并且 登录用户没有加入课程
            if (!video.getUserId().equals(userId)
                    && (userCourse == null || userCourse.getJoinState() != JoinEnum.JOINED)) {
                throw new VideoException(MessageConstant.VIDEO_NOT_PUBLIC);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("video", video);
        map.put("course", course);
        map.put("userCourse", userCourse);
        map.put("userVideoRecord", userVideoRecordMapper.selectOne(
                new LambdaQueryWrapper<UserVideoRecord>()
                        .eq(UserVideoRecord::getUserId, userId)
                        .eq(UserVideoRecord::getVideoId, id)
        ));

        // 发布者信息
        Result<UserDTO> userAndCoursesById = userClient.getUserAndCoursesById(video.getUserId());

        if (Objects.equals(userAndCoursesById.getCode(), ResultCode.SUCCESS.code())) {
            map.put("user", userAndCoursesById.getData());
        }


        // 查询视频分类信息
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getMcId, course.getMcId()).eq(Category::getScId, course.getScId()));
        map.put("category", category);
        return map;
    }

    /**
     * 更新评论数量
     *
     * @param id    视频ID
     * @param count 评论数量
     */
    @Override
    public void updateCommentCount(Long id, Integer count) {
        baseMapper.updateCommentCount(id, count);
    }

    /**
     * 更新弹幕数量
     *
     * @param id    视频ID
     * @param count 弹幕数量
     */
    @Override
    public void updateDanmuCount(Long id, Integer count) {
        baseMapper.updateDanmuCount(id, count);
    }

    /**
     * 添加视频资源
     *
     * @param videoResourceDTO 视频资源信息
     */
    @Override
    public void addVideoResource(VideoResourceDTO videoResourceDTO) {
        Video video = BeanUtil.copyProperties(videoResourceDTO, Video.class);
        int row = baseMapper.updateById(video);
        if (row == 0) {
            // 删除阿里云视频
            resourceClient.removeAlyVideo(videoResourceDTO.getVideoSourceId());
            throw new VideoException(MessageConstant.ADD_VIDEO_RESOURCE_FAILED);
        }
    }

    /**
     * 获取视频播放信息
     *
     * @param videoSourceId 视频源id
     * @return 视频播放地址
     */
    @Override
    public String getPlayInfo(String videoSourceId) {
        if (videoSourceId.startsWith("http") || videoSourceId.startsWith("https")) {
            return videoSourceId;
        }
        Result<String> playInfo = resourceClient.getPlayInfo(videoSourceId);
        if (Objects.equals(playInfo.getCode(), ResultCode.SUCCESS.code())) {
            return playInfo.getData();
        } else {
            throw new VideoException(MessageConstant.VIDEO_NOT_EXIST);
        }
    }

    /**
     * 新增视频(小节)
     *
     * @param videoDTO 视频(小节)信息
     * @return 视频(小节)id
     */
    @Override
    public Map<String, Long> addVideo(VideoDTO videoDTO) {
        Long userId = commonUtil.checkTeacher();

        Video video = BeanUtil.copyProperties(videoDTO, Video.class)
                .setUserId(userId);

        int insert = baseMapper.insert(video);
        if (insert == 0) {
            throw new CourseException(MessageConstant.VIDEO_ADD_FAILED);
        }
        return Map.of("videoId", video.getId());
    }

    /**
     * 删除视频
     *
     * @param videoId 视频id
     */
    @Override
    @Transactional
    public void deleteVideo(Long videoId) {
        Long userId = commonUtil.checkTeacher();

        Video video = baseMapper.selectById(videoId);

        if (!userId.equals(video.getUserId())) {
            throw new VideoException(MessageConstant.NO_PERMISSION);
        }

        int delete = baseMapper.deleteById(videoId);
        if (delete == 0) {
            throw new VideoException(MessageConstant.VIDEO_DELETE_FAILED);
        }

        try {
            // 消息队列通知云端删除视频
            rabbitTemplate.convertAndSend("resource.direct", "resource.video.remove", video.getVideoSourceId());
        } catch (Exception e) {
            log.error("向消息队列发送删除视频消息失败", e);
            throw new VideoException(MessageConstant.VIDEO_DELETE_FAILED);
        }
    }

    /**
     * 删除视频资源
     *
     * @param videoId 视频id
     */
    @Override
    public void removeVideoResource(Long videoId) {
        Long userId = commonUtil.checkTeacher();

        Video video = baseMapper.selectById(videoId);

        if (video == null) {
            throw new VideoException(MessageConstant.VIDEO_NOT_EXIST);
        }

        if (!userId.equals(video.getUserId())) {
            throw new VideoException(MessageConstant.NO_PERMISSION);
        }

        String videoSourceId = video.getVideoSourceId();

        video.setVideoSourceId("")
                .setVideoOriginalName("")
                .setSize(0L)
                .setDuration(0.0)
                .setMinWatchTime(0.0);
        int row = baseMapper.updateById(video);
        if (row == 0) {
            throw new VideoException(MessageConstant.VIDEO_DELETE_FAILED);
        }

        try {
            // 消息队列通知云端删除视频
            rabbitTemplate.convertAndSend("resource.direct", "resource.video.remove", videoSourceId);
        } catch (Exception e) {
            log.error("向消息队列发送删除视频消息失败", e);
            throw new VideoException(MessageConstant.VIDEO_DELETE_FAILED);
        }
    }
}
