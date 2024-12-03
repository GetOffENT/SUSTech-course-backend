package edu.sustech.user.entity.dto;

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
 * @since 2024-12-03 23:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "教师信息")
public class TeacherDTO {

    @ApiModelProperty("教师ID,即用户ID")
    private Long id;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("教师简介")
    private String teacherInfo;

    @ApiModelProperty("教师头像url")
    private String teacherAvatar;
}
