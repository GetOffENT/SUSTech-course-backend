package edu.sustech.interaction.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import edu.sustech.interaction.entity.enums.VideoCommentLikeStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 视频评论点赞列表
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-12-03
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Accessors(chain = true)
@TableName("video_comment_like")
@ApiModel(value = "VideoCommentLike对象", description = "视频评论点赞列表")
public class VideoCommentLike implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论点赞id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("视频id")
    private Long videoId;

    @ApiModelProperty("评论id")
    private Long commentId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("点赞状态  0无操作 1点赞 2点踩")
    private VideoCommentLikeStatus likeStatus;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Byte isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
