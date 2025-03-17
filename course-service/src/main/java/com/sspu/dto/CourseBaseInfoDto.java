package com.sspu.dto;

import com.sspu.entity.CourseBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "课程基本信息DTO")
public class CourseBaseInfoDto extends CourseBase {

    /**
     * 收费规则，对应数据字典
     */
    @Schema(description = "收费规则，对应数据字典", example = "免费")
    private String charge;

    /**
     * 价格
     */
    @Schema(description = "价格", example = "99.99")
    private Float price;

    /**
     * 原价
     */
    @Schema(description = "原价", example = "199.99")
    private Float originalPrice;

    /**
     * 咨询qq
     */
    @Schema(description = "咨询qq", example = "123456789")
    private String qq;

    /**
     * 微信
     */
    @Schema(description = "微信", example = "weixin123")
    private String wechat;

    /**
     * 电话
     */
    @Schema(description = "电话", example = "13800138000")
    private String phone;

    /**
     * 有效期天数
     */
    @Schema(description = "有效期天数", example = "365")
    private Integer validDays;

    /**
     * 大分类名称
     */
    @Schema(description = "大分类名称", example = "计算机科学")
    private String mtName;

    /**
     * 小分类名称
     */
    @Schema(description = "小分类名称", example = "编程语言")
    private String stName;
}
