package edu.sustech.interaction.entity.dto;

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
 * @since 2024-12-02 9:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "新建或修改作业表单")
public class AssignmentDTO {

    @ApiModelProperty("作业ID")
    private Long id;

    @ApiModelProperty("课程ID")
    private Long courseId;

    @ApiModelProperty("作业标题")
    private String title;

    @ApiModelProperty("作业描述")
    private String description;

    @ApiModelProperty("截止日期")
    private LocalDateTime deadline;
}
