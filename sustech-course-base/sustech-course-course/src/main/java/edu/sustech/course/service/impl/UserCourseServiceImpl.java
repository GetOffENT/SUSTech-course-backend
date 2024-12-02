package edu.sustech.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CourseException;
import edu.sustech.course.entity.Course;
import edu.sustech.course.entity.UserCourse;
import edu.sustech.course.entity.dto.CourseJoinStatusDTO;
import edu.sustech.common.enums.JoinStatus;
import edu.sustech.course.entity.enums.LikeEnum;
import edu.sustech.course.mapper.CourseMapper;
import edu.sustech.course.mapper.UserCourseMapper;
import edu.sustech.course.service.UserCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.course.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 课程用户关系 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserCourseServiceImpl extends ServiceImpl<UserCourseMapper, UserCourse> implements UserCourseService {

    private final CourseMapper courseMapper;

    private final CommonUtil commonUtil;

    private final RabbitTemplate rabbitTemplate;

    /**
     * 获取用户课程记录
     *
     * @param id 课程ID
     * @return 用户课程记录
     */
    @Override
    public UserCourse getUserCourse(Long id) {
        Long userId = commonUtil.checkUser();
        return baseMapper.selectOne(
                new LambdaQueryWrapper<UserCourse>()
                        .eq(UserCourse::getCourseId, id)
                        .eq(UserCourse::getUserId, userId)
        );
    }

    /**
     * 点赞或点踩，返回更新后的信息
     *
     * @param id     课程ID
     * @param isLike 设置赞或踩 true赞 false踩
     * @return 更新后的信息
     */
    @Transactional
    @Override
    public UserCourse likeOrNot(Long id, Boolean isLike) {
        UserCourse userCourse = checkUserCourse(id, null);

        if (isLike) {
            if (userCourse.getLike() == LikeEnum.LIKE) {
                courseMapper.updateLikeCount(id, -1);
                userCourse.setLike(LikeEnum.NONE);
            } else {
                courseMapper.updateLikeCountAndDislikeCount(id, 1, userCourse.getLike() == LikeEnum.DISLIKE ? -1 : 0);
                userCourse.setLike(LikeEnum.LIKE);
            }
        } else {
            if (userCourse.getLike() == LikeEnum.DISLIKE) {
                courseMapper.updateDislikeCount(id, -1);
                userCourse.setLike(LikeEnum.NONE);
            } else {
                courseMapper.updateLikeCountAndDislikeCount(id, userCourse.getLike() == LikeEnum.LIKE ? -1 : 0, 1);
                userCourse.setLike(LikeEnum.DISLIKE);
            }
        }

        this.updateById(userCourse);
        return userCourse;
    }

    /**
     * 申请加入课程
     *
     * @param id 课程ID
     * @return 用户课程记录
     */
    @Override
    public UserCourse applyCourse(Long id) {
        UserCourse userCourse = checkUserCourse(id, null);

        if (userCourse.getJoinState() == JoinStatus.APPLYING) {
            userCourse.setJoinState(JoinStatus.NONE);
            this.updateById(userCourse);
            return userCourse;
        }
        if (userCourse.getJoinState() == JoinStatus.JOINED) {
            throw new CourseException(MessageConstant.ALREADY_JOINED);
        }
        userCourse.setJoinState(JoinStatus.APPLYING);
        this.updateById(userCourse);
        return userCourse;
    }

    /**
     * 更新加入状态
     *
     * @param courseJoinStatusDTO 课程加入状态DTO
     */
    @Override
    public void updateJoinStatus(CourseJoinStatusDTO courseJoinStatusDTO) {
        Course course = courseMapper.selectById(courseJoinStatusDTO.getCourseId());
        if (course == null) {
            throw new CourseException(MessageConstant.COURSE_NOT_EXIST);
        }
        // 检查是否是自己的课程
        if (!course.getUserId().equals(commonUtil.checkUser())) {
            throw new CourseException(MessageConstant.NO_PERMISSION);
        }

        UserCourse userCourse = checkUserCourse(courseJoinStatusDTO.getCourseId(), courseJoinStatusDTO.getUserId());
        userCourse.setJoinState(courseJoinStatusDTO.getStatus());
        boolean updateById = this.updateById(userCourse);
        if (!updateById) {
            throw new CourseException(MessageConstant.ERROR);
        }

        // 消息队列通知消息服务 推送邮件
        try {
            courseJoinStatusDTO.setTitle(course.getTitle());
            rabbitTemplate.convertAndSend(
                    "course.joinStatus.direct", "course.joinStatus.notify",
                    courseJoinStatusDTO
            );
        } catch (Exception e) {
            log.error("发送邮件失败", e);
            throw new CourseException(MessageConstant.ERROR);
        }
    }

    /**
     * 检查数据库是否存在用户课程记录, 不存在则创建
     *
     * @param id     课程ID
     * @param userId 被检查用户ID(如果为null则检查当前用户)
     * @return 用户课程记录
     */
    private UserCourse checkUserCourse(Long id, Long userId) {
        if (userId == null) {
            userId = commonUtil.checkUser();
        }
        LambdaQueryWrapper<UserCourse> queryWrapper =
                new LambdaQueryWrapper<UserCourse>()
                        .eq(UserCourse::getCourseId, id)
                        .eq(UserCourse::getUserId, userId);
        UserCourse userCourse = baseMapper.selectOne(queryWrapper);
        if (userCourse == null) {
            userCourse = new UserCourse().setCourseId(id).setUserId(userId);
            boolean saved = this.save(userCourse);
            if (saved) {
                userCourse = baseMapper.selectOne(queryWrapper);
            } else {
                throw new CourseException(MessageConstant.ERROR);
            }
        }
        return userCourse;
    }
}
