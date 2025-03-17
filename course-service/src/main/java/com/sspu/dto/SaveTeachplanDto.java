package com.sspu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "保存教学计划DTO")
public class SaveTeachplanDto {

    @Schema(description = "教学计划id", example = "12345")
    private Long id;

    @Schema(description = "课程计划名称", example = "第一章：Java基础")
    private String pName;

    @Schema(description = "课程计划父级Id", example = "54321")
    private Long parentId;

    @Schema(description = "层级，分为1、2、3级", example = "1")
    private Integer grade;

    @Schema(description = "课程类型:1视频、2文档", example = "1")
    private String mediaType;

    @Schema(description = "课程标识", example = "67890")
    private Long courseId;

    @Schema(description = "课程发布标识", example = "98765")
    private Long coursePubId;

    @Schema(description = "是否支持试学或预览（试看）", example = "Y")
    private String isPreview;
}
