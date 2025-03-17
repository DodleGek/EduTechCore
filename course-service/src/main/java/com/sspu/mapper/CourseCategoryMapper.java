package com.sspu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sspu.dto.CourseCategoryTreeDto;
import com.sspu.entity.CourseCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {
    List<CourseCategoryTreeDto> selectTreeNodes();

}
