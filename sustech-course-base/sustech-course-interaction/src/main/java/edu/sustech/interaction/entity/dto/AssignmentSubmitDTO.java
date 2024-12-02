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
 * @since 2024-12-02 10:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "作业提交信息")
public class AssignmentSubmitDTO {

    @ApiModelProperty(value = "作业ID")
    private Long assignmentId;

    @ApiModelProperty(value = "提交内容")
    private String content;
}
