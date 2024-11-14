package edu.sustech.course.service;

import edu.sustech.course.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.api.entity.dto.UserCourseInfoDTO;
import edu.sustech.course.entity.vo.ChapterVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-12
 */
public interface CourseService extends IService<Course> {

    /**
     * 获取随机推荐课程
     * @return 随机推荐课程
     */
    List<Map<String, Object>> getRandomRecommendCourses();

    /**
     * 根据用户id查询该用户的所有课程信息
     * @param id 用户id
     * @return 该用户的所有课程信息
     */
    UserCourseInfoDTO getUserCoursesInfoByUserId(Long id);

    /**
     * 累加获取更多课程
     * @param courseIds 已经获取的课程id列表
     * @return 返回十门新课程，以及其id列表，并标注是否有更多课程可以获取
     */
    Map<String, Object> getCumulativeCourses(List<Long> courseIds);


    /**
     * 获取课程目录, 如果已经登录，则还会获取用户每一个小节是否学习
     * @param courseId 课程id
     * @return 课程目录(包括小节 : title id isLearned isPublic)
     */
    List<ChapterVO> getCatalog(Long courseId);
}
