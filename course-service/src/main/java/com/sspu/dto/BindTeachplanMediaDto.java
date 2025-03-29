package com.sspu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class BindTeachplanMediaDto {

    @NotNull(message = "媒资文件id不能为空")
    private String mediaId;

    @NotNull(message = "媒资文件名称不能为空")
    private String fileName;

    @NotNull(message = "课程计划标识不能为空")
    private Long teachplanId;

    private Date createTime;
}
