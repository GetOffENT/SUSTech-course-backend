package edu.sustech.interaction.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程评教自定义指标及得分
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Accessors(chain = true)
@TableName("course_review_score")
@ApiModel(value = "CourseReviewScore对象", description = "课程评教自定义指标及得分")
public class CourseReviewScore implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评教指标id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("评论id")
    private Long reviewId;

    @ApiModelProperty("课程id")
    private Long courseId;

    @ApiModelProperty("评教指标")
    @TableField("`index`")
    private String index;

    @ApiModelProperty("评分")
    private Byte score;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Byte isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
