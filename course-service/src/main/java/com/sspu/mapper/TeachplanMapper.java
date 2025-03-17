package com.sspu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sspu.dto.TeachplanDto;
import com.sspu.entity.Teachplan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeachplanMapper extends BaseMapper<Teachplan> {

    //查询课程计划(组成树型结构)
    List<TeachplanDto> selectTreeNodes(Long courseId);
}
