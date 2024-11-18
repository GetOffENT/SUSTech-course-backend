package edu.sustech.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.VideoException;
import edu.sustech.common.result.Result;
import edu.sustech.common.result.ResultCode;
import edu.sustech.common.util.UserContext;
import edu.sustech.course.entity.Category;
import edu.sustech.course.entity.Course;
import edu.sustech.course.entity.UserCourse;
import edu.sustech.course.entity.Video;
import edu.sustech.course.entity.enums.CourseOpenStatus;
import edu.sustech.course.entity.enums.CourseStatus;
import edu.sustech.course.entity.enums.JoinEnum;
import edu.sustech.course.mapper.CategoryMapper;
import edu.sustech.course.mapper.CourseMapper;
import edu.sustech.course.mapper.UserCourseMapper;
import edu.sustech.course.mapper.VideoMapper;
import edu.sustech.course.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
