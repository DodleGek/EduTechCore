package com.sspu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("teachplan_media")
@Schema(description = "课程计划媒资实体类")
public class TeachplanMedia implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 媒资文件id
     */
    @Schema(description = "媒资文件id")
    private String mediaId;

    /**
     * 课程计划标识
     */
    @Schema(description = "课程计划标识")
    private Long teachPlanId;

    /**
     * 课程标识
     */
    @Schema(description = "课程标识")
    private Long courseId;

    /**
     * 媒资文件原始名称
     */
    @Schema(description = "媒资文件原始名称")
    private String mediaFilename;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createPeople;

    /**
     * 修改人
     */
    @Schema(description = "修改人")
    private String changePeople;
}
   