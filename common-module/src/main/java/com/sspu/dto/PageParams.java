package com.sspu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {
    // 默认起始页码
    public static final long DEFAULT_PAGE_CURRENT = 1L;
    // 默认每页记录数
    public static final long DEFAULT_PAGE_SIZE = 10L;

    // 当前页码，直接设置默认值
    private Long pageNo = DEFAULT_PAGE_CURRENT;

    // 当前每页记录数，直接设置默认值
    private Long pageSize = DEFAULT_PAGE_SIZE;
}