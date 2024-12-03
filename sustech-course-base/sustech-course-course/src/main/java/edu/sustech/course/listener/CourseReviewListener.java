package edu.sustech.course.listener;

import edu.sustech.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * 课程评价监听器
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-12-03 17:05
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CourseReviewListener {

    private final CourseService courseService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "course.review.count.queue", durable = "true"),
            exchange = @Exchange(name = "course.review.direct"),
            key = "course.review.count"
    ))
    public void listenCourseReviewCount(Map<String, Object> map) {
        Long courseId = Long.valueOf((map.get("courseId").toString()));
        Integer count = (Integer) map.get("count");
        log.info("收到课程评价数量更新消息: courseId={}, count={}", courseId, count);
        courseService.updateCourseReviewCount(courseId, count);
    }

}
