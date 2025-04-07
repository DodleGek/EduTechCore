package com.sspu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.sspu.dto.*;
import com.sspu.entity.MediaFiles;
import com.sspu.entity.MediaProcess;
import com.sspu.exception.EduxException;
import com.sspu.mapper.MediaFilesMapper;
import com.sspu.mapper.MediaProcessMapper;
import com.sspu.service.MediaFileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 媒体文件服务实现类
 */
@Slf4j
@Service
public class MediaFileServiceImpl implements MediaFileService {

    private final MediaFilesMapper mediaFilesMapper; // 媒体文件数据访问接口
    private final MediaProcessMapper mediaProcessMapper; // 媒体处理任务数据访问接口
    private final MinioClient minioClient; // MinIO客户端
    private final MediaFileService mediaFileServiceProxy; // 自身代理对象，用于事务处理

    /**
     * 构造函数，注入所需依赖
     */
    MediaFileServiceImpl(MediaFilesMapper mediaFilesMapper, MediaProcessMapper mediaProcessMapper, MinioClient minioClient) {
        this.mediaFilesMapper = mediaFilesMapper;
        this.mediaProcessMapper = mediaProcessMapper;
        this.minioClient = minioClient;
        this.mediaFileServiceProxy = this;
    }

    @Value("${minio.bucket.files}")
    private String bucket_files; // 普通文件存储桶

    @Value("${minio.bucket.videofiles}")
    private String video_files; // 视频文件存储桶

    /**
     * 根据文件名获取文件类型
     *
     * @param objectName 文件名
     * @return MIME类型字符串
     */
    private static String getContentType(String objectName) {
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (objectName.contains(".")) {
            String extension = objectName.substring(objectName.lastIndexOf("."));
            ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
            if (extensionMatch != null) {
                contentType = extensionMatch.getMimeType();
            }
        }
        return contentType;
    }

