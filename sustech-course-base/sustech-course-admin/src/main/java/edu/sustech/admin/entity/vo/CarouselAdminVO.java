package edu.sustech.admin.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-10 21:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "管理端轮播图VO")
public class CarouselAdminVO implements Serializable {
    @ApiModelProperty("轮播图ID")
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

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModified;
}
