package edu.sustech.admin.service.impl;

import edu.sustech.admin.entity.dto.CourseStatusDTO;
import edu.sustech.admin.service.CourseReviewService;
import edu.sustech.api.client.CourseClient;
import edu.sustech.api.client.ResourceClient;
import edu.sustech.api.entity.dto.ChapterDTO;
import edu.sustech.api.entity.dto.CoursePageQueryDTO;
import edu.sustech.common.enums.ResultCode;
import edu.sustech.common.exception.CourseException;
import edu.sustech.common.exception.ResourceOperationException;
import edu.sustech.common.result.PageResult;
import edu.sustech.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-27 11:28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseReviewServiceImpl implements CourseReviewService {

    private final CourseClient courseClient;

    private final ResourceClient resourceClient;

    private final RabbitTemplate rabbitTemplate;

    /**
     * 分页动态条件获取课程列表
     *
     * @param coursePageQueryDTO 课程分页查询DTO
     * @return 课程列表
     */
    @Override
    public PageResult<Map<String, Object>> getCourseList(CoursePageQueryDTO coursePageQueryDTO) {
        Result<PageResult<Map<String, Object>>> coursesByCondition = courseClient.getCoursesByCondition(coursePageQueryDTO);
        if (!Objects.equals(coursesByCondition.getCode(), ResultCode.SUCCESS.code())) {
            throw new CourseException(coursesByCondition.getMessage());
        }
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
        if (!Objects.equals(catalog.getCode(), ResultCode.SUCCESS.code())) {
            throw new CourseException(catalog.getMessage());
        }
        return catalog.getData();
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
        if (!Objects.equals(videoUrl.getCode(), ResultCode.SUCCESS.code())) {
            throw new ResourceOperationException(videoUrl.getMessage());
        }
        return videoUrl.getData();
    }

    /**
     * 审核课程
     *
     * @param courseStatusDTO 课程状态信息
     */
    @Override
    public void reviewCourse(CourseStatusDTO courseStatusDTO) {
        // 发送消息到课程服务更新课程状态
        try {
            log.info("发送消息到课程服务更新课程状态 courseStatusDTO:{}", courseStatusDTO);
            rabbitTemplate.convertAndSend("course.status.direct", "course.status.update", courseStatusDTO);
        } catch (Exception e) {
            log.error("审核失败", e);
            throw new RuntimeException("审核失败");
        }

        // 发送消息到通知服务通知用户
        try {
            log.info("发送消息到通知服务通知用户 courseStatusDTO:{}", courseStatusDTO);
            rabbitTemplate.convertAndSend("course.status.direct", "course.status.notify", courseStatusDTO);
        } catch (Exception e) {
            log.error("审核失败", e);
            throw new RuntimeException("审核失败");
        }

    }
}
