package edu.sustech.user.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-09
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Accessors(chain = true)
@ApiModel(value = "User对象", description = "用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty("微信openid")
    private String openid;

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

    @ApiModelProperty("积分")
    private Integer point;

    @ApiModelProperty("状态 0正常 1封禁 2注销")
    private Byte state;

    @ApiModelProperty("角色类型 0普通用户 1学生 2教师")
    private Byte role;

    @ApiModelProperty("教师头像url")
    private String teacherAvatar;

    @ApiModelProperty("教师简介")
    private String teacher;

    @ApiModelProperty("注销时间")
    private LocalDateTime gmtDestroy;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Byte isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
