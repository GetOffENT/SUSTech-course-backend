package edu.sustech.course.entity.vo;

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
 * @since 2024-12-02 23:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户视频观看记录")
public class UserVideoRecordVO {
    @ApiModelProperty("用户观看记录ID")
    private Long id;

    @ApiModelProperty("视频ID")
    private Long videoId;

    @ApiModelProperty("章节ID")
    private Long chapterId;

    @ApiModelProperty("课程ID")
    private Long courseId;

    @ApiModelProperty("播放区间 例:\"(1,2), (3,6), (4,7)\"")
    private String playRange;

    @ApiModelProperty("总已观看时长(秒)")
    private Double totalPlayTime;

    @ApiModelProperty("是否学习完毕 0否 1是(根据min_watch_time)")
    private Byte isLearned;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModified;
}
