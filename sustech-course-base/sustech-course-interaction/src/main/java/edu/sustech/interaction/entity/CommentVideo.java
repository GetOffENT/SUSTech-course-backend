package edu.sustech.interaction.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Accessors(chain = true)
@TableName("comment_video")
@ApiModel(value = "CommentVideo对象", description = "评论")
public class CommentVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("视频id")
    private Long videoId;

    @ApiModelProperty("发布者id")
    private Long userId;

    @ApiModelProperty("根节点评论的id,如果为0表示为根节点")
    private Long rootId;

    @ApiModelProperty("被回复的评论id，只有root_id为0时才允许为0，表示根评论")
    private Long parentId;

    @ApiModelProperty("被回复的用户id")
    private Long toUserId;

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
