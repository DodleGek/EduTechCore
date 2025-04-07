package com.sspu.controller;

import com.sspu.dto.R;
import com.sspu.dto.UploadFileParamsDto;
import com.sspu.service.MediaFileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 大文件上传控制器
 * 处理文件分块上传的相关接口
 */
@RestController
public class BigFilesController {

    private final MediaFileService mediaFileService;

    private BigFilesController(MediaFileService mediaFileService) {
        this.mediaFileService = mediaFileService;
    }

    /**
     * 上传文件分块
     *
     * @param file    分块文件
     * @param fileMd5 文件MD5值
     * @param chunk   分块序号
     * @return 上传结果
     */
    @PostMapping("/upload/uploadchunk")
    public R<Boolean> uploadChunk(@RequestParam("file") MultipartFile file, @RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") int chunk) throws Exception {
        return mediaFileService.uploadChunk(fileMd5, chunk, file.getBytes());
    }

    /**
     * 检查文件是否存在
     *
     * @param fileMd5 文件MD5值
     * @return 检查结果
     */
    @PostMapping("/upload/checkfile")
    public R<Boolean> checkFile(@RequestParam("fileMd5") String fileMd5) {
        return mediaFileService.checkFile(fileMd5);
    }

    /**
     * 检查分块是否存在
     *
     * @param fileMd5 文件MD5值
     * @param chunk   分块序号
     * @return 检查结果
     */
    @PostMapping("/upload/checkchunk")
    public R<Boolean> checkChunk(@RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") int chunk) {
        return mediaFileService.checkChunk(fileMd5, chunk);
    }

    /**
     * 合并文件分块
     *
     * @param fileMd5    文件MD5值
     * @param fileName   文件名
     * @param chunkTotal 分块总数
     * @return 合并结果
     */
    @PostMapping("/upload/mergechunks")
    public R<Boolean> mergeChunks(@RequestParam("fileMd5") String fileMd5, @RequestParam("fileName") String fileName, @RequestParam("chunkTotal") int chunkTotal) throws IOException {
        Long companyId = 1232141425L;
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFileType("001002");    // 设置文件类型为视频
        uploadFileParamsDto.setTags("课程视频");       // 设置文件标签
        uploadFileParamsDto.setRemark("");            // 设置备注
        uploadFileParamsDto.setFilename(fileName);    // 设置文件名
        return mediaFileService.mergeChunks(companyId, fileMd5, chunkTotal, uploadFileParamsDto);
    }
}