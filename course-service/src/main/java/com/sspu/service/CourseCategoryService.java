package com.sspu.service;

import com.sspu.dto.CourseCategoryTreeDto;

import java.util.List;

public interface CourseCategoryService {
    /**
     * 课程分类查询
     *
     * @param id 根节点id
     * @return 根节点下面的所有子节点
     */
    List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
