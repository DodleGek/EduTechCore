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

@RestController
@RequestMapping("/open")
public class MediaOpenController {

    private MediaFileService mediaFileService;

    MediaOpenController(MediaFileService mediaFileService) {
        this.mediaFileService = mediaFileService;
    }

    @GetMapping("/preview/{mediaId}")
    public R<String> getMediaUrl(@PathVariable String mediaId) {
        MediaFiles mediaFile = mediaFileService.getFileById(mediaId);
        if (mediaFile == null || StringUtils.isEmpty(mediaFile.getUrl())) {
            EduxException.cast("视频还没有转码处理");
        }
        return R.success(mediaFile.getUrl());
    }

}
