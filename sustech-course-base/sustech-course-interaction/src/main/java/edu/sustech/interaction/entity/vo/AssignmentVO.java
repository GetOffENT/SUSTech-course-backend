package edu.sustech.interaction.entity.vo;

import edu.sustech.interaction.entity.enums.AssignmentStatus;
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
 * @since 2024-12-02 8:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "作业信息")
public class AssignmentVO {

    @ApiModelProperty("该作业ID")
    private Long id;

    @ApiModelProperty("该作业的名称")
    private String title;

    @ApiModelProperty("作业状态 1未发布 2已发布 3已批改")
    private AssignmentStatus status;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("该作业的截止时间")
    private LocalDateTime deadline;
}
