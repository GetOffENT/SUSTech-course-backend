package edu.sustech.admin.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-10 20:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarouselVO implements Serializable {
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
}
