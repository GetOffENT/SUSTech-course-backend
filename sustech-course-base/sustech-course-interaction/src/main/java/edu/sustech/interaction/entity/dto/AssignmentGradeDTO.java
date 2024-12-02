package edu.sustech.interaction.entity.dto;

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
 * @since 2024-12-02 10:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "批改作业表单")
public class AssignmentGradeDTO {

    @ApiModelProperty(value = "作业ID")
    private Long assignmentId;

    @ApiModelProperty(value = "学生ID")
    private Long userId;

    @ApiModelProperty(value = "反馈")
    private String feedback;

    @ApiModelProperty(value = "等级")
    private String grade;

    @ApiModelProperty(value = "分数")
    private Integer score;
}
