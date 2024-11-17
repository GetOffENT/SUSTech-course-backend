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
 * 课程评价
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
@TableName("course_review")
@ApiModel(value = "CourseReview对象", description = "课程评价")
public class CourseReview implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("课程id")
    private Long courseId;

    @ApiModelProperty("发布者id")
    private Long userId;

    @ApiModelProperty("评分 0-5")
    private Double score;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("该条评论的点赞数")
    private Long love;

    @ApiModelProperty("不喜欢的数量")
    private Long bad;

    @ApiModelProperty("是否置顶 0普通 1置顶")
    private Byte isTop;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Byte isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
