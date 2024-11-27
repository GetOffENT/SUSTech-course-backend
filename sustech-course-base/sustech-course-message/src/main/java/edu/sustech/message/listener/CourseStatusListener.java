package edu.sustech.message.listener;

import edu.sustech.message.entity.dto.CourseStatusDTO;
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
 * @since 2024-11-28 1:17
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CourseStatusListener {

    private final MailService mailService;

    /**
     * 监听课程状态变更, 发送邮件通知
     *
     * @param courseStatusDTO 课程状态DTO
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "course.status.notify.queue", durable = "true"),
            exchange = @Exchange(name = "course.status.direct"),
            key = "course.status.notify"
    ))
    public void listenCourseStatus(CourseStatusDTO courseStatusDTO) {
        log.info("监听到课程状态变更，发送邮件通知: {}", courseStatusDTO);
        mailService.sendCourseStatusMail(courseStatusDTO);
    }
}
