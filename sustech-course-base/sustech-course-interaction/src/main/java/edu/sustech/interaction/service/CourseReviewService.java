package edu.sustech.interaction.service;

import edu.sustech.interaction.entity.CourseReview;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.interaction.entity.vo.CourseReviewVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程评价 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-17
 */
public interface CourseReviewService extends IService<CourseReview> {

    /**
     * 获取课程评价列表
     *
     * @param courseId 课程id
     * @param page     页码
     * @param pageSize 每页大小
     * @return 课程评价列表(" reviews ", 课程评价列表, " total ", 总数, " score ", 全部评价的平均分)
     */
    Map<String, Object> getCourseReviewList(Integer courseId, Integer page, Integer pageSize);

    /**
     * 添加课程评价
     *
     * @param courseReviewVO 课程评价
     * @param courseId       课程id
     */
    void addCourseReview(CourseReviewVO courseReviewVO, Long courseId);

    /**
     * 获取用户对课程评价
     * @param courseId 课程id
     * @return 课程评价
     */
    CourseReviewVO getCourseReview(Long courseId);
}
