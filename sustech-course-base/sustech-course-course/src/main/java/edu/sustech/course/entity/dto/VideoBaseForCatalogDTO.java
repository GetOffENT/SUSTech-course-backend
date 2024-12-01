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
 * @since 2024-12-01 11:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "小节基本信息")
public class VideoBaseForCatalogDTO {

    @ApiModelProperty("小节ID")
    private Long id;

    @ApiModelProperty("章节ID")
    private Long chapterId;

    @ApiModelProperty("小节名称")
    private String title;
}
