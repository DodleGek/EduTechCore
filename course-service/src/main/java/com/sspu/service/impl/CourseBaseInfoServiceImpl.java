package com.sspu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sspu.dto.*;
import com.sspu.entity.CourseBase;
import com.sspu.entity.CourseCategory;
import com.sspu.entity.CourseMarket;
import com.sspu.exception.EduxException;
import com.sspu.mapper.CourseBaseMapper;
import com.sspu.mapper.CourseCategoryMapper;
import com.sspu.mapper.CourseMarketMapper;
import com.sspu.service.CourseBaseInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Resource
    CourseBaseMapper courseBaseMapper;

    @Resource
    CourseCategoryMapper courseCategoryMapper;

    @Resource
    CourseMarketMapper courseMarketMapper;

    @Resource
    CourseMarketServiceImpl courseMarketService;


    @Override
    @Transactional
    public PageResult<CourseBase> queryCourseBaseList(Long companyId, PageParams pageParams, QueryCourseParamDto queryCourseParams) {
        // 构建条件查询器
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseBase::getCompanyId, companyId);
        // 构建查询条件：按照课程名称模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParams.getCourseName()), CourseBase::getName, queryCourseParams.getCourseName());
        // 构建查询条件，按照课程审核状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParams.getAuditStatus()), CourseBase::getAuditStatus, queryCourseParams.getAuditStatus());
        // 构建查询条件，按照课程发布状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParams.getPublishStatus()), CourseBase::getStatus, queryCourseParams.getPublishStatus());
        // 分页对象
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<CourseBase> pageInfo = courseBaseMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<CourseBase> items = pageInfo.getRecords();
        // 获取数据总条数
        long counts = pageInfo.getTotal();
        // 构建结果集
        return new PageResult<>(items, counts, pageParams.getPageNo(), pageParams.getPageSize());
    }

    @Override
    @Transactional
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto) {
        // 合法性校验，由JSR-303校验
        // 封装请求参数
        // 封装课程基本信息
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(addCourseDto, courseBase);
        // 设置默认审核状态（去数据字典表中查询状态码）
        courseBase.setAuditStatus("202002");
        // 设置默认发布状态
        courseBase.setStatus("203001");
        // 设置机构id
        courseBase.setCompanyId(companyId);
        // 设置创建/更新人 todo: 从security当前用户中获取

        // 设置添加时间
        courseBase.setCreateDate(LocalDateTime.now());
        // 插入课程基本信息表
        int baseInsert = courseBaseMapper.insert(courseBase);
        Long courseId = courseBase.getId();
        // 封装课程营销信息
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseDto, courseMarket);
        courseMarket.setId(courseId);
        // 判断收费规则，若课程收费，则价格必须大于0
        int marketInsert = saveCourseMarket(courseMarket);
        if (baseInsert <= 0 || marketInsert <= 0) {
            EduxException.cast("新增课程基本信息失败");
        }
        // 返回添加的课程信息
        return getCourseBaseInfo(courseId);
    }

    private int saveCourseMarket(CourseMarket courseMarket) {
        String charge = courseMarket.getCharge();
        if (StringUtils.isBlank(charge))
            EduxException.cast("请设置收费规则");
        if (charge.equals("201001")) {
            if (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0) {
                EduxException.cast("课程价格不能为空，且必须大于0");
            }
        }
        // 插入课程营销信息表
        boolean flag = courseMarketService.saveOrUpdate(courseMarket);
        return flag ? 1 : -1;
    }

    @Override
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId) {
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        // 根据课程id查询课程基本信息
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null)
            return null;
        // 拷贝属性
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);

        // 根据课程id查询课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        // 拷贝属性
        if (courseMarket != null)
            BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);

        // 查询课程分类名称，并设置属性
        // 根据小分类id查询课程分类对象
        CourseCategory courseCategoryBySt = courseCategoryMapper.selectById(courseBase.getSt());
        // 设置课程的小分类名称
        courseBaseInfoDto.setStName(courseCategoryBySt.getName());
        // 根据大分类id查询课程分类对象
        CourseCategory courseCategoryByMt = courseCategoryMapper.selectById(courseBase.getMt());
        // 设置课程大分类名称
        courseBaseInfoDto.setMtName(courseCategoryByMt.getName());
        return courseBaseInfoDto;
    }

    @Override
    @Transactional
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {
        // 判断当前修改课程是否属于当前机构
        Long courseId = editCourseDto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (!companyId.equals(courseBase.getCompanyId())) {
            EduxException.cast("只允许修改本机构的课程");
        }
        // 拷贝对象
        BeanUtils.copyProperties(editCourseDto, courseBase);
        // 更新，设置更新时间
        courseBase.setChangeDate(LocalDateTime.now());
        courseBaseMapper.updateById(courseBase);
        // 查询课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        // 由于课程营销信息不是必填项，故这里先判断一下
        if (courseMarket == null) {
            courseMarket = new CourseMarket();
        }
        // 对象拷贝
        BeanUtils.copyProperties(editCourseDto, courseMarket);
        courseMarket.setId(courseId);
        // 获取课程收费状态并设置
        this.saveCourseMarket(courseMarket);
        return getCourseBaseInfo(courseId);
    }

    @Transactional
    @Override
    public void deleteCourse(Long companyId, Long courseId) {
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (!companyId.equals(courseBase.getCompanyId()))
            EduxException.cast("只允许删除本机构的课程");
        // todo :删除课程教师信息
        // todo :删除课程计划
        // 删除营销信息
        courseMarketMapper.deleteById(courseId);
        // 删除课程基本信息
        courseBaseMapper.deleteById(courseId);
    }

}

