package com.wisdom.course.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author WisdomCR
 * @since 2022/11/16
 */
@Component
public class getUrlTime {
    @Value("${ffmpeg}")
    private String ffmpegPath;

    public Long mp4(String url) throws IOException {
        String filePath = new File("").getCanonicalPath();// 获取项目根目录
        //ffmpeg命令位置
        String ffmpeg_path = filePath + ffmpegPath;
        //视频url地址
        String video_path = url;
        //拼接系统命令
        List<String> commands = new ArrayList<String>();
        commands.add(ffmpeg_path);
        commands.add("-i");
        commands.add(video_path);
        //执行系统命令
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commands);
        final Process p = builder.start();
        //从输入流中读取视频信息
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        StringBuffer sb = new StringBuffer();
        String line = "";
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        //从视频信息中解析时长
        String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
        Pattern pattern = Pattern.compile(regexDuration);
        Matcher m = pattern.matcher(sb.toString());
        if (m.find()) {// 该视频url是否有效
            String time = m.group(1).substring(0, m.group(1).length() - 3);
            String[] my = time.split(":");
            int hour = Integer.parseInt(my[0]);
            int min = Integer.parseInt(my[1]);
            int sec = Integer.parseInt(my[2]);
            long totalSec = hour * 3600 + min * 60 + sec;// 视频时长
            return totalSec;
        }
        return null;
    }
}
