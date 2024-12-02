package edu.sustech.interaction.entity.vo;

import edu.sustech.api.entity.dto.UserDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 封装评论树的类
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-13 17:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "封装的评论树")
public class CommentTreeVO implements Serializable {
    @ApiModelProperty("评论id")
    private Long id;

    @ApiModelProperty("视频id")
    private Long videoId;

    @ApiModelProperty("发布者信息")
    private UserDTO user;

    @ApiModelProperty("根节点评论的id,如果为0表示为根节点")
    private Long rootId;

    @ApiModelProperty("被回复的评论id，只有root_id为0时才允许为0，表示根评论")
    private Long parentId;

    @ApiModelProperty("被回复的用户信息")
    private UserDTO toUser;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("该条评论的点赞数")
    private Long likeCount;

    @ApiModelProperty("不喜欢的数量")
    private Long dislikeCount;

    @ApiModelProperty("是否置顶 0普通 1置顶")
    private Byte isTop;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("评论回复 只有根评论有")
    private List<CommentTreeVO> replies;

    @ApiModelProperty("回复数量")
    private Long count;
}
