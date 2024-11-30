package edu.sustech.common.constant;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-28 1:29
 */
public class MailNotificationConstant {
    public static final String COURSE_STATUS_MAIL_SUBJECT = "课程审核状态更新通知";

    public static final String COURSE_PASSED_MAIL_TEMPLATE = """
            <h2>尊敬的老师，</h2>
            <p>您好！</p>
            <p>我们很高兴地通知您，您提交的课程《%s》已经通过审核。</p>
            <p>现在，您的课程已经发布到网站上，供学生们进行学习，您也可以前往课程管理页面主动添加部分学生加入课程。</p>
            <p>如果您有任何问题，或者需要进一步的帮助，请随时联系我们。</p>
            <p>感谢您的支持与合作！</p>
            <br>
            <p>祝您工作愉快！</p>
            """;

    public static final String COURSE_REJECTED_MAIL_TEMPLATE = """
            <h2>尊敬的老师，</h2>
            <p>您好！</p>
            <p>很抱歉地通知您，您提交的课程《%s》未通过审核。</p>
            <p>具体原因如下：</p>
            <p><strong>%s</strong></p>
            <p>您可以根据反馈进行修改后再次提交审核。</p>
            <p>如果您有任何问题，或者需要进一步的帮助，请随时联系我们。</p>
            <p>感谢您的支持与合作！</p>
            <br>
            <p>祝您工作愉快！</p>
            """;

    public static final String COURSE_DELETE_MAIL_TEMPLATE = """
            <h2>尊敬的老师，</h2>
            <p>您好！</p>
            <p>我们通知您，您提交的课程《%s》已被删除。</p>
            <p>删除原因如下：</p>
            <p><strong>%s</strong></p>
            <p>如您有任何问题或需要进一步了解情况，请联系我们的课程管理团队，我们将尽力协助您。</p>
            <p>感谢您的理解与支持！</p>
            <br>
            <p>祝您工作愉快！</p>
            """;


    public static final String COURSE_JOIN_STATUS_MAIL_SUBJECT = "课程加入状态更新通知";

    public static final String COURSE_JOIN_PASSED_MAIL_TEMPLATE = """
            <h2>同学你好！</h2>
            <p>我们很高兴地通知你，你加入课程《%s》的申请已经被课程老师通过。</p>
            <p>现在，你可以前往课程页面查看课程内容，与老师和同学们一起学习。</p>
            <p>如果你有任何问题，或者需要进一步的帮助，请随时联系我们。</p>
            <p>感谢你的支持与合作！</p>
            <br>
            <p>祝你学习愉快！</p>
            """;

    public static final String COURSE_JOIN_REJECTED_MAIL_TEMPLATE = """
            <h2>同学你好！</h2>
            <p>很抱歉地通知你，你未能成功加入课程《%s》。</p>
            <p>具体原因如下：</p>
            <p><strong>%s</strong></p>
            <p>你可以根据反馈进行修改后再次申请加入。</p>
            <p>如果你有任何问题，或者需要进一步的帮助，请随时联系我们。</p>
            <p>感谢你的支持与合作！</p>
            <br>
            <p>祝你学习愉快！</p>
            """;

    public static final String COURSE_JOIN_INVITE_MAIL_TEMPLATE = """
            <h2>同学你好！</h2>
            <p>《%s》的老师已经将你加入该课程。</p>
            <p>现在，你可以前往课程页面查看课程内容，与老师和同学们一起学习。</p>
            <p>如果你有任何问题，或者需要进一步的帮助，请随时联系我们。</p>
            <p>感谢你的支持与合作！</p>
            <br>
            <p>祝你学习愉快！</p>
            """;
}
