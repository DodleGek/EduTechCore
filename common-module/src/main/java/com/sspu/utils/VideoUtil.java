package com.sspu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 此文件作为视频文件处理父类，提供：
 * 1、查看视频时长
 * 2、校验两个视频的时长是否相等
 */
public class VideoUtil {

    private String ffmpeg_path = "/usr/local/bin/ffmpeg"; // macOS默认安装路径

    public VideoUtil(String ffmpeg_path) {
        this.ffmpeg_path = ffmpeg_path;
    }

    /**
     * 检查视频时间是否一致
     * @param source 源视频路径
     * @param target 目标视频路径
     * @return 时长是否一致
     */
    public Boolean check_video_time(String source, String target) {
        String source_time = get_video_time(source);
        if (source_time == null) {
            return false;
        }
        // 取出时分秒
        source_time = source_time.substring(0, source_time.lastIndexOf("."));

        String target_time = get_video_time(target);
        if (target_time == null) {
            return false;
        }
        // 取出时分秒
        target_time = target_time.substring(0, target_time.lastIndexOf("."));

        return source_time.equals(target_time);
    }

    /**
     * 获取视频时长(时：分：秒：毫秒)
     * @param video_path 视频路径
     * @return 视频时长
     */
    public String get_video_time(String video_path) {
        List<String> command = new ArrayList<>();
        command.add(ffmpeg_path);
        command.add("-i");
        command.add(video_path);

        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(command);
            // 将标准输入流和错误输入流合并，通过标准输入流程读取信息
            builder.redirectErrorStream(true);
            Process p = builder.start();
            String outstring = waitFor(p);

            int start = outstring.trim().indexOf("Duration: ");
            if (start >= 0) {
                int end = outstring.trim().indexOf(", start:");
                if (end >= 0) {
                    String time = outstring.substring(start + 10, end);
                    if (time != null && !time.isEmpty()) {
                        return time.trim();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 等待进程执行完毕并获取输出
     * @param p 进程对象
     * @return 进程输出字符串
     */
    public String waitFor(Process p) {
        InputStream in = null;
        InputStream error = null;
        StringBuilder outputString = new StringBuilder();

        try {
            in = p.getInputStream();
            error = p.getErrorStream();
            boolean finished = false;
            int maxRetry = 600; // 每次休眠1秒，最长执行时间10分钟
            int retry = 0;

            while (!finished) {
                if (retry > maxRetry) {
                    return "error";
                }
                try {
                    while (in.available() > 0) {
                        char c = (char) in.read();
                        outputString.append(c);
                        System.out.print(c);
                    }
                    while (error.available() > 0) {
                        char c = (char) error.read(); // 修正：从error流读取
                        outputString.append(c);
                        System.out.print(c);
                    }
                    // 进程未结束时调用exitValue将抛出异常
                    p.exitValue();
                    finished = true;
                } catch (IllegalThreadStateException e) {
                    Thread.sleep(1000); // 休眠1秒
                    retry++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 确保关闭输入流和错误流
            closeQuietly(in);
            closeQuietly(error);
        }
        return outputString.toString();
    }

    /**
     * 安静地关闭流
     * @param stream 要关闭的流
     */
    private void closeQuietly(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                System.out.println("关闭流失败: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String ffmpeg_path = "/opt/homebrew/bin/ffmpeg"; // macOS默认安装路径
        VideoUtil videoUtil = new VideoUtil(ffmpeg_path);
        String video_time = videoUtil.get_video_time("/Users/syw/Desktop/视频1.mp4");
        System.out.println(video_time);
    }
}