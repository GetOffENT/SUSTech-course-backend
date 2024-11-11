package edu.sustech.message.service;

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
     * @param email 邮箱
     */
    void sendCaptcha(String email, String type);
}
