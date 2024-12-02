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

import java.time.LocalDateTime;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-12-02 15:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "课程信息")
public class CourseInfoDTO {

    @ApiModelProperty("课程ID")
    private Long id;

    @ApiModelProperty("发布用户ID")
    private Long userId;

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

    @ApiModelProperty("总课程时长(秒,该课程所有视频时长总和)")
    private Double duration;

    @ApiModelProperty("是否公开 0不公开 1部分公开 2完全公开")
    private CourseOpenStatus openState;

    @ApiModelProperty("是否是已经认证老师的用户发布的课程 0否 1是")
    private Byte isTeacher;

    @ApiModelProperty("标签 回车分隔")
    private String tags;

    @ApiModelProperty("课程封面图片路径")
    private String coverUrl;

    @ApiModelProperty("加入课程人数")
    private Long joinCount;

    @ApiModelProperty("浏览数量")
    private Long viewCount;

    @ApiModelProperty("总弹幕数")
    private Long danmuCount;

    @ApiModelProperty("点赞数量")
    private Long likeCount;

    @ApiModelProperty("踩数量")
    private Long dislikeCount;

    @ApiModelProperty("收藏数")
    private Long collectCount;

    @ApiModelProperty("分享数")
    private Long shareCount;

    @ApiModelProperty("评论数量")
    private Long commentCount;

    @ApiModelProperty("状态 0审核中 1已过审 2未过审 3已发布 4已删除 5已私密 6编辑中")
    private CourseStatus status;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModified;
}
