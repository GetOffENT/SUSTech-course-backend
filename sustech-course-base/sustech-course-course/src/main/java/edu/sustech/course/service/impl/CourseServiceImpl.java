package edu.sustech.course.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.common.result.Result;
import edu.sustech.common.result.ResultCode;
import edu.sustech.course.entity.Course;
import edu.sustech.api.entity.dto.UserCourseInfoDTO;
import edu.sustech.course.mapper.CourseMapper;
import edu.sustech.course.service.CategoryService;
import edu.sustech.course.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
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
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    private final UserClient userClient;

    private final CategoryService categoryService;

    /**
     * 获取随机推荐课程
     *
     * @return 随机推荐课程
     */
    @Override
    public List<Map<String, Object>> getRandomRecommendCourses() {
        // TODO: 查询redis中有无缓存的已发布状态的视频id数据

        // 先从数据库查询所有已发布状态的视频id数据
        Set<Integer> ids = baseMapper.selectIds();

        if (CollUtil.isNotEmpty(ids)) {
            List<Map<String, Object>> videoList = page(ids, 1, 11);
            // copy
            List<Map<String, Object>> videoListCopy = new ArrayList<>(videoList);
            // 使用hutool工具把videoList随机排序
            Collections.shuffle(videoListCopy);
            return videoListCopy;
        }
        return List.of();
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
     * 分页查询视频信息
     *
     * @param ids      视频id集合
     * @param page     页码
     * @param pageSize 每页数量
     * @return 视频信息
     */
    public List<Map<String, Object>> page(Set<Integer> ids, Integer page, Integer pageSize) {
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

        List<Integer> queryIds = new ArrayList<>(ids).subList(start, Math.min(end, ids.size()));
        List<Course> courseList = baseMapper.selectList(
                new LambdaQueryWrapper<Course>()
                        .in(Course::getId, queryIds)
                        .eq(Course::getStatus, 1)
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
                        Result<UserDTO> userAndCoursesById = userClient.getUserAndCoursesById(course.getUserId());
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
}
