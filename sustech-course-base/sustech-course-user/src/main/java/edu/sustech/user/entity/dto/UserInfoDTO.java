package edu.sustech.user.entity.dto;

import edu.sustech.user.entity.enums.Gender;
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
 * @since 2024-12-04 1:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户信息")
public class UserInfoDTO {

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("个性签名")
    private String description;

    @ApiModelProperty("性别 0女 1男 2未知")
    private Gender gender;
}
