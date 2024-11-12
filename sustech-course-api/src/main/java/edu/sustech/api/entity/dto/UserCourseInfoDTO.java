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
 * @since 2024-11-12 7:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(description = "用户发布的课程信息汇总")
public class UserCourseInfoDTO {

    @ApiModelProperty("总发布课程数")
    private Integer courseCount;

    @ApiModelProperty("总播放量")
    private Integer play;

    @ApiModelProperty("总点赞数")
    private Integer like;
}
