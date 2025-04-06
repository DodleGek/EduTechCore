package com.sspu;


import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BigFileTest {


    @Test
    // 大文件分块
    public void testChunk() throws IOException {
        // 原始文件
        File source = new File("D:\\test\\1.mp4");
        // 分块文件存放路径
        String chunkFolder = "D:\\test\\chunk\\";

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
    public void testMerge() {

    }

}
