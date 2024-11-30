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
 * @since 2024-11-30 14:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(description = "学生信息")
public class StudentDTO {

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户头像url")
    private String avatar;
}
