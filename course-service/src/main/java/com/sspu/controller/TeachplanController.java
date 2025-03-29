package com.sspu.controller;

import com.sspu.dto.BindTeachplanMediaDto;
import com.sspu.dto.TeachplanDto;
import com.sspu.entity.Teachplan;
import com.sspu.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class TeachplanController {

    private final TeachplanService teachplanService;

    TeachplanController(TeachplanService teachplanService) {
        this.teachplanService = teachplanService;
    }

    // 查看课程计划树形结构
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId) {
        return teachplanService.findTeachplanTree(courseId);
    }

    // 课程计划创建
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody Teachplan teachplanDto) {
        teachplanService.saveTeachplan(teachplanDto);
    }

    // 课程计划删除
    @DeleteMapping("/teachplan/{teachplanId}")
    public void deleteTeachplan(@PathVariable Long teachplanId) {
        teachplanService.deleteTeachplan(teachplanId);
    }

    // 课程计划排序
    @PostMapping("/teachplan/{moveType}/{teachplanId}")
    public void orderByTeachplan(@PathVariable String moveType, @PathVariable Long teachplanId) {
        teachplanService.orderByTeachplan(moveType, teachplanId);
    }

    // 课程计划与媒资文件关联
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto) {
        teachplanService.associationMedia(bindTeachplanMediaDto);
    }

    // 课程计划与媒资文件解除关联
    @DeleteMapping("/teachplan/association/media/{teachPlanId}/{mediaId}")
    public void unassociationMedia(@PathVariable Long teachPlanId, @PathVariable String mediaId) {
        teachplanService.unassociationMedia(teachPlanId, mediaId);
    }

    // 获取课程计划信息
    @PostMapping("/teachplan/{teachplanId}")
    public Teachplan getTeachplan(@PathVariable Long teachplanId) {
        return teachplanService.getTeachplan(teachplanId);
    }
}
