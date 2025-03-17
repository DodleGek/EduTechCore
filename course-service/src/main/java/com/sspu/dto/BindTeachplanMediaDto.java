package com.sspu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "教学计划-媒资绑定")
public class BindTeachplanMediaDto {

    @NotNull(message = "媒资文件id不能为空")
    @Schema(description = "媒资文件id", example = "media123")
    private String mediaId;

    @NotNull(message = "媒资文件名称不能为空")
    @Schema(description = "媒资文件名称", example = "Introduction to Java")
    private String fileName;

    @NotNull(message = "课程计划标识不能为空")
    @Schema(description = "课程计划标识", example = "12345")
    private Long teachPlanId;

    @Schema(description = "创建时间", example = "2023-10-01T12:34:56Z")
    private Date createTime;
}
