package com.sspu.service;

import com.sspu.dto.PageParams;
import com.sspu.dto.PageResult;
import com.sspu.dto.QueryCourseParamDto;
import com.sspu.entity.CourseBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CourseBaseInfoServiceTest {

    @Autowired
    CourseBaseInfoService courseBaseInfoService;

    @Test
    public void testCourseBaseInfoService() {
        // 查询课程信息
        QueryCourseParamDto courseParamDto = new QueryCourseParamDto();
        courseParamDto.setCourseName("java");
        courseParamDto.setAuditStatus("202004");
        // 分页参数查询
        PageParams pageParams = new PageParams(2L, 2L);

        PageResult<CourseBase> courseBasePageResult = courseBaseInfoService.queryCourseBaseList(256L, pageParams, courseParamDto);

        System.out.println(courseBasePageResult);
    }
}