package edu.sustech.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.common.result.Result;
import edu.sustech.common.result.ResultCode;
import edu.sustech.course.entity.Category;
import edu.sustech.course.entity.Course;
import edu.sustech.course.entity.Video;
import edu.sustech.course.mapper.CategoryMapper;
import edu.sustech.course.mapper.CourseMapper;
import edu.sustech.course.mapper.VideoMapper;
import edu.sustech.course.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    private final UserClient userClient;

    private final CategoryMapper categoryMapper;

    private final CourseMapper courseMapper;

    /**
     * 获取单个视频信息
     *
     * @param id 视频ID
     * @return 单个视频信息(包含发布用户信息)
     */
    @Override
    public Map<String, Object> getVideoWithDataById(Long id) {

        // TODO: Redis缓存优化

        Map<String, Object> map = new HashMap<>();

        Video video = baseMapper.selectById(id);
        map.put("video", video);

        // 发布者信息
        Result<UserDTO> userAndCoursesById = userClient.getUserAndCoursesById(video.getUserId());

        if (Objects.equals(userAndCoursesById.getCode(), ResultCode.SUCCESS.code())) {
            map.put("user", userAndCoursesById.getData());
        }

        // 查询视频分类信息
        // TODO: 还没决定做不做课程详情页面，如果做的话就不写分类信息了
        Course course = courseMapper.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getId, video.getCourseId()));
        map.put("course", course);
        map.put("category", categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getMcId, course.getMcId()).eq(Category::getScId, course.getScId())));
        return map;
    }
}
