package edu.sustech.interaction.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import edu.sustech.interaction.entity.enums.AssignmentUserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 作业学生关系表
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
@TableName("assignment_user")
@ApiModel(value = "AssignmentUser对象", description = "作业记录表")
public class AssignmentUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("作业记录ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("作业的ID")
    private Long assignmentId;

    @ApiModelProperty("创建当前课程的老师")
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

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Byte isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
