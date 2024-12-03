package edu.sustech.interaction.entity.vo;

import edu.sustech.interaction.entity.enums.VideoCommentLikeStatus;
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
 * @since 2024-12-03 13:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "视频评论点赞记录(根据用户ID获取)")
public class VideoCommentLikeVO {

    @ApiModelProperty("评论id")
    private Long commentId;

    @ApiModelProperty("点赞状态  0无操作 1点赞 2点踩")
    private VideoCommentLikeStatus likeStatus;
}
