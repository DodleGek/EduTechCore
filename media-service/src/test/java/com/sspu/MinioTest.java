package com.sspu;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;

public class MinioTest {

    // 创建 Minio 客户端实例
    MinioClient minioClient = MinioClient.builder()
            .endpoint("http://129.211.26.164:19000")
            .credentials("minio", "XitXeepGAFKNmrbh")
            .build();

    @Test
    public void test_upload() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try (FileInputStream fis = new FileInputStream("C:\\Users\\syw\\Downloads\\745622456-1-208.mp4")) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("test")
                            .object("丝之歌.mp4")
                            .stream(fis, fis.available(), -1)
                            .build()
            );
            System.out.println("文件上传成功");
        }
    }

    @Test
    public void test_download() throws Exception {
        InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket("test")
                        .object("丝之歌.mp4")
                        .build()
        );
        Path targetPath = Paths.get("C:\\Users\\syw\\Downloads\\丝之歌_downloaded.mp4");
        Files.createDirectories(targetPath.getParent());
        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("文件下载成功：" + targetPath);
    }

    @Test
    public void test_delete() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket("mediafiles")
                        .object("2025/04/06/dfb525ca01ac9347c6e27c81dff3af5e.jpg")
                        .build()
        );
        System.out.println("文件删除成功");
    }

    @Test
    // 上传分块文件
    public void test_upload_chunk() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String chunkFolder = "/Users/syw/Desktop/chunk/";
        File chunkDir = new File(chunkFolder);
        if (!chunkDir.exists() || !chunkDir.isDirectory()) {
            throw new RuntimeException("分块文件夹不存在");
        }

        File[] chunkFiles = chunkDir.listFiles();
        if (chunkFiles == null || chunkFiles.length == 0) {
            throw new RuntimeException("没有找到分块文件");
        }

        // 按文件名数字顺序排序
        Arrays.sort(chunkFiles, Comparator.comparingInt(f -> Integer.parseInt(f.getName())));

        for (File chunkFile : chunkFiles) {
            try (FileInputStream fis = new FileInputStream(chunkFile)) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket("test")
                                .object("chunk/" + chunkFile.getName())
                                .stream(fis, fis.available(), -1)
                                .build()
                );
                System.out.println("分块文件上传成功：" + chunkFile.getName());
            }
        }
    }
}