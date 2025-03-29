package com.sspu.dto;

import lombok.Data;

import java.util.List;

@Data
public class CoursePreviewDto {

    /**
     * 课程基本计划和课程营销信息
     */
    private CourseBaseInfoDto courseBase;

    /**
     * 课程计划信息
     */
    private List<TeachplanDto> teachPlans;
}
