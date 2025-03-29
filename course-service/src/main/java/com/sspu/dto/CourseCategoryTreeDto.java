package com.sspu.dto;

import com.sspu.entity.CourseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategoryTreeDto extends CourseCategory {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<CourseCategoryTreeDto> childrenTreeNodes;
}
