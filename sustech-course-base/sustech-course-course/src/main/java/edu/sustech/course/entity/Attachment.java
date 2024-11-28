package edu.sustech.course.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程视频对应附件
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Accessors(chain = true)
@ApiModel(value = "Attachment对象", description = "课程视频对应附件")
public class Attachment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("附件ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("附件UUID")
    private String uuid;

    @ApiModelProperty("附件版本")
    private Integer version;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("课程ID")
    private Long courseId;

    @ApiModelProperty("章节ID")
    private Long chapterId;

    @ApiModelProperty("视频ID")
    private Long videoId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String fileUrl;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("是否是课件 0否 1是")
    private Byte isLecture;

    @ApiModelProperty("是否可以下载 0否 1是")
    private Byte isDownload;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Byte isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
