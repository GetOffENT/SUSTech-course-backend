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
 * @since 2024-12-01 11:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "章节基本信息")
public class ChapterBaseForCatalogDTO {

    @ApiModelProperty("章节ID")
    private Long id;

    @ApiModelProperty("章节名称")
    private String title;

    @ApiModelProperty("章节下的小节基本信息")
    private List<VideoBaseForCatalogDTO> videos;
}
