package com.sspu.dto;

import com.sspu.exception.ValidationGroups;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddCourseDto {

    @NotEmpty(message = "添加课程名称不能为空", groups = {ValidationGroups.Insert.class})
    @NotEmpty(message = "修改课程名称不能为空", groups = {ValidationGroups.Update.class})
    private String name;

    @NotEmpty(message = "适用人群不能为空")
    @Size(min = 10, message = "适用人群内容过少，至少10个字符")
    private String users;

    private String tags;

    @NotEmpty(message = "课程分类不能为空")
    private String mt;

    @NotEmpty(message = "课程分类不能为空")
    private String st;

    @NotEmpty(message = "课程等级不能为空")
    private String grade;

    private String teachMode;

    @Size(min = 10, message = "课程描述内容过少，至少10个字符")
    private String description;

    private String pic;

    @NotEmpty(message = "收费规则不能为空")
    private String charge;

    private Float price;

    private Float originalPrice;

    private String qq;

    private String wechat;

    private String phone;

    private Integer validDays;
}
