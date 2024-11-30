package edu.sustech.message.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    // 简化方法：发送简单文本邮件
    public void sendSimpleMail(String to, String personal, String subject, String content) {
        sendSimpleMail(List.of(to), personal, subject, content);
    }

    // 简化方法：发送带多个收件人的简单文本邮件
    public void sendSimpleMail(List<String> to, String personal, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(getFormattedFromAddress(personal));
        message.setTo(to.toArray(new String[0]));
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    // 简化方法：发送HTML邮件
    public void sendHtmlMail(String to, String personal, String subject, String content) {
        sendHtmlMail(List.of(to), personal, subject, content);
    }

    // 简化方法：发送带多个收件人的HTML邮件
    public void sendHtmlMail(List<String> to, String personal, String subject, String content) {
        sendMail(to, personal, subject, content, true, null, null, null);
    }

    // 完整方法：包含附件、抄送、密送等参数
    public void sendMail(List<String> to, String personal, String subject, String content, boolean isHtml,
                         List<String> cc, List<String> bcc, List<MultipartFile> attachments) {
        // 判断附件是否为空并且邮件是否为 HTML 格式
        if ((attachments == null || attachments.isEmpty()) && !isHtml) {
            // 如果没有附件且不是 HTML 格式，但有 cc 或 bcc，则依然进入 sendMimeMail 逻辑
            if ((cc != null && !cc.isEmpty()) || (bcc != null && !bcc.isEmpty())) {
                sendMimeMail(to, personal, subject, content, false, cc, bcc, attachments);
            } else {
                sendSimpleMail(to, personal, subject, content);
            }
        } else {
            sendMimeMail(to, personal, subject, content, isHtml, cc, bcc, attachments);
        }
    }

    // 私有方法：用于发送复杂邮件（包含HTML内容或附件）
    private void sendMimeMail(List<String> to, String personal, String subject, String content, boolean isHtml,
                              List<String> cc, List<String> bcc, List<MultipartFile> attachments) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(new InternetAddress(getFormattedFromAddress(personal)));
            helper.setTo(to.toArray(new String[0]));
            if (cc != null && !cc.isEmpty()) {
                helper.setCc(cc.toArray(new String[0]));
            }
            if (bcc != null && !bcc.isEmpty()) {
                helper.setBcc(bcc.toArray(new String[0]));
            }
            helper.setSubject(subject);
            helper.setText(content, isHtml);

            if (attachments != null && !attachments.isEmpty()) {
                for (MultipartFile file : attachments) {
                    helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
                }
            }
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }

        mailSender.send(message);
    }

    // 格式化发送人地址（带自定义昵称）
    private String getFormattedFromAddress(String personal) {
        try {
            return MimeUtility.encodeText(personal) + " <" + from + ">";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to encode sender's name", e);
        }
    }
}
