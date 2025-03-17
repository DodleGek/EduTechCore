package com.sspu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("course_base")
@Schema(description = "课程基本信息实体类")
public class CourseBase implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 机构ID
     */
    @Schema(description = "机构ID")
    private Long companyId;

    /**
     * 机构名称
     */
    @Schema(description = "机构名称")
    private String companyName;

    /**
     * 课程名称
     */
    @Schema(description = "课程名称")
    private String name;

    /**
     * 适用人群
     */
    @Schema(description = "适用人群")
    private String users;

    /**
     * 课程标签
     */
    @Schema(description = "课程标签")
    private String tags;

    /**
     * 大分类
     */
    @Schema(description = "大分类")
    private String mt;

    /**
     * 小分类
     */
    @Schema(description = "小分类")
    private String st;

    /**
     * 课程等级
     */
    @Schema(description = "课程等级")
    private String grade;

    /**
     * 教育模式(common普通，record 录播，live直播等）
     */
    @Schema(description = "教育模式(common普通，record 录播，live直播等)")
    private String teachMode;

    /**
     * 课程介绍
     */
    @Schema(description = "课程介绍")
    private String description;

    /**
     * 课程图片
     */
    @Schema(description = "课程图片")
    private String pic;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createPeople;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private String changePeople;

    /**
     * 审核状态
     */
    @Schema(description = "审核状态")
    private String auditStatus;

    /**
     * 课程发布状态 未发布  已发布 下线
     */
    @Schema(description = "课程发布状态 未发布  已发布 下线")
    private String status;

}
