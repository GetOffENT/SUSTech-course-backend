package edu.sustech.course.entity.vo;

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
 * @since 2024-11-14 3:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "视频(即小节)")
public class VideoVO {

    @ApiModelProperty("视频ID")
    private Long id;

    @ApiModelProperty("课程ID")
    private Long chapterId; //没有实际用户，方便service写代码

    @ApiModelProperty("小节名称")
    private String title;

    @ApiModelProperty("视频时长（秒）")
    private Double duration;

    @ApiModelProperty("是否公开 0不公开 1公开")
    private Byte isPublic;

    @ApiModelProperty("是否已经学习 0未学习 1已学习")
    private Byte isLearned;
}
