package edu.sustech.message.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-30 20:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "群发邮件DTO")
public class BulkEmailDTO {

    @ApiModelProperty("邮件主题")
    private String subject;

    @ApiModelProperty("邮件内容")
    private String content;

    @ApiModelProperty("发送者")
    private String sender;

    @ApiModelProperty("接收者")
    private List<String> to;

    @ApiModelProperty("抄送者")
    private List<String> cc;

    @ApiModelProperty("密送者")
    private List<String> bcc;

    @ApiModelProperty("附件")
    private List<MultipartFile> attachments;

}
