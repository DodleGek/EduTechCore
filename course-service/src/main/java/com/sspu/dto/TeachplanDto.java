package com.sspu.dto;

import com.sspu.entity.Teachplan;
import com.sspu.entity.TeachplanMedia;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Schema(description = "教学计划DTO")
public class TeachplanDto extends Teachplan {

    @Schema(description = "课程媒资信息")
    private TeachplanMedia teachplanMedia;

    @Schema(description = "课程计划子目录")
    private List<TeachplanDto> teachPlanTreeNodes;
}
