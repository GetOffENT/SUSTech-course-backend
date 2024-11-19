package edu.sustech.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-19 22:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(description = "附件")
public class AttachmentDTO {

    @ApiModelProperty("附件ID")
    private Long id;

    @ApiModelProperty("附件UUID")
    private String uuid;

    @ApiModelProperty("附件版本")
    private Integer version;

    @ApiModelProperty("课程ID")
    private Long courseId;

    @ApiModelProperty("章节ID")
    private Long chapterId;

    @ApiModelProperty("视频(小节)ID")
    private Long videoId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String fileUrl;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("文件类型")
    private String fileType;
}
