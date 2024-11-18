package edu.sustech.course.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CourseException;
import edu.sustech.common.result.Result;
import edu.sustech.common.result.ResultCode;
import edu.sustech.common.util.UserContext;
import edu.sustech.course.entity.*;
import edu.sustech.api.entity.dto.UserCourseInfoDTO;
import edu.sustech.course.entity.enums.CourseOpenStatus;
import edu.sustech.course.entity.enums.CourseStatus;
import edu.sustech.course.entity.enums.JoinEnum;
import edu.sustech.course.entity.vo.ChapterVO;
import edu.sustech.course.entity.vo.VideoVO;
import edu.sustech.course.mapper.*;
import edu.sustech.course.service.CategoryService;
import edu.sustech.course.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-12
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    private final UserClient userClient;

    private final CategoryService categoryService;

    private final ChapterMapper chapterMapper;

    private final VideoMapper videoMapper;

    private final UserVideoRecordMapper userVideoRecordMapper;

    private final UserCourseMapper userCourseMapper;

    /**
     * 获取随机推荐课程
     *
     * @return 随机推荐课程
     */
    @Override
    public List<Map<String, Object>> getRandomRecommendCourses() {
        // TODO: 查询redis中有无缓存的已发布状态的课程id数据

        // 先从数据库查询所有已发布状态并 非不公开的课程id数据
        List<Long> ids = baseMapper.selectPublishedCourseIds();

        if (CollUtil.isEmpty(ids)) {
            return List.of();
        }

        List<Map<String, Object>> courseList = page(ids, 1, 11);
        // copy
        List<Map<String, Object>> courseListCopy = new ArrayList<>(courseList);
        // 使用hutool工具把courseList随机排序
        Collections.shuffle(courseListCopy);
        return courseListCopy;
    }

    /**
     * 累加获取更多课程
     *
     * @param courseIds 已经获取的课程id列表
     * @return 返回十门新课程，以及其id列表，并标注是否有更多课程可以获取
     */
    @Override
    public Map<String, Object> getCumulativeCourses(List<Long> courseIds) {
        // TODO: 查询redis中有无缓存的已发布状态的课程id数据

        // 先从数据库查询所有已发布状态并 非不公开的课程id数据
        List<Long> ids = baseMapper.selectPublishedCourseIds();

        if (CollUtil.isEmpty(ids)) {
            return Map.of(
                    "courses", List.of(),
                    "courseIds", List.of(),
                    "hasMore", false
            );
        }

        // 从ids中过滤掉已经获取的课程id
        ids.removeAll(courseIds);

        // 从ids中随机选取10条记录
        List<Long> randomIds = getRandomElements(ids, 10);
        List<Map<String, Object>> courseList = page(randomIds, 1, 10);
        // copy
        List<Map<String, Object>> courseListCopy = new ArrayList<>(courseList);
        // 使用hutool工具把courseList随机排序
        Collections.shuffle(courseListCopy);

        return Map.of(
                "courses", courseListCopy,
                "courseIds", randomIds,
                "hasMore", ids.size() - 10 > 0
        );
    }

    /**
     * 获取课程目录, 如果已经登录，则还会获取用户每一个小节是否学习
     *
     * @param courseId 课程id
     * @return 课程目录(包括小节 : title id isLearned isPublic)
     */
    @Override
    public List<ChapterVO> getCatalog(Long courseId) {

        // 查询课程的章节信息
        List<Chapter> chapters = chapterMapper.selectList(new LambdaQueryWrapper<Chapter>()
                .eq(Chapter::getCourseId, courseId)
                .orderByAsc(Chapter::getSort)
        );

        // 查询课程的视频(小节)信息
        List<Video> videos = videoMapper.selectList(new LambdaQueryWrapper<Video>()
                .eq(Video::getCourseId, courseId)
                .orderByAsc(Video::getSort)
        );
        List<VideoVO> videoVOs = BeanUtil.copyToList(videos, VideoVO.class);

        // 如果用户登录了，则查询是否已经学习
        Long user = UserContext.getUser();
        if (user != null) {
            log.info("用户已登录，查询用户的视频学习记录");
            // 查询用户的视频学习记录
            List<UserVideoRecord> userVideoRecords = userVideoRecordMapper.selectList(
                    new LambdaQueryWrapper<UserVideoRecord>()
                            .eq(UserVideoRecord::getUserId, user)
                            .eq(UserVideoRecord::getCourseId, courseId)
            );

            for (VideoVO videoVO : videoVOs) {
                for (UserVideoRecord userVideoRecord : userVideoRecords) {
                    if (videoVO.getId().equals(userVideoRecord.getVideoId())) {
                        videoVO.setIsLearned(userVideoRecord.getIsLearned());
                        break;
                    }
                }
            }
        }

        if (CollUtil.isEmpty(chapters)) {
            return List.of();
        }

        List<ChapterVO> chapterVOList = new ArrayList<>();
        for (Chapter chapter : chapters) {
            ChapterVO chapterVO = BeanUtil.copyProperties(chapter, ChapterVO.class);
            List<VideoVO> videoVOList = new ArrayList<>();
            for (VideoVO video : videoVOs) {
                if (video.getChapterId().equals(chapter.getId())) {
                    videoVOList.add(video);
                }
            }
            chapterVO.setVideos(videoVOList);
            chapterVOList.add(chapterVO);
        }
        return chapterVOList;
    }

    /**
     * 获取课程信息
     *
     * @param courseId 课程id
     * @return 课程信息
     */
    @Override
    public Course getCourseById(Long courseId) {
        Course course = baseMapper.selectById(courseId);
        if (course == null) {
            throw new CourseException(MessageConstant.COURSE_NOT_EXIST);
        }

        Long userId = UserContext.getUser();

        // 用户登录了，且用户是课程的发布者
        if (userId != null && userId.equals(course.getUserId())) {
            return course;
        }

        if (course.getStatus() != CourseStatus.PASSED) {
            throw new CourseException(MessageConstant.COURSE_NOT_EXIST);
        }

        if (course.getOpenState() == CourseOpenStatus.NOT_OPEN) {
            // 用户未登录
            if (userId == null) {
                throw new CourseException(MessageConstant.COURSE_NOT_EXIST);
            }

            // 已登录的用户是否加入了课程
            UserCourse userCourse = userCourseMapper.selectOne(
                    new LambdaQueryWrapper<UserCourse>()
                            .eq(UserCourse::getUserId, userId)
                            .eq(UserCourse::getCourseId, courseId)
            );
            // 用户加入了课程
            if (userCourse != null && userCourse.getJoinState() == JoinEnum.JOINED) {
                return course;
            }

            throw new CourseException(MessageConstant.COURSE_NOT_EXIST);
        }
        return course;
    }

    /**
     * 根据用户id查询该用户的所有课程信息
     *
     * @param id 用户id
     * @return 该用户的所有课程信息
     */
    @Override
    public UserCourseInfoDTO getUserCoursesInfoByUserId(Long id) {
        // TODO: 根据用户id查询该用户的所有课程信息
        return new UserCourseInfoDTO().setCourseCount(100000).setLike(456789).setPlay(9555);
    }


    /**
     * 分页查询课程信息
     *
     * @param ids      课程id集合
     * @param page     页码
     * @param pageSize 每页数量
     * @return 课程信息
     */
    public List<Map<String, Object>> page(List<Long> ids, Integer page, Integer pageSize) {
        if (page == null || page < 1) {
            page = 1;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }

        int start = (page - 1) * pageSize;
        int end = start + pageSize;
        if (start >= ids.size()) {
            return List.of();
        }

        List<Long> queryIds = new ArrayList<>(ids).subList(start, Math.min(end, ids.size()));
        List<Course> courseList = baseMapper.selectList(
                new LambdaQueryWrapper<Course>()
                        .in(Course::getId, queryIds)
        );

        if (CollUtil.isEmpty(courseList)) {
            return List.of();
        }

        return courseList.stream().parallel()
                .map(course -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("course", course);

                    ExecutorService dbExecutor = Executors.newCachedThreadPool();
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        // 查询用户信息
                        UserCourseInfoDTO userCoursesInfoByUserId = this.getUserCoursesInfoByUserId(course.getUserId());
                        Result<UserDTO> userAndCoursesById = userClient.getUserById(course.getUserId());
                        if (Objects.equals(userAndCoursesById.getCode(), ResultCode.SUCCESS.code())) {
                            UserDTO data = userAndCoursesById.getData();
                            data.setCourseCount(userCoursesInfoByUserId.getCourseCount())
                                    .setLike(userCoursesInfoByUserId.getLike())
                                    .setPlay(userCoursesInfoByUserId.getPlay());
                            map.put("user", data);
                        }
                        // map.put("user", userService.getUserById(course.getUid()));
                    }, dbExecutor);

                    CompletableFuture<Void> categoryFuture = CompletableFuture.runAsync(() -> {
                        // 查询分类信息
                        map.put("category", categoryService.getCategoryById(course.getMcId(), course.getScId()));
                    }, dbExecutor);

                    CompletableFuture<Void> allFutures = CompletableFuture.allOf(future, categoryFuture);
                    allFutures.thenRun(dbExecutor::shutdown);
                    allFutures.join();
                    return map;
                }).toList();
    }

    public <T> List<T> getRandomElements(List<T> list, int n) {
        Collections.shuffle(list);
        return new ArrayList<>(list.subList(0, Math.min(n, list.size())));
    }
}
