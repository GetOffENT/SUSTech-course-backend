package edu.sustech.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CourseException;
import edu.sustech.course.entity.UserCourse;
import edu.sustech.course.entity.enums.JoinEnum;
import edu.sustech.course.entity.enums.LikeEnum;
import edu.sustech.course.mapper.CourseMapper;
import edu.sustech.course.mapper.UserCourseMapper;
import edu.sustech.course.service.UserCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.course.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        UserCourse userCourse = checkUserCourse(id);

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
        UserCourse userCourse = checkUserCourse(id);

        if (userCourse.getJoinState() == JoinEnum.APPLYING) {
            userCourse.setJoinState(JoinEnum.NONE);
            this.updateById(userCourse);
            return userCourse;
        }
        if (userCourse.getJoinState() == JoinEnum.JOINED) {
            throw new CourseException(MessageConstant.ALREADY_JOINED);
        }
        userCourse.setJoinState(JoinEnum.APPLYING);
        this.updateById(userCourse);
        return userCourse;
    }

    private UserCourse checkUserCourse(Long id) {
        Long userId = commonUtil.checkUser();

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
