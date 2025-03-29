package com.sspu.controller;

import com.sspu.dto.*;
import com.sspu.entity.CourseBase;
import com.sspu.exception.ValidationGroups;
import com.sspu.service.CourseBaseInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourseBaseController {

    private final CourseBaseInfoService courseBaseInfoService;

    public CourseBaseController(CourseBaseInfoService courseBaseInfoService) {
        this.courseBaseInfoService = courseBaseInfoService;
    }

    // 分页查询课程
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody QueryCourseParamDto queryCourseParams) {
        Long companyId = 1232141425L;
        // todo :companyId从 SecurityContextHolder 中获取当前用户信息
        PageResult<CourseBase> result = courseBaseInfoService.queryCourseBaseList(companyId, pageParams, queryCourseParams);
        return result;
    }

    // 新增课程
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated(ValidationGroups.Insert.class) AddCourseDto addCourseDto) {
        Long companyId = 1232141425L;
        // todo :companyId从 SecurityContextHolder 中获取当前用户信息
        return courseBaseInfoService.createCourseBase(companyId, addCourseDto);
    }

    // 根据课程id查询课程基本信息
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long courseId) {
        // todo :用户身份
        return courseBaseInfoService.getCourseBaseInfo(courseId);
    }

    // 修改课程
    @PutMapping("/course")
    public CourseBaseInfoDto modifyCourseBase(@RequestBody EditCourseDto editCourseDto) {
        Long companyId = 1232141425L;
        // todo :从 SecurityContextHolder 中获取当前用户信息
        return courseBaseInfoService.updateCourseBase(companyId, editCourseDto);
    }

    @DeleteMapping("/course/{courseId}")
    public void deleteCourse(@PathVariable Long courseId) {
        Long companyId = 1232141425L;
        // todo :从 SecurityContextHolder 中获取当前用户信息
        courseBaseInfoService.deleteCourse(companyId, courseId);
    }
}
