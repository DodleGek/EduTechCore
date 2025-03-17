package com.sspu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "新增课程基本信息")
public class AddCourseDto {

    @NotEmpty(message = "添加课程名称不能为空")
    @NotEmpty(message = "修改课程名称不能为空")
    @Schema(description = "课程名称")
    private String name;

    @NotEmpty(message = "适用人群不能为空")
    @Size(min = 10, message = "适用人群内容过少，至少10个字符")
    @Schema(description = "适用人群")
    private String users;

    @Schema(description = "课程标签")
    private String tags;

    @NotEmpty(message = "课程分类不能为空")
    @Schema(description = "大分类")
    private String mt;

    @NotEmpty(message = "课程分类不能为空")
    @Schema(description = "小分类")
    private String st;

    @NotEmpty(message = "课程等级不能为空")
    @Schema(description = "课程等级")
    private String grade;

    @Schema(description = "教学模式（普通，录播，直播等）")
    private String teachMode;

    @Schema(description = "课程介绍")
    private String description;

    @Schema(description = "课程图片")
    private String pic;

    @NotEmpty(message = "收费规则不能为空")
    @Schema(description = "收费规则，对应数据字典")
    private String charge;

    @Schema(description = "价格")
    private Float price;

    @Schema(description = "原价")
    private Float originalPrice;

    @Schema(description = "qq")
    private String qq;

    @Schema(description = "微信")
    private String wechat;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "有效期")
    private Integer validDays;
}
