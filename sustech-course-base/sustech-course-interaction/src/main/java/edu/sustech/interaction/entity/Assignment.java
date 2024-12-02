package edu.sustech.interaction.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import edu.sustech.interaction.entity.enums.AssignmentStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程作业
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-12-02
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Accessors(chain = true)
@ApiModel(value = "Assignment对象", description = "课程作业")
public class Assignment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("该作业ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("对应课程的ID")
    private Long courseId;

    @ApiModelProperty("创建当前课程的老师")
    private Long userId;

    @ApiModelProperty("该作业的名称")
    private String title;

    @ApiModelProperty("作业状态 1未发布 2已发布 3已批改")
    private AssignmentStatus status;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("该作业的截止时间")
    private LocalDateTime deadline;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Byte isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
