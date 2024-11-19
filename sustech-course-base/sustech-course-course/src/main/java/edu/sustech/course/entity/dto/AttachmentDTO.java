package edu.sustech.course.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-19 22:06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "附件(或课件)详细信息")
public class AttachmentDTO {

    @ApiModelProperty("附件ID")
    private Long id;

    @ApiModelProperty("是否是课件 0否 1是")
    private Byte isLecture;

    @ApiModelProperty("是否可以下载 0否 1是")
    private Byte isDownload;
}
