package edu.sustech.interaction.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * 评论具体指标以及得分
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-17 2:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "评论具体指标以及得分")
public class CourseReviewScoreVO {

    @ApiModelProperty("评教指标")
    private String index;

    @ApiModelProperty("评分")
    private Byte score;
}
