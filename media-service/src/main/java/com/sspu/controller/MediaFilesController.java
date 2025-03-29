package com.sspu.controller;

import com.sspu.dto.*;
import com.sspu.entity.MediaFiles;
import com.sspu.exception.EduxException;
import com.sspu.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class MediaFilesController {


    @Autowired
    MediaFileService mediaFileService;


    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams pageParams, @RequestBody QueryMediaParamsDto queryMediaParamsDto) {
        Long companyId = 1232141425L;
        return mediaFileService.queryMediaFiles(companyId, pageParams, queryMediaParamsDto);

    }

    @RequestMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResultDto upload(@RequestPart("filedata") MultipartFile upload,
                                      @RequestParam(value = "folder", required = false) String folder,
                                      @RequestParam(value = "objectName", required = false) String objectName) {
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFileSize(upload.getSize());
        String contentType = upload.getContentType();
        if (contentType.contains("image")) {
            uploadFileParamsDto.setFileType("001001");
        } else {
            uploadFileParamsDto.setFileType("001003");
        }
        uploadFileParamsDto.setRemark("");
        uploadFileParamsDto.setFilename(upload.getOriginalFilename());
        uploadFileParamsDto.setContentType(contentType);
        Long companyId = 1232141425L;
        try {
            UploadFileResultDto uploadFileResultDto = mediaFileService.uploadFile(companyId, uploadFileParamsDto, upload.getBytes(), folder, objectName);
            return uploadFileResultDto;
        } catch (IOException e) {
            EduxException.cast("上传文件过程出错:" + e.getMessage());
        }
        return null;
    }


    @GetMapping("/preview/{mediaId}")
    public R<String> getPlayUrlByMediaId(@PathVariable String mediaId) {
        MediaFiles mediaFile = mediaFileService.getFileById(mediaId);
        return R.success(mediaFile.getUrl());
    }
}
