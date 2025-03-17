package com.sspu.controller;

import com.sspu.dto.PageParams;
import com.sspu.dto.PageResult;
import com.sspu.dto.QueryCourseParamsDto;
import com.sspu.entity.CourseBase;
import com.sspu.service.CourseBaseInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseBaseController {

    private final CourseBaseInfoService courseBaseInfoService;

    public CourseBaseController(CourseBaseInfoService courseBaseInfoService) {
        this.courseBaseInfoService = courseBaseInfoService;
    }

    @RequestMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParamDto) {
        return courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamDto);
    }
}
