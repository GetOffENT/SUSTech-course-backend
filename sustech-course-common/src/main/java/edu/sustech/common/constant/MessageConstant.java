package edu.sustech.common.constant;

/**
 * <p>
 * 信息提示常量类信息提示常量类
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-10-05 14:56
 */
public class MessageConstant {
    public static final String ACCOUNT_ALREADY_EXIST = "该邮箱已注册";
    public static final String RANDOM_NICKNAME_PREFIX = "SUSTecher";
    public static final String LOGIN_ERROR = "邮箱未注册或密码错误";
    public static final String ACCOUNT_NOT_FOUND = "该邮箱未注册";
    public static final String ACCOUNT_LOCKED = "账号被锁定或不存在";

    public static final String CAPTCHA_ALREADY_SEND = "验证码已发送，还需等待：%d秒";
    public static final String CAPTCHA_RETRY_TOO_SOON = "%d秒后可重新发送";
    public static final String CAPTCHA_MAX_SEND_LIMIT_EXCEEDED = "当天验证码发送次数超过限制：%d次";
    public static final String CAPTCHA_TYPE_NOT_SUPPORTED = "不支持的获取验证码类型";
    public static final String CAPTCHA_ERROR = "验证码错误";
    public static final String CAPTCHA_NOT_SEND_OR_EXPIRED = "验证码未发送或已过期";
    public static final String CAPTCHA_VERIFICATION_FAILED = "验证码校验失败，请重新获取";

    public static final String EMAIL_TO_EMPTY = "收件人邮箱不能为空";
    public static final String SENDER_EMPTY = "发件人不能为空";

    public static final String NOT_LOGIN = "用户未登录";
    public static final String COMMENT_NOT_EXIST = "评论不存在";
    public static final String DANMU_NOT_EXIST = "弹幕不存在";
    public static final String COMMENT_NO_PERMISSION = "无权限删除该评论";
    public static final String DANMU_NO_PERMISSION = "无权限删除该弹幕";
    public static final String NO_PERMISSION_TO_ADD_COURSE_REVIEW = "无权添加课程评价";
    public static final String COMMENT_DELETE_ERROR = "删除评论失败";
    public static final String DANMU_DELETE_ERROR = "删除弹幕失败";
    public static final String ALREADY_JOINED = "已加入该课程";
    public static final String COURSE_NOT_EXIST = "课程不存在";

    public static final String VIDEO_NOT_EXIST = "视频不存在";
    public static final String VIDEO_NOT_PUBLIC = "视频未公开";
    public static final String VIDEO_DELETE_FAILED = "删除视频失败";
    public static final String PARAM_ERROR = "评分不能为空";
    public static final String NOT_TEACHER = "您不是老师,无法发布课程";
    public static final String NO_PERMISSION = "无权限操作";
    public static final String COURSE_ADD_FAILED = "课程添加失败，请稍后再试！";
    public static final String CHAPTER_ADD_FAILED = "章节添加失败，请稍后再试！";
    public static final String CHAPTER_NOT_FOUND = "章节不存在";
    public static final String CHAPTER_DELETE_FAILED = "删除章节失败";
    public static final String VIDEO_ADD_FAILED = "小节添加失败，请稍后再试！";
    public static final String ATTACHMENT_ADD_FAILED = "附件添加失败，请稍后再试！";
    public static final String ATTACHMENT_DELETE_FAILED = "附件删除失败，请稍后再试！";
    public static final String ATTACHMENT_NOT_EXIST = "附件不存在";
    public static final String ATTACHMENT_UPDATE_FAILED = "附件下载状态更新失败，请稍后再试！";
    public static final String UPLOAD_VIDEO_FAILED = "上传视频失败，请稍后再试！";
    public static final String ADD_VIDEO_RESOURCE_FAILED = "添加视频资源失败，请稍后再试！";
    public static final String GET_VIDEO_URL_FAILED = "获取视频地址失败，请稍后再试！";
    public static final String ERROR = "妮小可被玩坏了(¯﹃¯)";
    public static final String SERVER_ERROR = "妮小可出错了！请稍后再试(¯﹃¯)";

    public static final String INVALID_TIME_POINTS = "时间点列表必须为偶数长度！";
}
