package edu.sustech.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-12 6:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(description = "用户信息")
public class UserDTO implements Serializable {
    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户头像url")
    private String avatar;

    @ApiModelProperty("主页背景图url")
    private String background;

    @ApiModelProperty("性别 0女 1男 2未知")
    private Byte gender;

    @ApiModelProperty("个性签名")
    private String description;

    @ApiModelProperty("经验值")
    private Integer exp;

    @ApiModelProperty("状态 0正常 1封禁 2注销")
    private Byte state;

    @ApiModelProperty("角色类型 0普通用户 1学生 2教师")
    private Byte role;

    @ApiModelProperty("总发布课程数")
    private Integer courseCount;

    @ApiModelProperty("关注数")
    private Integer followsCount;

    @ApiModelProperty("粉丝数")
    private Integer fansCount;

    @ApiModelProperty("获赞数")
    private Integer like;

    @ApiModelProperty("播放数")
    private Integer play;
}
