package edu.sustech.interaction.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 弹幕表
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Accessors(chain = true)
@ApiModel(value = "Danmu对象", description = "弹幕表")
public class Danmu implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("弹幕ID")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("视频ID")
    private Long videoId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("弹幕内容")
    private String content;

    @ApiModelProperty("字体大小")
    private Byte fontsize;

    @ApiModelProperty("弹幕模式 1滚动 2顶部 3底部")
    private Byte mode;

    @ApiModelProperty("弹幕颜色 6位十六进制标准格式")
    private String color;

    @ApiModelProperty("弹幕所在视频的时间点(分秒，十分之一秒)")
    private Double timePoint;

    @ApiModelProperty("弹幕状态 1默认过审 2被举报审核中 3删除")
    private Byte state;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Byte isDelete;

    @ApiModelProperty("弹幕创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("弹幕更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
