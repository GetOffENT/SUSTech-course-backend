package edu.sustech.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CommentException;
import edu.sustech.common.util.UserContext;
import edu.sustech.course.entity.UserCourse;
import edu.sustech.course.entity.enums.LikeEnum;
import edu.sustech.course.mapper.UserCourseMapper;
import edu.sustech.course.service.UserCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程用户关系 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-15
 */
@Service
public class UserCourseServiceImpl extends ServiceImpl<UserCourseMapper, UserCourse> implements UserCourseService {

    /**
     * 获取用户课程记录
     *
     * @param id 课程ID
     * @return 用户课程记录
     */
    @Override
    public UserCourse getUserCourse(Long id) {
        Long userId = checkUser();
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
    @Override
    public UserCourse likeOrNot(Long id, Boolean isLike) {
        Long userId = checkUser();

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
                throw new RuntimeException(MessageConstant.ERROR);
            }
        }

        if (isLike) {
            if (userCourse.getLike() == LikeEnum.LIKE) {
                userCourse.setLike(LikeEnum.NONE);
            } else {
                userCourse.setLike(LikeEnum.LIKE);
            }
        } else {
            if (userCourse.getLike() == LikeEnum.DISLIKE) {
                userCourse.setLike(LikeEnum.NONE);
            } else {
                userCourse.setLike(LikeEnum.DISLIKE);
            }
        }

        this.updateById(userCourse);
        return userCourse;
    }

    private Long checkUser() {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CommentException(MessageConstant.NOT_LOGIN);
        }
        return userId;
    }
}
