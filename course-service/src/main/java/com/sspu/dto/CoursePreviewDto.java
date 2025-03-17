package com.sspu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "课程预览信息DTO")
public class CoursePreviewDto {

    /**
     * 课程基本计划和课程营销信息
     */
    @Schema(description = "课程基本计划和课程营销信息")
    private CourseBaseInfoDto courseBase;

    /**
     * 课程计划信息
     */
    @Schema(description = "课程计划信息")
    private List<TeachplanDto> teachPlans;
}
