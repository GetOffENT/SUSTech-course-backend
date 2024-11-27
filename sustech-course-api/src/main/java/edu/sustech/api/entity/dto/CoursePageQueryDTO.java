package edu.sustech.api.entity.dto;

import edu.sustech.api.entity.enums.CourseForm;
import edu.sustech.api.entity.enums.CourseOpenStatus;
import edu.sustech.api.entity.enums.CourseStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-19 20:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "新增课程")
public class CoursePageQueryDTO {

    @ApiModelProperty("页码")
    private Integer page;

    @ApiModelProperty("每页大小")
    private Integer pageSize;

    @ApiModelProperty("主分类ID")
    private String mcId;

    @ApiModelProperty("子分类ID")
    private String scId;

    @ApiModelProperty("课程标题")
    private String title;

    @ApiModelProperty("课程形式 1课件 2视频 3直播")
    private CourseForm form;

    @ApiModelProperty("类型 1自制 2转载")
    private Byte type;

    @ApiModelProperty("作者声明 0不声明 1未经允许禁止转载")
    private Byte auth;

    @ApiModelProperty("课程总时长")
    private List<Double> duration;

    @ApiModelProperty("是否公开 0不公开 1部分公开 2完全公开")
    private List<CourseOpenStatus> openState;

    @ApiModelProperty("标签 回车分隔")
    private List<String> tags;

    @ApiModelProperty("课程状态 0审核中 1已过审 2未过审 3已删除 5已私密 6编辑中")
    private List<CourseStatus> status;
}
