package com.sspu.controller;

import com.sspu.dto.*;
import com.sspu.entity.MediaFiles;
import com.sspu.exception.EduxException;
import com.sspu.service.MediaFileService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 媒体文件控制器
 * 处理媒体文件的上传、查询等操作
 */
@RestController
public class MediaFilesController {

    MediaFileService mediaFileService;

    public MediaFilesController(MediaFileService mediaFileService) {
        this.mediaFileService = mediaFileService;
    }

    /**
     * 分页查询媒体文件列表
     *
     * @param pageParams          分页参数
     * @param queryMediaParamsDto 查询条件
     * @return 分页查询结果
     */
    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams pageParams, @RequestBody QueryMediaParamsDto queryMediaParamsDto) {
        // todo 获取当前用户
        Long companyId = 1232141425L;
        return mediaFileService.queryMediaFiles(companyId, pageParams, queryMediaParamsDto);
    }

    /**
     * 上传课程文件
     *
     * @param upload     上传的文件
     * @param folder     文件夹路径（可选）
     * @param objectName 对象名称（可选）
     * @return 上传结果
     */
    @PostMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResultDto upload(@RequestPart("filedata") MultipartFile upload,
                                      @RequestParam(value = "folder", required = false) String folder,
                                      @RequestParam(value = "objectName", required = false) String objectName) {
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFileSize(upload.getSize());
        String contentType = upload.getContentType();
        // 判断是否为图片类型
        if (contentType != null && contentType.contains("image")) {
            uploadFileParamsDto.setFileType("001001");  // 设置为图片类型
        }
        uploadFileParamsDto.setRemark("");
        uploadFileParamsDto.setFilename(upload.getOriginalFilename());
        uploadFileParamsDto.setContentType(contentType);
        // todo 获取当前用户
        Long companyId = 1232141425L;
        try {
            UploadFileResultDto uploadFileResultDto = mediaFileService.uploadFile(companyId, uploadFileParamsDto, upload.getBytes(), folder, objectName);
            return uploadFileResultDto;
        } catch (IOException e) {
            EduxException.cast("上传文件过程出错:" + e.getMessage());
        }
        return null;
    }

    /**
     * 根据媒体ID获取播放地址
     *
     * @param mediaId 媒体文件ID
     * @return 包含播放URL的响应对象
     */
    @GetMapping("/preview/{mediaId}")
    public R<String> getPlayUrlByMediaId(@PathVariable String mediaId) {
        MediaFiles mediaFile = mediaFileService.getFileById(mediaId);
        return R.success(mediaFile.getUrl());
    }
}