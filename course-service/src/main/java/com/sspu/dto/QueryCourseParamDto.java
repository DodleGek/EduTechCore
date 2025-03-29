package com.sspu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCourseParamDto {

    private String auditStatus;

    private String courseName;

    private String publishStatus;
}
