package com.sspu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "查询课程参数DTO")
public class QueryCourseParamsDto {

    @Schema(description = "审核状态", example = "NOT_AUDIT")
    private String auditStatus;

    @Schema(description = "课程名称", example = "Java编程基础")
    private String courseName;

    @Schema(description = "发布状态", example = "PUBLISHED")
    private String publishStatus;
}
