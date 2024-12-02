package edu.sustech.interaction.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import edu.sustech.interaction.entity.enums.CourseReviewLikeStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程评论点赞列表
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
@TableName("course_review_like")
@ApiModel(value = "CourseReviewLike对象", description = "课程评论点赞列表")
public class CourseReviewLike implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id 唯一标识")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("课程id")
    private Long courseId;

    @ApiModelProperty("评论id")
    private Long reviewId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("点赞状态  0无操作 1点赞 2点踩")
    private CourseReviewLikeStatus likeStatus;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Byte isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
