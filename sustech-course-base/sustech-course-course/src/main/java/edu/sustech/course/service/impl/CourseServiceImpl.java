package edu.sustech.course.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.*;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CourseException;
import edu.sustech.common.result.PageResult;
import edu.sustech.common.result.Result;
import edu.sustech.common.result.ResultCode;
import edu.sustech.common.util.UserContext;
import edu.sustech.course.entity.*;
import edu.sustech.course.entity.dto.*;
import edu.sustech.api.entity.enums.CourseOpenStatus;
import edu.sustech.api.entity.enums.CourseStatus;
import edu.sustech.course.entity.enums.JoinEnum;
import edu.sustech.course.mapper.*;
import edu.sustech.course.service.CategoryService;
import edu.sustech.course.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final CourseDescriptionMapper courseDescriptionMapper;
    private final AttachmentMapper attachmentMapper;

    /**
     * 获取随机推荐课程
     *
     * @return 随机推荐课程
     */
    @Override
    public List<Map<String, Object>> getRandomRecommendCourses() {
        // 先从数据库查询所有已发布状态并 非不公开的课程id数据
        List<Long> ids = baseMapper.selectCourseIds(
                List.of(CourseStatus.PASSED.getCode()),
                List.of(CourseOpenStatus.FULL_OPEN.getValue(), CourseOpenStatus.PART_OPEN.getValue())
        );

        if (CollUtil.isEmpty(ids)) {
            return List.of();
        }

        CoursePageQueryDTO coursePageQueryDTO = CoursePageQueryDTO.builder()
                .page(1)
                .pageSize(11)
                .build();
        List<Map<String, Object>> courseList = page(ids, coursePageQueryDTO);
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
        // 先从数据库查询所有已发布状态并 非不公开的课程id数据
        List<Long> ids = baseMapper.selectCourseIds(
                List.of(CourseStatus.PASSED.getCode()),
                List.of(CourseOpenStatus.FULL_OPEN.getValue(), CourseOpenStatus.PART_OPEN.getValue())
        );

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

        CoursePageQueryDTO coursePageQueryDTO = CoursePageQueryDTO.builder()
                .page(1)
                .pageSize(10)
                .build();
        List<Map<String, Object>> courseList = page(randomIds, coursePageQueryDTO);
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
     * 获取课程目录, 如果课程已经发布且用户已经登录，则还会获取用户每一个小节是否学习
     *
     * @param courseId 课程id
     * @return 课程目录(包括小节 : title id isLearned isPublic)
     */
    @Override
    public List<ChapterDTO> getCatalog(Long courseId) {
        Course course = baseMapper.selectById(courseId);
        if (course == null) {
            throw new CourseException(MessageConstant.COURSE_NOT_EXIST);
        }

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
        List<VideoDTO> videoDTOS = BeanUtil.copyToList(videos, VideoDTO.class);

        // 如果用户登录了，则查询是否已经学习
        Long user = UserContext.getUser();
        if (user != null && course.getStatus() == CourseStatus.PASSED) {
            log.info("课程已经发布且用户已登录，查询用户的视频学习记录");

            // 查询用户的视频学习记录
            List<UserVideoRecord> userVideoRecords = userVideoRecordMapper.selectList(
                    new LambdaQueryWrapper<UserVideoRecord>()
                            .eq(UserVideoRecord::getUserId, user)
                            .eq(UserVideoRecord::getCourseId, courseId)
            );

            for (VideoDTO videoDTO : videoDTOS) {
                for (UserVideoRecord userVideoRecord : userVideoRecords) {
                    if (videoDTO.getId().equals(userVideoRecord.getVideoId())) {
                        videoDTO.setIsLearned(userVideoRecord.getIsLearned());
                        break;
                    }
                }
            }
        }

        if (CollUtil.isEmpty(chapters)) {
            return List.of();
        }

        List<ChapterDTO> chapterDTOList = new ArrayList<>();
        for (Chapter chapter : chapters) {
            ChapterDTO chapterDTO = BeanUtil.copyProperties(chapter, ChapterDTO.class);
            List<VideoDTO> videoDTOList = new ArrayList<>();
            for (VideoDTO video : videoDTOS) {
                if (video.getChapterId().equals(chapter.getId())) {
                    videoDTOList.add(video);
                }
            }
            chapterDTO.setVideos(videoDTOList);
            chapterDTOList.add(chapterDTO);
        }
        return chapterDTOList;
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
     * 新增课程(添加课程基本信息)
     *
     * @param courseDTO 课程信息
     * @return 课程id
     */
    @Transactional
    @Override
    public Map<String, Long> addCourse(CourseDTO courseDTO) {
        Long userId = checkTeacher();

        Course course = BeanUtil.copyProperties(courseDTO, Course.class)
                .setUserId(userId)
                .setIsTeacher((byte) 1)
                .setTags(CollUtil.join(courseDTO.getTags(), "\n"))
                .setStatus(CourseStatus.EDITING);

        int insert = baseMapper.insert(course);
        if (insert == 0) {
            throw new CourseException(MessageConstant.COURSE_ADD_FAILED);
        }

        int count = courseDescriptionMapper.insert(
                CourseDescription.builder()
                        .id(course.getId())
                        .description(courseDTO.getCourseDescription())
                        .build()
        );
        if (count == 0) {
            throw new CourseException(MessageConstant.COURSE_ADD_FAILED);
        }

        return Map.of("courseId", course.getId());
    }

    /**
     * 新增课程章节
     *
     * @param chapterInfoDTO 章节信息
     * @return 章节id
     */
    @Override
    public Map<String, Long> addChapter(ChapterInfoDTO chapterInfoDTO) {
        checkTeacher();

        Chapter chapter = BeanUtil.copyProperties(chapterInfoDTO, Chapter.class);
        int insert = chapterMapper.insert(chapter);
        if (insert == 0) {
            throw new CourseException(MessageConstant.CHAPTER_ADD_FAILED);
        }
        return Map.of("chapterId", chapter.getId());
    }

    /**
     * 新增视频(小节)
     *
     * @param videoDTO 视频(小节)信息
     * @return 视频(小节)id
     */
    @Override
    public Map<String, Long> addVideo(edu.sustech.api.entity.dto.VideoDTO videoDTO) {
        Long userId = checkTeacher();

        Video video = BeanUtil.copyProperties(videoDTO, Video.class)
                .setUserId(userId);

        int insert = videoMapper.insert(video);
        if (insert == 0) {
            throw new CourseException(MessageConstant.VIDEO_ADD_FAILED);
        }
        return Map.of("videoId", video.getId());
    }

    /**
     * 新增课程详细信息
     *
     * @param courseDetailDTO 课程详细信息
     */
    @Transactional
    @Override
    public void addCourseDetail(CourseDetailDTO courseDetailDTO) {
        checkTeacher();

        Course course = BeanUtil.copyProperties(courseDetailDTO, Course.class)
                .setStatus(CourseStatus.PENDING);
        int update = baseMapper.updateById(course);
        if (update == 0) {
            throw new CourseException(MessageConstant.COURSE_ADD_FAILED);
        }

        courseDescriptionMapper.updateById(
                CourseDescription.builder()
                        .id(course.getId())
                        .description(courseDetailDTO.getCourseDescription())
                        .build()
        );

        List<ChapterDetailDTO> chapterDetailDTOList = courseDetailDTO.getChapters();
        for (ChapterDetailDTO chapterDetailDTO : chapterDetailDTOList) {
            Chapter chapter = BeanUtil.copyProperties(chapterDetailDTO, Chapter.class);
            chapterMapper.updateById(chapter);

            List<VideoDetailDTO> videoDTOList = chapterDetailDTO.getVideos();
            for (VideoDetailDTO videoDetailDTO : videoDTOList) {
                Video video = BeanUtil.copyProperties(videoDetailDTO, Video.class);
                videoMapper.updateById(video);
            }
        }

        List<AttachmentDetailDTO> attachments = courseDetailDTO.getAttachments();
        BeanUtil.copyToList(attachments, Attachment.class)
                .forEach(attachmentMapper::updateById);
    }

    /**
     * 根据条件获取课程
     *
     * @param coursePageQueryDTO 课程动态查询条件
     * @return 课程信息
     */
    @Override
    public PageResult<Map<String, Object>> getCoursesByCondition(CoursePageQueryDTO coursePageQueryDTO) {
        Page<Course> coursePage = new Page<>(coursePageQueryDTO.getPage(), coursePageQueryDTO.getPageSize());

        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        if (coursePageQueryDTO.getMcId() != null) {
            queryWrapper.eq(Course::getMcId, coursePageQueryDTO.getMcId());
        }
        if (coursePageQueryDTO.getScId() != null) {
            queryWrapper.eq(Course::getScId, coursePageQueryDTO.getScId());
        }
        if (coursePageQueryDTO.getTitle() != null) {
            queryWrapper.like(Course::getTitle, coursePageQueryDTO.getTitle());
        }
        if (coursePageQueryDTO.getForm() != null) {
            queryWrapper.eq(Course::getForm, coursePageQueryDTO.getForm().getCode());
        }
        if (coursePageQueryDTO.getType() != null) {
            queryWrapper.eq(Course::getType, coursePageQueryDTO.getType());
        }
        if (coursePageQueryDTO.getAuth() != null) {
            queryWrapper.eq(Course::getAuth, coursePageQueryDTO.getAuth());
        }
        if (CollUtil.isNotEmpty(coursePageQueryDTO.getDuration())) {
            queryWrapper.between(Course::getDuration, coursePageQueryDTO.getDuration().get(0), coursePageQueryDTO.getDuration().get(1));
        }
        if (CollUtil.isNotEmpty(coursePageQueryDTO.getTags())) {
            queryWrapper.in(Course::getTags, coursePageQueryDTO.getTags());
        }
        if (!CollUtil.isEmpty(coursePageQueryDTO.getStatus())) {
            queryWrapper.in(Course::getStatus, coursePageQueryDTO.getStatus().stream().map(CourseStatus::getCode).toList());
        }
        if (!CollUtil.isEmpty(coursePageQueryDTO.getOpenState())) {
            queryWrapper.in(Course::getOpenState, coursePageQueryDTO.getOpenState().stream().map(CourseOpenStatus::getValue).toList());
        }

        baseMapper.selectPage(coursePage, queryWrapper);

        List<Course> records = coursePage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return new PageResult<>(0L, null);
        }

        return new PageResult<>(coursePage.getTotal(), getCourseDetail(records));
    }

    /**
     * 获取课程详情
     *
     * @param courseStatusDTO 课程状态信息
     */
    @Override
    public void updateCourseStatus(CourseStatusDTO courseStatusDTO) {
        baseMapper.updateById(BeanUtil.copyProperties(courseStatusDTO, Course.class));
        if (courseStatusDTO.getStatus() == CourseStatus.DELETED) {
            // 删除课程的章节信息
            chapterMapper.delete(new LambdaQueryWrapper<Chapter>()
                    .eq(Chapter::getCourseId, courseStatusDTO.getId())
            );
            // 删除课程的视频信息
            videoMapper.delete(new LambdaQueryWrapper<Video>()
                    .eq(Video::getCourseId, courseStatusDTO.getId())
            );
            // 删除课程的附件信息
            attachmentMapper.delete(new LambdaQueryWrapper<Attachment>()
                    .eq(Attachment::getCourseId, courseStatusDTO.getId())
            );
            // 删除课程的用户课程信息
            userCourseMapper.delete(new LambdaQueryWrapper<UserCourse>()
                    .eq(UserCourse::getCourseId, courseStatusDTO.getId())
            );
            // 删除课程的课程描述信息
            courseDescriptionMapper.deleteById(courseStatusDTO.getId());
            // 删除课程
            baseMapper.deleteById(courseStatusDTO.getId());
        }
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
     * 分页查询课程信息(伪分页)
     *
     * @param ids                课程id集合
     * @param coursePageQueryDTO 课程动态查询条件
     * @return 课程信息
     */
    public List<Map<String, Object>> page(List<Long> ids, CoursePageQueryDTO coursePageQueryDTO) {
        Integer page = coursePageQueryDTO.getPage();
        Integer pageSize = coursePageQueryDTO.getPageSize();
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

        return getCourseDetail(courseList);
    }

    public List<Map<String, Object>> getCourseDetail(List<Course> courseList) {
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

    private Long checkTeacher() {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CourseException(MessageConstant.NOT_LOGIN);
        }

        // TODO: redis缓存用户
        UserDTO user = userClient.getUserById(userId).getData();
        if (user.getRole() != 2) {
            throw new CourseException(MessageConstant.NOT_TEACHER);
        }
        return userId;
    }
}
