package edu.sustech.message.service.impl;

import cn.hutool.core.util.RandomUtil;
import edu.sustech.common.constant.CaptchaConstant;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CaptchaException;
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
}
