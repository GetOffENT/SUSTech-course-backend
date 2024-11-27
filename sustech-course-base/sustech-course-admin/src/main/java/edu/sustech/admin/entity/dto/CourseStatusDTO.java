package edu.sustech.admin.entity.dto;

import edu.sustech.api.entity.enums.CourseStatus;
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
 * @since 2024-11-27 23:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "更新课程状态DTO")
public class CourseStatusDTO {

    @ApiModelProperty("课程ID")
    private Long id;

    @ApiModelProperty("状态 0审核中 1已过审 2未过审 3已发布 4已删除 5已私密 6编辑中")
    private CourseStatus status;
}
