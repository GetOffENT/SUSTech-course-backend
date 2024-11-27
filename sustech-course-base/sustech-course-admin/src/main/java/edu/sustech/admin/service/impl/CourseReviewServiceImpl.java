package edu.sustech.admin.service.impl;

import edu.sustech.admin.service.CourseReviewService;
import edu.sustech.api.client.CourseClient;
import edu.sustech.api.client.ResourceClient;
import edu.sustech.api.entity.dto.ChapterDTO;
import edu.sustech.api.entity.dto.CoursePageQueryDTO;
import edu.sustech.common.result.PageResult;
import edu.sustech.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-27 11:28
 */
@Service
@RequiredArgsConstructor
public class CourseReviewServiceImpl implements CourseReviewService {

    private final CourseClient courseClient;

    private final ResourceClient resourceClient;

    /**
     * 分页动态条件获取课程列表
     *
     * @param coursePageQueryDTO 课程分页查询DTO
     * @return 课程列表
     */
    @Override
    public PageResult<Map<String, Object>> getCourseList(CoursePageQueryDTO coursePageQueryDTO) {
        Result<PageResult<Map<String, Object>>> coursesByCondition = courseClient.getCoursesByCondition(coursePageQueryDTO);
        return coursesByCondition.getData();
    }

    /**
     * 获取课程目录
     *
     * @param courseId 课程id
     * @return 课程目录
     */
    @Override
    public List<ChapterDTO> getCatalog(Long courseId) {
        Result<List<ChapterDTO>> catalog = courseClient.getCatalog(courseId);
        if (catalog.getCode() == 20000) {
            return catalog.getData();
        }
        return null;
    }

    /**
     * 获取视频播放地址
     *
     * @param videoSourceId 课程id
     * @return 视频播放地址
     */
    @Override
    public String getPlayInfo(String videoSourceId) {
        Result<String> videoUrl = resourceClient.getPlayInfo(videoSourceId);
        if (videoUrl.getCode() == 20000) {
            return videoUrl.getData();
        }
        return null;
    }
}
