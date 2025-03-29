package com.sspu.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EditCourseDto extends AddCourseDto {

    private Long id;
}
