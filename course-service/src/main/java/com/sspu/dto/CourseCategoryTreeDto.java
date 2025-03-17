package com.sspu.dto;

import com.sspu.entity.CourseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategoryTreeDto extends CourseCategory {

    @Schema(description = "子节点树列表")
    private List<CourseCategoryTreeDto> childrenTreeNodes;
}
