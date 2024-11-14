package edu.sustech.course.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户观看记录
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-14
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Accessors(chain = true)
@TableName("user_video_record")
@ApiModel(value = "UserVideoRecord对象", description = "用户观看记录")
public class UserVideoRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户观看记录ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("视频ID")
    private Long videoId;

    @ApiModelProperty("章节ID")
    private Long chapterId;

    @ApiModelProperty("课程ID")
    private Long courseId;

    @ApiModelProperty("播放时长(分秒，十分之一秒)")
    private Integer playTime;

    @ApiModelProperty("播放区间 例:\"(1,2), (3,6), (4,7)\"")
    private String playRange;

    @ApiModelProperty("总已观看时长(分秒，十分之一秒)")
    private Integer totalPlayTime;

    @ApiModelProperty("是否学习完毕 0否 1是(根据min_watch_time)")
    private Byte isLearned;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Byte isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
