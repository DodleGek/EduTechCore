package com.sspu.service;

import com.sspu.dto.PageParams;
import com.sspu.dto.PageResult;
import com.sspu.dto.QueryCourseParamsDto;
import com.sspu.entity.CourseBase;

public interface CourseBaseInfoService {

    // 课程分页查询
    PageResult<CourseBase> queryCourseBaseList(PageParams params, QueryCourseParamsDto queryCourseParamsDto);
}
