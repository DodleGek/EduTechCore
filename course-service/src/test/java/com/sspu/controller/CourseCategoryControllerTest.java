package com.sspu.controller;

import com.sspu.dto.CourseCategoryTreeDto;
import com.sspu.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CourseCategoryControllerTest {

    @Autowired
    CourseCategoryService courseCategoryService;

    @Test
    public void queryTreeNodes() {

        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryService.queryTreeNodes("1");
        System.out.println(courseCategoryTreeDtos);

    }

}