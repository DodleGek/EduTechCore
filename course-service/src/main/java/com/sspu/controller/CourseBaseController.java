package com.sspu.controller;

import com.sspu.dto.PageParams;
import com.sspu.dto.PageResult;
import com.sspu.dto.QueryCourseParamsDto;
import com.sspu.entity.CourseBase;
import com.sspu.service.CourseBaseInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CourseBaseController", description = "课程信息管理接口")
@RestController
public class CourseBaseController {

    private final CourseBaseInfoService courseBaseInfoService;

    public CourseBaseController(CourseBaseInfoService courseBaseInfoService) {
        this.courseBaseInfoService = courseBaseInfoService;
    }

    @Operation(summary = "课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParamDto) {
        return courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamDto);
    }
}
