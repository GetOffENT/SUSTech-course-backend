package edu.sustech.course.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程视频
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Accessors(chain = true)
@ApiModel(value = "Video对象", description = "课程视频")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("视频ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("课程ID")
    private Long courseId;

    @ApiModelProperty("章节ID")
    private Long chapterId;

    @ApiModelProperty("发布用户ID")
    private Long userId;

    @ApiModelProperty("小节名称")
    private String title;

    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("云端视频资源")
    private String videoSourceId;

    @ApiModelProperty("原始文件名称")
    private String videoOriginalName;

    @ApiModelProperty("显示排序")
    private Integer sort;

    @ApiModelProperty("是否公开 0不公开 1公开")
    private Byte isPublic;

    @ApiModelProperty("视频时长（秒）")
    private Double duration;

    @ApiModelProperty("至少观看时长（秒）")
    private Double minWatchTime;

    @ApiModelProperty("视频源文件大小（字节）")
    private Long size;

    @ApiModelProperty("播放量")
    private Long play;

    @ApiModelProperty("弹幕数")
    private Long danmu;

    @ApiModelProperty("评论数量统计")
    private Long comment;

    @ApiModelProperty("乐观锁")
    private Long version;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Byte isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
