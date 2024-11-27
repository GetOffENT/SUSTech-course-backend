package edu.sustech.message.service;

import edu.sustech.message.entity.dto.CourseStatusDTO;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-06 22:35
 */
public interface MailService {

    /**
     * 发送验证码
     *
     * @param email 邮箱
     */
    void sendCaptcha(String email, String type);

    /**
     * 发送课程状态邮件
     *
     * @param courseStatusDTO 课程状态信息
     */
    void sendCourseStatusMail(CourseStatusDTO courseStatusDTO);
}
