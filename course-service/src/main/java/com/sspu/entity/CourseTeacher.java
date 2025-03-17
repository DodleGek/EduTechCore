package com.sspu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("course_teacher")
public class CourseTeacher implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 课程标识
     */
    @Schema(description = "课程标识")
    private Long courseId;

    /**
     * 教师标识（教师名称）
     */
    @Schema(description = "教师名称")
    private String teacherName;

    /**
     * 教师职位
     */
    @Schema(description = "教师职位")
    private String position;

    /**
     * 教师简介
     */
    @Schema(description = "教师简介")
    private String introduction;

    /**
     * 照片
     */
    @Schema(description = "教师照片")
    private String photograph;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createDate;
}
