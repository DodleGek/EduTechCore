package com.sspu.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mp4VideoUtil extends VideoUtil {

    String ffmpeg_path = "/opt/homebrew/bin/ffmpeg";
    String video_path;
    String mp4_name;
    String mp4folder_path;

    public Mp4VideoUtil(String ffmpeg_path, String video_path, String mp4_name, String mp4folder_path) {
        super(ffmpeg_path);
        this.ffmpeg_path = ffmpeg_path;
        this.video_path = video_path;
        this.mp4_name = mp4_name;
        this.mp4folder_path = mp4folder_path;
    }

    // 清除已生成的mp4
    private void clear_mp4(String mp4_path) {
        File mp4File = new File(mp4_path);
        if (mp4File.exists() && mp4File.isFile()) {
            mp4File.delete();
        }
    }

    /**
     * 视频编码，生成mp4文件
     *
     * @return 成功返回success，失败返回控制台日志
     */
    public String generateMp4() {
        // 构建完整的输出文件路径
        String outputFilePath = mp4folder_path + mp4_name;

        // 清除已生成的mp4
        clear_mp4(outputFilePath);

        List<String> command = new ArrayList<>();
        command.add(ffmpeg_path);
        command.add("-i");
        command.add(video_path);
        command.add("-c:v");
        command.add("libx264");
        command.add("-y"); // 覆盖输出文件
        command.add("-s");
        command.add("1280x720");
        command.add("-pix_fmt");
        command.add("yuv420p");
        command.add("-b:a");
        command.add("63k");
        command.add("-b:v");
        command.add("753k");
        command.add("-r");
        command.add("18");
        command.add(outputFilePath);

        String outstring = null;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(command);
            // 将标准输入流和错误输入流合并
            builder.redirectErrorStream(true);
            Process p = builder.start();
            outstring = waitFor(p);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 检查视频时长是否一致
        Boolean check_video_time = this.check_video_time(video_path, outputFilePath);
        if (!check_video_time) {
            return outstring;
        } else {
            return "success";
        }
    }

    public static void main(String[] args) throws IOException {
        // 测试示例
        String ffmpeg_path = "/opt/homebrew/bin/ffmpeg";
        String video_path = "/Users/syw/Desktop/output.avi";
        String mp4_name = "视频1_转换.mp4";
        String mp4folder_path = "/Users/syw/Desktop/";

        Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4folder_path);
        String result = videoUtil.generateMp4();
        System.out.println(result);
    }
}