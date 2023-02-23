package com.km.rtmp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.km.rtmp.bean.BStreamer;

public class FFmpegPusher extends BStreamer {
    public FFmpegPusher(String url) {
        super(url);
    }

    public FFmpegPusher(String url, int w, int h) {
        super(url, w, h);
    }
    private Process process;

    public void push(){
        String basePath="C:\\Users\\Lenovo\\Desktop\\ui\\rtmp推流\\";
        String videoPath=String.format("%s\\test.avi",basePath);
        String ffmpegPath=String.format("%s\\ffmpeg",basePath);
       try {
            // 视频切换时，先销毁进程，全局变量Process process，方便进程销毁重启，即切换推流视频
            if(process != null){
                process.destroy();
                System.out.println(">>>>>>>>>>推流视频切换<<<<<<<<<<");
            }
           // ffmpeg开头，-re代表按照帧率发送，在推流时必须有
           // 指定要推送的视频
           // 指定推送服务器，-f：指定格式
           String command =String.format("%s -re -i %s -f rtsp %s",
                   ffmpegPath,videoPath,getUrl());
            System.out.println("ffmpeg推流命令：" + command);
            // 运行cmd命令，获取其进程
            process = Runtime.getRuntime().exec(command);
            // 输出ffmpeg推流日志
            BufferedReader br= new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println("视频推流信息[" + line + "]");
            }
            process.waitFor();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    


        public static void main(String[] args) throws Exception {
            String url="rtsp://192.168.88.81:554/test";
            FFmpegPusher pusher=new FFmpegPusher(url);
            pusher.push();
    }

    
    
}
