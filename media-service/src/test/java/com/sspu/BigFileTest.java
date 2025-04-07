package com.sspu;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BigFileTest {


    @Test
    // 大文件分块
    public void testChunk() throws IOException {
        // 原始文件
        File source = new File("/Users/syw/Desktop/视频1.mp4");
        // 分块文件存放路径
        String chunkFolder = "/Users/syw/Desktop/chunk/";

        int chunkSize = 1024 * 1024; // 分块大小 1M
        // 分块数量
        int chunkNum = (int) Math.ceil(source.length() * 1.0 / chunkSize);

        // 缓冲区
        byte[] b = new byte[chunkSize];

        // 使用流读取文件，向分块文件写入数据
        try (RandomAccessFile raf = new RandomAccessFile(source, "r")) {
            for (int i = 0; i < chunkNum; i++) {
                File chunkFile = new File(chunkFolder + i);
                // 分块文件写入流
                try (RandomAccessFile raf_chunk = new RandomAccessFile(chunkFile, "rw")) {
                    int bytesRead = 0;
                    int totalBytesRead = 0;
                    while (totalBytesRead < chunkSize && (bytesRead = raf.read(b)) != -1) {
                        raf_chunk.write(b, 0, bytesRead);
                        totalBytesRead += bytesRead;
                    }
                }
            }
        }
    }


    @Test
    // 分块合并
    public void testMerge() throws IOException {
        // 块文件存放路径
        File chunkFolder = new File("/Users/syw/Desktop/chunk/");
        // 源文件
        File sourceFile = new File("/Users/syw/Desktop/视频1.mp4");
        // 合并后的文件
        File mergedFile = new File("/Users/syw/Desktop/merged.mp4");

        // 确保文件存在
        if(!chunkFolder.exists()) {
            System.out.println("文件块目录不存在，请检查路径");
            return;
        }

        // 获取并排序文件列表
        File[] chunkFilesArray = chunkFolder.listFiles();
        if(chunkFilesArray == null || chunkFilesArray.length == 0) {
            System.out.println("没有找到分块文件");
            return;
        }

        List<File> chunkFiles = Arrays.asList(chunkFilesArray);
        chunkFiles.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getName())));

        // 使用 RandomAccessFile 执行合并操作
        try (RandomAccessFile raf_write = new RandomAccessFile(mergedFile, "rw")) {
            // 缓冲区
            byte[] buffer = new byte[1024 * 1024];

            // 遍历排序后的分块文件
            for (File chunkFile : chunkFiles) {
                try (RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r")) {
                    int len;
                    while ((len = raf_read.read(buffer)) != -1) {
                        raf_write.write(buffer, 0, len);
                    }
                }
            }
        }

        System.out.println("文件合并完成，保存路径：" + mergedFile.getAbsolutePath());

        // 计算MD5并比较
        String originalMd5;
        try (FileInputStream fis1 = new FileInputStream(sourceFile)) {
            originalMd5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis1);
        }

        String mergedMd5;
        try (FileInputStream fis2 = new FileInputStream(mergedFile)) {
            mergedMd5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis2);
        }

        boolean isIdentical = originalMd5.equals(mergedMd5);
        System.out.println("原始文件MD5: " + originalMd5);
        System.out.println("合并文件MD5: " + mergedMd5);
        System.out.println("MD5比较结果: " + (isIdentical ? "一致" : "不一致"));
    }

}
