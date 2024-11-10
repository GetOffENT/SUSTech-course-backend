package edu.sustech.admin.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 轮播图表
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-10
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "Carousel对象", description = "轮播图表")
public class Carousel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("轮播图ID")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("轮播图url")
    private String url;

    @ApiModelProperty("轮播图标题")
    private String title;

    @ApiModelProperty("背景颜色 6位十六进制标准格式")
    private String color;

    @ApiModelProperty("点击跳转url")
    private String target;

    @ApiModelProperty("排序值 0为不显示，其他的越小越靠前")
    private Integer sort;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Byte isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
