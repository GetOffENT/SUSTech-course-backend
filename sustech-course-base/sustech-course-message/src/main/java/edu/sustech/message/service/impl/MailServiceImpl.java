package edu.sustech.message.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.api.entity.enums.CourseStatus;
import edu.sustech.common.constant.CaptchaConstant;
import edu.sustech.common.constant.MailNotificationConstant;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.enums.JoinEnum;
import edu.sustech.common.exception.CaptchaException;
import edu.sustech.common.exception.CourseException;
import edu.sustech.common.util.UserContext;
import edu.sustech.message.entity.dto.BulkEmailDTO;
import edu.sustech.message.entity.dto.CourseJoinStatusDTO;
import edu.sustech.message.entity.dto.CourseStatusDTO;
import edu.sustech.message.service.MailService;
import edu.sustech.message.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-06 22:36
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final EmailUtil emailUtil;

    private final UserClient userClient;

    /**
     * 发送验证码
     *
     * @param email 邮箱
     */
    @Override
    public void sendCaptcha(String email, String type) {
        String captcha = RandomUtil.randomNumbers(6);
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        String captchaKey = CaptchaConstant.REGISTER_EMAIL_Captcha + email;
        String sendCountKey = CaptchaConstant.REGISTER_EMAIL_SEND_COUNT + email;  // 用于存储发送次数的 Redis key

        // 检查当天是否超过发送次数限制
        Integer sendCount = (Integer) redisTemplate.opsForValue().get(sendCountKey);
        if (sendCount != null && sendCount >= CaptchaConstant.MAX_SEND_COUNT_PER_DAY) {
            throw new CaptchaException(
                    MessageConstant.CAPTCHA_MAX_SEND_LIMIT_EXCEEDED.formatted(CaptchaConstant.MAX_SEND_COUNT_PER_DAY)
            );
        }

        // 检查重发时间限制
        Long remainingTime = redisTemplate.getExpire(captchaKey, TimeUnit.SECONDS);
        if (remainingTime != null) {
            // 计算已经过去的时间
            long elapsedTime = CaptchaConstant.CAPTCHA_EXPIRE_TIME - remainingTime;

            if (elapsedTime < CaptchaConstant.MIN_RETRY_TIME) {
                throw new CaptchaException(
                        MessageConstant.CAPTCHA_RETRY_TOO_SOON.formatted(CaptchaConstant.MIN_RETRY_TIME - elapsedTime)
                );
            }
        }

        // 如果没有验证码，或者已经过期，则重新生成验证码
        ops.set(captchaKey, captcha, CaptchaConstant.CAPTCHA_EXPIRE_TIME, TimeUnit.SECONDS);

        // 更新发送次数
        if (sendCount == null) {
            ops.set(sendCountKey, 1, 1, TimeUnit.DAYS);
        } else {
            ops.increment(sendCountKey, 1);  // 增加发送次数
        }

        // 发送邮件
        if (CaptchaConstant.REGISTER_EMAIL.equals(type)) {
            String content = CaptchaConstant.REGISTER_CONTENT_TEMPLATE.formatted(captcha);
            emailUtil.sendSimpleMail(email, CaptchaConstant.DEFAULT_SENDER, CaptchaConstant.REGISTER_EMAIL_SUBJECT, content);
        } else if (CaptchaConstant.FOUND_EMAIL.equals(type)) {
            String content = CaptchaConstant.FOUND_CONTENT_TEMPLATE.formatted(captcha);
            emailUtil.sendSimpleMail(email, CaptchaConstant.DEFAULT_SENDER, CaptchaConstant.FOUND_EMAIL_SUBJECT, content);
        } else {
            throw new CaptchaException(MessageConstant.CAPTCHA_TYPE_NOT_SUPPORTED);
        }
        log.info("已发送到邮箱：{}, 验证码：{}", email, captcha);
    }

    /**
     * 发送课程状态邮件
     *
     * @param courseStatusDTO 课程状态信息
     */
    @Override
    public void sendCourseStatusMail(CourseStatusDTO courseStatusDTO) {
        if (courseStatusDTO.getStatus() == CourseStatus.PASSED) {
            String content = MailNotificationConstant.COURSE_PASSED_MAIL_TEMPLATE.formatted(courseStatusDTO.getTitle());
            emailUtil.sendHtmlMail(
                    courseStatusDTO.getEmail(),
                    CaptchaConstant.DEFAULT_SENDER,
                    MailNotificationConstant.COURSE_STATUS_MAIL_SUBJECT,
                    content
            );
        } else if (courseStatusDTO.getStatus() == CourseStatus.NOT_PASSED) {
            String content = MailNotificationConstant.COURSE_REJECTED_MAIL_TEMPLATE.formatted(courseStatusDTO.getTitle(), courseStatusDTO.getReason());
            emailUtil.sendHtmlMail(
                    courseStatusDTO.getEmail(),
                    CaptchaConstant.DEFAULT_SENDER,
                    MailNotificationConstant.COURSE_STATUS_MAIL_SUBJECT,
                    content
            );
        } else if (courseStatusDTO.getStatus() == CourseStatus.DELETED) {
            String content = MailNotificationConstant.COURSE_DELETE_MAIL_TEMPLATE.formatted(courseStatusDTO.getTitle(), courseStatusDTO.getReason());
            emailUtil.sendHtmlMail(
                    courseStatusDTO.getEmail(),
                    CaptchaConstant.DEFAULT_SENDER,
                    MailNotificationConstant.COURSE_STATUS_MAIL_SUBJECT,
                    content
            );
        }
    }

    /**
     * 群发邮件
     *
     * @param bulkEmailDTO 群发邮件信息
     */
    @Override
    public void sendBulkEmails(BulkEmailDTO bulkEmailDTO) {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CourseException(MessageConstant.NOT_LOGIN);
        }
        UserDTO user = userClient.getUserById(userId).getData();
        if (user.getRole() != 2) {
            throw new CourseException(MessageConstant.NOT_TEACHER);
        }

        if (CollUtil.isEmpty(bulkEmailDTO.getTo())) {
            throw new CourseException(MessageConstant.EMAIL_TO_EMPTY);
        }

        if (bulkEmailDTO.getSender() == null) {
            throw new CourseException(MessageConstant.SENDER_EMPTY);
        }

        emailUtil.sendMail(
                bulkEmailDTO.getTo(),
                bulkEmailDTO.getSender(),
                bulkEmailDTO.getSubject(),
                bulkEmailDTO.getContent(),
                true,
                bulkEmailDTO.getCc(),
                bulkEmailDTO.getBcc(),
                bulkEmailDTO.getAttachments()
        );
    }

    /**
     * 发送课程加入状态邮件
     *
     * @param courseJoinStatusDTO 课程加入状态信息
     */
    @Override
    public void sendCourseJoinStatusMail(CourseJoinStatusDTO courseJoinStatusDTO) {
        if (courseJoinStatusDTO.getStatus() == JoinEnum.JOINED) {
            String content;
            if (courseJoinStatusDTO.getInviteOrApply() == 0) {
                content = MailNotificationConstant.COURSE_JOIN_INVITE_MAIL_TEMPLATE.formatted(courseJoinStatusDTO.getTitle());

            } else {
                content = MailNotificationConstant.COURSE_JOIN_PASSED_MAIL_TEMPLATE.formatted(courseJoinStatusDTO.getTitle());
            }
            emailUtil.sendHtmlMail(
                    courseJoinStatusDTO.getEmail(),
                    CaptchaConstant.DEFAULT_SENDER,
                    MailNotificationConstant.COURSE_JOIN_STATUS_MAIL_SUBJECT,
                    content
            );
        } else if (courseJoinStatusDTO.getStatus() == JoinEnum.REJECTED) {
            String content = MailNotificationConstant.COURSE_JOIN_REJECTED_MAIL_TEMPLATE.formatted(courseJoinStatusDTO.getTitle(), courseJoinStatusDTO.getReason());
            emailUtil.sendHtmlMail(
                    courseJoinStatusDTO.getEmail(),
                    CaptchaConstant.DEFAULT_SENDER,
                    MailNotificationConstant.COURSE_JOIN_STATUS_MAIL_SUBJECT,
                    content
            );
        }
    }


}
