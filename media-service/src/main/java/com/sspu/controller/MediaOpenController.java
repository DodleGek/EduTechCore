package com.sspu.controller;

import com.sspu.dto.R;
import com.sspu.entity.MediaFiles;
import com.sspu.exception.EduxException;
import com.sspu.service.MediaFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 媒体文件开放接口控制器
 * 提供对外开放的媒体文件访问接口
 */
@RestController
@RequestMapping("/open")
public class MediaOpenController {

    private final MediaFileService mediaFileService;

    /**
     * 构造方法注入MediaFileService
     *
     * @param mediaFileService 媒体文件服务
     */
    MediaOpenController(MediaFileService mediaFileService) {
        this.mediaFileService = mediaFileService;
    }

    /**
     * 获取媒体文件的访问地址
     *
     * @param mediaId 媒体文件ID
     * @return 包含媒体文件访问URL的响应对象
     */
    @GetMapping("/preview/{mediaId}")
    public R<String> getMediaUrl(@PathVariable String mediaId) {
        MediaFiles mediaFile = mediaFileService.getFileById(mediaId);
        if (mediaFile == null || StringUtils.isEmpty(mediaFile.getUrl())) {
            EduxException.cast("视频还没有转码处理");
        }
        return R.success(mediaFile.getUrl());
    }

}