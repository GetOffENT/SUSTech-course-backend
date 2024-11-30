package edu.sustech.course.service;

import edu.sustech.course.entity.UserCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.course.entity.dto.CourseJoinStatusDTO;

/**
 * <p>
 * 课程用户关系 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-15
 */
public interface UserCourseService extends IService<UserCourse> {

    /**
     * 获取用户课程记录
     *
     * @param id 课程ID
     * @return 用户课程记录
     */
    UserCourse getUserCourse(Long id);

    /**
     * 点赞或点踩，返回更新后的信息
     *
     * @param id     课程ID
     * @param isLike 设置赞还是踩 true赞 false踩
     * @return 更新后的信息
     */
    UserCourse likeOrNot(Long id, Boolean isLike);

    /**
     * 申请加入课程
     *
     * @param id 课程ID
     * @return 用户课程记录
     */
    UserCourse applyCourse(Long id);

    /**
     * 更新加入状态
     *
     * @param courseJoinStatusDTO 课程加入状态DTO
     */
    void updateJoinStatus(CourseJoinStatusDTO courseJoinStatusDTO);
}
