package edu.sustech.course.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-18 21:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户观看视频记录")
public class UserVideoRecordDTO implements Serializable {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("视频id")
    private Long videoId;

    @ApiModelProperty("章节id")
    private Long chapterId;

    @ApiModelProperty("课程id")
    private Long courseId;

    /**
     * 其实可以用Integer，记录无需很准确，但懒得改数据库了
     * 0表示未开始或者已经看完了
     */
    @ApiModelProperty("结束视频观看的时间点")
    private Double playTime;

    /**
     * 根据该值计算是否已经学习完成，可以从后端查，但之前前端获取了该数据，这里直接传过来减少一次数据库查询
     */
    @ApiModelProperty("至少观看时长（秒）")
    private Double minWatchTime;

    @ApiModelProperty("视频观看的时间点，两两一组(开始观看、调整进度条、结束观看时均打点记录)")
    List<Integer> timePoints;
}
