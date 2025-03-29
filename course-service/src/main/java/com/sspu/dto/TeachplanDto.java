package com.sspu.dto;

import com.sspu.entity.Teachplan;
import com.sspu.entity.TeachplanMedia;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
public class TeachplanDto extends Teachplan {

    // 媒资
    private TeachplanMedia teachplanMedia;

    // 小结
    private List<TeachplanDto> teachPlanTreeNodes;
}
