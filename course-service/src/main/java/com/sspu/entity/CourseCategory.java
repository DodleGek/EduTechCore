package com.sspu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("course_category")
@Schema(description = "课程分类实体类")
public class CourseCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String name;

    /**
     * 分类标签默认和名称一样
     */
    @Schema(description = "分类标签默认和名称一样")
    private String label;

    /**
     * 父结点id（第一级的父节点是0，自关联字段id）
     */
    @Schema(description = "父结点id（第一级的父节点是0，自关联字段id）")
    private String parentId;

    /**
     * 是否显示
     */
    @Schema(description = "是否显示")
    private Integer isShow;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private Integer orderBy;

    /**
     * 是否叶子
     */
    @Schema(description = "是否叶子")
    private Integer isLeaf;

}
