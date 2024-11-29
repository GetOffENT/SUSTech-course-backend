package edu.sustech.course.listener;

import edu.sustech.course.entity.dto.CourseStatusDTO;
import edu.sustech.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * <p>
 * 课程状态更新监听器
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-27 22:48
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CourseStatusListener {

    private final CourseService courseService;

    /**
     * 监听课程状态更新消息
     * @param courseStatusDTO 课程状态信息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "course.status.update.queue", durable = "true"),
            exchange = @Exchange(name = "course.status.direct"),
            key = "course.status.update"
    ))
    public void listenCourseStatus(CourseStatusDTO courseStatusDTO) {
        log.info("收到课程状态更新消息: {}", courseStatusDTO);
        courseService.updateCourseStatus(courseStatusDTO);
    }
}
