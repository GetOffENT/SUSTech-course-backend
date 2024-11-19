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
 * @since 2024-11-19 21:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "章节信息")
public class ChapterDTO {

    @ApiModelProperty("课程ID")
    private Long courseId;

    @ApiModelProperty("章节名称")
    private String title;
}
