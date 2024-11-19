package edu.sustech.course.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-19 21:43
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "章节详细信息")
public class ChapterDetailDTO {

    @ApiModelProperty("章节ID")
    private Long id;

    @ApiModelProperty("章节名称")
    private String title;

    @ApiModelProperty("显示排序")
    private Integer sort;

    @ApiModelProperty("视频(小节)列表")
    List<VideoDetailDTO> videos;
}
