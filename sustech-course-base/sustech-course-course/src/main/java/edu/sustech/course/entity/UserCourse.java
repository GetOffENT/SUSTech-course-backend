package edu.sustech.course.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import edu.sustech.common.enums.JoinEnum;
import edu.sustech.course.entity.enums.LikeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程用户关系
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Accessors(chain = true)
@TableName("user_course")
@ApiModel(value = "UserCourse对象", description = "课程用户关系")
public class UserCourse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程用户关系ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("课程ID")
    private Long courseId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("是否点赞 0否 1是")
    @TableField("`like`")
    private LikeEnum like;

    @ApiModelProperty("加入状态  0否 1申请中 2已加入 3申请被拒绝 4已退出")
    private JoinEnum joinState;

    @ApiModelProperty("已学习时长(秒)")
    private Double learnedTime;

    @ApiModelProperty("学习进度(已学习时长/总课程时长) x% -> x*100")
    private Integer progress;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Byte isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
