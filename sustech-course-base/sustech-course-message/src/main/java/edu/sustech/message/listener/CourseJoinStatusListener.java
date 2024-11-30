package edu.sustech.message.listener;

import edu.sustech.message.entity.dto.CourseJoinStatusDTO;
import edu.sustech.message.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * @since 2024-11-30 22:05
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CourseJoinStatusListener {

    private final MailService mailService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "course.joinStatus.notify.queue", durable = "true"),
            exchange = @Exchange(name = "course.joinStatus.direct"),
            key = "course.joinStatus.notify"
    ))
    public void listenCourseStatus(CourseJoinStatusDTO courseJoinStatusDTO) {
        mailService.sendCourseJoinStatusMail(courseJoinStatusDTO);
    }

}
