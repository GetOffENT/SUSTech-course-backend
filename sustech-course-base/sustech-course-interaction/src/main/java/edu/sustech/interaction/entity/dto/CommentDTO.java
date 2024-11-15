package edu.sustech.interaction.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-15 15:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "评论信息")
public class CommentDTO implements Serializable {

    @ApiModelProperty("视频id")
    Long vid;

    @ApiModelProperty("用户id")
    Long uid;

    @ApiModelProperty("根评论id")
    Long rootId;

    @ApiModelProperty("被回复的评论id")
    Long parentId;

    @ApiModelProperty("被回复的用户id")
    Long toUserId;

    @ApiModelProperty("评论内容")
    String content;

}