    /**
     * 上传文件
     *
     * @param companyId           机构ID
     * @param uploadFileParamsDto 上传参数
     * @param bytes               文件字节数组
     * @param folder              文件夹
     * @param objectName          对象名称
     * @return 上传结果
     */
    @Override
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName) {
        // 计算文件MD5
        String fileMD5 = DigestUtils.md5DigestAsHex(bytes);
        // 处理文件夹路径
        if (StringUtils.isEmpty(folder)) {
            folder = getFileFolder(true, true, true);
        } else if (!folder.endsWith("/")) {
            folder = folder + "/";
        }
        // 处理对象名称
        if (StringUtils.isEmpty(objectName)) {
            String filename = uploadFileParamsDto.getFilename();
            objectName = fileMD5 + filename.substring(filename.lastIndexOf("."));
        }
        objectName = folder + objectName;
        try {
            // 上传文件到MinIO
            addMediaFilesToMinIO(bytes, bucket_files, objectName);
            // 将文件信息保存到数据库
            MediaFiles mediaFiles = mediaFileServiceProxy.addMediaFilesToDB(companyId, uploadFileParamsDto, objectName, fileMD5, bucket_files);
            // 构建上传结果
            UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
            BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);
            return uploadFileResultDto;
        } catch (Exception e) {
            log.debug("上传过程中出错：{}", e.getMessage());
            EduxException.cast("上传过程中出错" + e.getMessage());
        }
        return null;
    }

    /**
     * 将文件信息添加到数据库
     */
    @Transactional
    @Override
    public MediaFiles addMediaFilesToDB(Long companyId, UploadFileParamsDto uploadFileParamsDto, String objectName, String fileMD5, String bucket) {
        String contentType = getContentType(objectName);
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMD5);
        if (mediaFiles == null) {
            mediaFiles = new MediaFiles();
            BeanUtils.copyProperties(uploadFileParamsDto, mediaFiles);
            // 设置文件基本信息
            mediaFiles.setId(fileMD5);
            mediaFiles.setFileId(fileMD5);
            mediaFiles.setCompanyId(companyId);
            mediaFiles.setBucket(bucket);
            mediaFiles.setCreateDate(LocalDateTime.now());
            mediaFiles.setStatus("1");
            mediaFiles.setFilePath(objectName);
            // 如果是图片或MP4视频，设置访问地址
            if (contentType.contains("image") || contentType.contains("mp4")) {
                mediaFiles.setUrl("/" + bucket + "/" + objectName);
            }
            mediaFiles.setAuditStatus("002003");
        }
        // 保存文件信息到数据库
        int insert = mediaFilesMapper.insert(mediaFiles);
        if (insert <= 0) {
            EduxException.cast("保存文件信息失败");
        }
        // 如果是AVI视频，添加到待处理任务
        if ("video/x-msvideo".equals(contentType)) {
            MediaProcess mediaProcess = new MediaProcess();
            BeanUtils.copyProperties(mediaFiles, mediaProcess);
            mediaProcess.setStatus("1");
            int processInsert = mediaProcessMapper.insert(mediaProcess);
            if (processInsert <= 0) {
                EduxException.cast("保存avi视频到待处理表失败");
            }
        }
        return mediaFiles;
    }

    /**
     * 检查文件是否存在
     */
    @Override
    public R<Boolean> checkFile(String fileMd5) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if (mediaFiles == null) {
            return R.success(false);
        }
        try (InputStream inputStream = minioClient.getObject(GetObjectArgs
                .builder()
                .bucket(mediaFiles.getBucket())
                .object(mediaFiles.getFilePath())
                .build())) {
            return R.success(inputStream != null);
        } catch (Exception e) {
            return R.success(false);
        }
    }

    /**
     * 检查分块是否存在
     */
    @Override
    public R<Boolean> checkChunk(String fileMd5, int chunkIndex) {
        String chunkFilePath = getChunkFileFolderPath(fileMd5) + chunkIndex;
        try (InputStream inputStream = minioClient.getObject(GetObjectArgs
                .builder()
                .bucket(video_files)
                .object(chunkFilePath)
                .build())) {
            return R.success(inputStream != null);
        } catch (Exception e) {
            return R.success(false);
        }
    }

    /**
     * 上传分块
     */
    @Override
    public R<Boolean> uploadChunk(String fileMd5, int chunk, byte[] bytes) {
        String chunkFilePath = getChunkFileFolderPath(fileMd5) + chunk;
        try {
            addMediaFilesToMinIO(bytes, video_files, chunkFilePath);
            return R.success(true);
        } catch (Exception e) {
            log.debug("上传分块文件：{}失败：{}", chunkFilePath, e.getMessage());
        }
        return R.fail("上传文件失败", false);
    }

    /**
     * 合并分块
     */
    @Override
    public R<Boolean> mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
        File[] chunkFiles = checkChunkStatus(fileMd5, chunkTotal);
        String fileName = uploadFileParamsDto.getFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        File mergeFile = null;
        try {
            mergeFile = Files.createTempFile(fileName, extension).toFile();
        } catch (IOException e) {
            EduxException.cast("创建合并临时文件出错");
        }
        // 合并文件
        try (OutputStream os = Files.newOutputStream(mergeFile.toPath())) {
            for (File chunkFile : chunkFiles) {
                try (InputStream is = Files.newInputStream(chunkFile.toPath())) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (Exception e) {
            EduxException.cast("合并文件过程中出错");
        }

        // 校验合并后的文件
        uploadFileParamsDto.setFileSize(mergeFile.length());
        try (FileInputStream mergeInputStream = new FileInputStream(mergeFile)) {
            String mergeMd5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(mergeInputStream);
            if (!fileMd5.equals(mergeMd5)) {
                EduxException.cast("合并文件校验失败");
            }
        } catch (Exception e) {
            EduxException.cast("合并文件校验异常");
        }

        // 将合并后的文件上传到MinIO
        String mergeFilePath = getFilePathByMd5(fileMd5, extension);
        addMediaFilesToMinIO(mergeFile.getAbsolutePath(), video_files, mergeFilePath);
        MediaFiles mediaFiles = addMediaFilesToDB(companyId, uploadFileParamsDto, mergeFilePath, fileMd5, video_files);
        if (mediaFiles == null) {
            EduxException.cast("媒资文件入库出错");
        }

        // 清理临时文件
        for (File chunkFile : chunkFiles) {
            try {
                chunkFile.delete();
            } catch (Exception e) {
                log.debug("临时分块文件删除错误：{}", e.getMessage());
            }
        }
        try {
            mergeFile.delete();
        } catch (Exception e) {
            log.debug("临时合并文件删除错误：{}", e.getMessage());
        }
        return R.success();
    }

    /**
     * 根据文件ID获取文件信息
     */
    @Override
    public MediaFiles getFileById(String id) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(id);
        if (mediaFiles == null || StringUtils.isEmpty(mediaFiles.getUrl())) {
            EduxException.cast("视频还没有转码处理");
        }
        return mediaFiles;
    }

    /**
     * 根据MD5生成文件路径
     */
    public String getFilePathByMd5(String fileMd5, String extension) {
        return fileMd5.charAt(0) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + extension;
    }

    /**
     * 从本地上传文件到MinIO
     */
    public void addMediaFilesToMinIO(String filePath, String bucket, String objectName) {
        String contentType = getContentType(objectName);
        try (InputStream inputStream = new FileInputStream(filePath)) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            EduxException.cast("上传到文件系统出错:" + e.getMessage());
        }
    }

    /**
     * 检查分块状态并下���
     */
    private File[] checkChunkStatus(String fileMd5, int chunkTotal) {
        File[] files = new File[chunkTotal];
        String chunkFileFolder = getChunkFileFolderPath(fileMd5);
        for (int i = 0; i < chunkTotal; i++) {
            String chunkFilePath = chunkFileFolder + i;
            File chunkFile = null;
            try {
                chunkFile = Files.createTempFile("chunk" + i, null).toFile();
            } catch (IOException e) {
                EduxException.cast("创建临时分块文件出错：" + e.getMessage());
            }
            chunkFile = downloadFileFromMinio(chunkFile, video_files, chunkFilePath);
            files[i] = chunkFile;
        }
        return files;
    }

    /**
     * 从MinIO下载文件
     */
    public File downloadFileFromMinio(File file, String bucket, String objectName) {
        try (InputStream inputStream = minioClient.getObject(GetObjectArgs
                .builder()
                .bucket(bucket)
                .object(objectName)
                .build());
             OutputStream outputStream = Files.newOutputStream(file.toPath(), StandardOpenOption.CREATE)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return file;
        } catch (Exception e) {
            EduxException.cast("查询文件分块出错");
        }
        return null;
    }

    /**
     * 获取分块文件存储路径
     */
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + "chunk" + "/";
    }

    /**
     * 上传文件到MinIO
     */
    public void addMediaFilesToMinIO(byte[] bytes, String bucket, String objectName) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        String contentType = getContentType(objectName);
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(byteArrayInputStream, byteArrayInputStream.available(), -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            log.debug("上传到文件系统出错:{}", e.getMessage());
            throw new EduxException("上传到文件系统出错");
        }
    }

    /**
     * 获取文件存储目录
     */
    private String getFileFolder(boolean year, boolean month, boolean day) {
        StringBuilder stringBuffer = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(new Date());
        String[] split = dateString.split("-");
        if (year) {
            stringBuffer.append(split[0]).append("/");
        }
        if (month) {
            stringBuffer.append(split[1]).append("/");
        }
        if (day) {
            stringBuffer.append(split[2]).append("/");
        }
        return stringBuffer.toString();
    }

    /**
     * 分页查询媒体文件
     */
    @Override
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(queryMediaParamsDto.getFilename()), MediaFiles::getFilename, queryMediaParamsDto.getFilename());
        queryWrapper.eq(!StringUtils.isEmpty(queryMediaParamsDto.getFileType()), MediaFiles::getFileType, queryMediaParamsDto.getFileType());
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
        List<MediaFiles> list = pageResult.getRecords();
        long total = pageResult.getTotal();
        return new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
    }
}