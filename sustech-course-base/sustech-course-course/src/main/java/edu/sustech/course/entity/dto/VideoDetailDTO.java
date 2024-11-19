package edu.sustech.course.entity.dto;

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
 * @since 2024-11-19 21:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "视频(小节)详细信息")
public class VideoDetailDTO {

    @ApiModelProperty("视频ID")
    private Long id;

    @ApiModelProperty("小节名称")
    private String title;

    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("显示排序")
    private Integer sort;

    @ApiModelProperty("是否公开 0不公开 1公开")
    private Byte isPublic;

    @ApiModelProperty("视频时长（秒）")
    private Double duration;

    @ApiModelProperty("至少观看时长（秒）")
    private Double minWatchTime;

}
