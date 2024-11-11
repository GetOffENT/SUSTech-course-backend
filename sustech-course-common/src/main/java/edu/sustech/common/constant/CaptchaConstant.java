package edu.sustech.common.constant;

/**
 * <p>
 * 验证码相关常量
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-10-07 21:20
 */
public class CaptchaConstant {

    public static final String REGISTER_EMAIL_Captcha = "register:emailCaptcha:";
    public static final String REGISTER_EMAIL_SEND_COUNT = "register:emailSendCount:";
    public static final Integer MAX_SEND_COUNT_PER_DAY = 3;
    public static final String REGISTER_CONTENT_TEMPLATE;
    public static final String FOUND_CONTENT_TEMPLATE;
    public static final String REGISTER_EMAIL_SUBJECT = "SUSTech-course邮箱注册验证";
    public static final String FOUND_EMAIL_SUBJECT = "SUSTech-course邮箱找回密码验证";
    public static final Long CAPTCHA_EXPIRE_TIME = 600L; // 单位秒
    public static final Long MIN_RETRY_TIME = 60L;  // 重发限制时间，单位秒
    public static final String DEFAULT_SENDER = "SUSTech-course";

    public static final String REGISTER_EMAIL = "register";
    public static final String FOUND_EMAIL = "found";


    static {
        REGISTER_CONTENT_TEMPLATE = "SUSTech-course平台注册验证码: %s, 有效期" + CAPTCHA_EXPIRE_TIME / 60 + "分钟";
        FOUND_CONTENT_TEMPLATE = "SUSTech-course平台找回密码验证码: %s, 有效期" + CAPTCHA_EXPIRE_TIME / 60 + "分钟";
    }
}
