package com.sspu.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sspu.dto.PageParams;
import com.sspu.dto.PageResult;
import com.sspu.dto.QueryCourseParamDto;
import com.sspu.entity.CourseBase;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CourseBaseMapperTest {

    @Autowired
    CourseBaseMapper courseBaseMapper;


    @Test
    public void testCourseBaseMapper() {
        Assertions.assertNotNull(courseBaseMapper.selectById(18));

        //详细进行分页查询的单元测试
        //查询条件
        QueryCourseParamDto courseParamsDto = new QueryCourseParamDto();
        courseParamsDto.setCourseName("java"); //课程名称


        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //根据课程名称模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()), CourseBase::getName, courseParamsDto.getCourseName());
        //根据课程审核状态
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, courseParamsDto.getAuditStatus());

        PageParams pageParams = new PageParams();
        pageParams.setPageNo(1L);
        pageParams.setPageSize(2L);

        //创建page分页参数对象
        Page<CourseBase> page = new Page<CourseBase>(pageParams.getPageNo(), pageParams.getPageSize());

        // 开始分页查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        List<CourseBase> items = pageResult.getRecords();
        long total = pageResult.getTotal();

        PageResult<CourseBase> courseBasePageResult = new PageResult<>(items, total, pageParams.getPageNo(), pageParams.getPageSize());

        System.out.println(courseBasePageResult);

    }

}