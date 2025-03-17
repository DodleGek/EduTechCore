package com.sspu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "修改课程基本信息")
public class EditCourseDto extends AddCourseDto {

    @Schema(description = "课程id", example = "12345")
    private Long id;
}
