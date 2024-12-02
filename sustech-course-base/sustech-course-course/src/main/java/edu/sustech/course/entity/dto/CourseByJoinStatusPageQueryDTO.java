package edu.sustech.course.entity.dto;

import edu.sustech.api.entity.enums.CourseForm;
import edu.sustech.common.enums.JoinStatus;
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
 * @since 2024-12-02 18:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "根据加入状态查询课程分页查询DTO")
public class CourseByJoinStatusPageQueryDTO {

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

    @ApiModelProperty("课程总时长")
    private List<Double> duration;

    @ApiModelProperty("标签 回车分隔")
    private List<String> tags;

    @ApiModelProperty("加入状态  0否 1申请中 2已加入 3申请被拒绝 4已退出")
    private JoinStatus joinStatus;
}
