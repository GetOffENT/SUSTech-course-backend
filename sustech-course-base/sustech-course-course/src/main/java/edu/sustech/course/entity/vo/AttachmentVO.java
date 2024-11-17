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
 * @since 2024-11-18 2:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "文件")
public class AttachmentVO {
    @ApiModelProperty("附件UUID")
    private String uuid;

    @ApiModelProperty("附件版本")
    private Integer version;

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

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;
}
