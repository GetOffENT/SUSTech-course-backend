package edu.sustech.interaction.entity.vo;

import edu.sustech.interaction.entity.enums.CourseReviewLikeStatus;
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
 * @since 2024-12-03 15:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "课程评价点赞记录(根据用户ID获取)")
public class CourseReviewLikeVO {

    @ApiModelProperty("评论id")
    private Long reviewId;

    @ApiModelProperty("点赞状态  0无操作 1点赞 2点踩")
    private CourseReviewLikeStatus likeStatus;
}
