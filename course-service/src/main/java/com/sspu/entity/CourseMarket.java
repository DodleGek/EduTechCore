package com.sspu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("course_market")
public class CourseMarket implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键，课程id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键，课程id")
    private Long id;

    /**
     * 收费规则，对应数据字典
     */
    @Schema(description = "收费规则")
    private String charge;

    /**
     * 现价
     */
    @Schema(description = "现价")
    private Float price;

    /**
     * 原价
     */
    @Schema(description = "原价")
    private Float originalPrice;

    /**
     * 咨询qq
     */
    @Schema(description = "咨询qq")
    private String qq;

    /**
     * 微信
     */
    @Schema(description = "微信")
    private String wechat;

    /**
     * 电话
     */
    @Schema(description = "电话")
    private String phone;

    /**
     * 有效期天数
     */
    @Schema(description = "有效期天数")
    private Integer validDays;


}
