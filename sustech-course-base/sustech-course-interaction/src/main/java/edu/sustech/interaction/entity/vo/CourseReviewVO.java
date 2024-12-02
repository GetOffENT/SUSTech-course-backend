package edu.sustech.interaction.entity.vo;

import edu.sustech.api.entity.dto.UserDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 课程评价VO
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-17 2:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "课程评价")
public class CourseReviewVO {
    @ApiModelProperty("评论id")
    private Long id;

    @ApiModelProperty("发布用户id")
    private Long userId;

    @ApiModelProperty("评分，0-5")
    private Double score;

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

    @ApiModelProperty("具体指标以及得分")
    private List<CourseReviewScoreVO> indexScores;

    @ApiModelProperty("用户信息")
    private UserDTO user;
}
