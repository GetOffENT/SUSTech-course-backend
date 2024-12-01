package edu.sustech.course.service;

import edu.sustech.api.entity.dto.ChapterDTO;
import edu.sustech.api.entity.dto.CoursePageQueryDTO;
import edu.sustech.api.entity.dto.StudentDTO;
import edu.sustech.common.result.PageResult;
import edu.sustech.course.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.api.entity.dto.UserCourseInfoDTO;
import edu.sustech.course.entity.dto.CourseDTO;
import edu.sustech.course.entity.dto.CourseDetailDTO;
import edu.sustech.course.entity.dto.CourseStatusDTO;

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
     *
     * @return 随机推荐课程
     */
    List<Map<String, Object>> getRandomRecommendCourses();

    /**
     * 根据用户id查询该用户的所有课程信息
     *
     * @param id 用户id
     * @return 该用户的所有课程信息
     */
    UserCourseInfoDTO getUserCoursesInfoByUserId(Long id);

    /**
     * 累加获取更多课程
     *
     * @param courseIds 已经获取的课程id列表
     * @return 返回十门新课程，以及其id列表，并标注是否有更多课程可以获取
     */
    Map<String, Object> getCumulativeCourses(List<Long> courseIds);


    /**
     * 获取课程目录, 如果课程已经发布且用户已经登录，则还会获取用户每一个小节是否学习
     *
     * @param courseId 课程id
     * @return 课程目录(包括小节 : title id isLearned isPublic)
     */
    List<ChapterDTO> getCatalog(Long courseId);

    /**
     * 获取课程信息
     *
     * @param courseId 课程id
     * @return 课程信息
     */
    Course getCourseById(Long courseId);

    /**
     * 新增课程(添加课程基本信息)
     *
     * @param courseDTO 课程信息
     * @return 课程id
     */
    Map<String, Long> addCourse(CourseDTO courseDTO);

    /**
     * 新增课程详细信息
     *
     * @param courseDetailDTO 课程详细信息
     */
    void addCourseDetail(CourseDetailDTO courseDetailDTO);

    /**
     * 根据条件获取课程
     *
     * @param coursePageQueryDTO 查询条件
     * @return 课程列表
     */
    PageResult<Map<String, Object>> getCoursesByCondition(CoursePageQueryDTO coursePageQueryDTO);

    /**
     * 更新课程状态
     *
     * @param courseStatusDTO 课程状态信息
     */
    void updateCourseStatusByTeacher(CourseStatusDTO courseStatusDTO);

    /**
     * 更新课程状态(监听到管理端审核消息以及在教师更新课程状态时调用)
     *
     * @param courseStatusDTO 课程状态信息
     */
    void updateCourseStatus(CourseStatusDTO courseStatusDTO);

    /**
     * 获取课程学生 (根据加入状态)
     *
     * @param courseId 课程id
     * @return 课程学生
     */
    List<StudentDTO> getCourseStudentList(Long courseId, Integer joinState);
}
