package edu.sustech.course.listener;

import edu.sustech.course.entity.dto.CourseStatusDTO;
import edu.sustech.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-27 22:48
 */
@Component
@RequiredArgsConstructor
public class CourseStatusListener {

    private final CourseService courseService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "course.status.queue", durable = "true"),
            exchange = @Exchange(name = "course.status.direct"),
            key = "course.status"
    ))
    public void listenCourseStatus(CourseStatusDTO courseStatusDTO) {
        System.out.println("courseStatusDTO = " + courseStatusDTO);
        courseService.updateCourseStatus(courseStatusDTO);
    }
}
