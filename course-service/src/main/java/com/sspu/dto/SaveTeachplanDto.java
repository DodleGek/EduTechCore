package com.sspu.dto;

import lombok.Data;

@Data
public class SaveTeachplanDto {

    private Long id;

    private String pName;

    private Long parentId;

    private Integer grade;

    private String mediaType;

    private Long courseId;

    private Long coursePubId;

    private String isPreview;
}
