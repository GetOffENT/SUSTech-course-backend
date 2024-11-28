package edu.sustech.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-15 17:51
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(description = "视频信息")
public class VideoDTO {

    @ApiModelProperty("视频ID")
    private Long id;

    @ApiModelProperty("课程ID")
    private Long courseId;

    @ApiModelProperty("章节ID")
    private Long chapterId;

    @ApiModelProperty("发布用户ID")
    private Long userId;

    @ApiModelProperty("小节名称")
    private String title;

    @ApiModelProperty("视频时长（秒）")
    private Double duration;

    @ApiModelProperty("至少观看时长（秒）")
    private Double minWatchTime;

    @ApiModelProperty("显示排序")
    private Integer sort;

    @ApiModelProperty("是否公开 0不公开 1公开")
    private Byte isPublic;

    @ApiModelProperty("是否已经学习 0未学习 1已学习")
    private Byte isLearned;

    @ApiModelProperty("视频源ID")
    private String videoSourceId;
}
