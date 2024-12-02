package edu.sustech.interaction.entity.vo;

import edu.sustech.interaction.entity.enums.AssignmentUserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-12-02 9:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "作业记录")
public class AssignmentUserVO {

    @ApiModelProperty("作业的ID")
    private Long assignmentId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("作业状态 0未提交 1已提交 2已批改 3已过期")
    private AssignmentUserStatus status;

    @ApiModelProperty("提交信息")
    private String content;

    @ApiModelProperty("提交时间")
    private LocalDateTime gmtSubmit;

    @ApiModelProperty("老师评语")
    private String feedback;

    @ApiModelProperty("等级 A, B, C, D, F 五个等级")
    private String grade;

    @ApiModelProperty("分数")
    private Integer score;

    @ApiModelProperty("批改时间")
    private LocalDateTime gmtGrade;
}
