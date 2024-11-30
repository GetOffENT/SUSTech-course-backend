package edu.sustech.message.entity.dto;

import edu.sustech.common.enums.JoinEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-30 22:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "更新课程加入状态")
public class CourseJoinStatusDTO {

    @ApiModelProperty(value = "被操作用户的邮箱")
    private String email;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "状态")
    private JoinEnum status;

    @ApiModelProperty(value = "拒绝原因")
    private String reason;

    @ApiModelProperty(value = "邀请或申请 0:邀请 1:申请")
    private Integer inviteOrApply;
}
