package com.km.rtmp.util;
 
import java.util.HashMap;
import java.util.Map;
 
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
 
public class Rtsp2RtmpPacket {
 
    /**
     * 此处应用场景为多个摄像头rtsp流的读取
     */
    public static Map<String, Rtsp2RtmpPacket> PACKETS = new HashMap<>();
 
    public boolean exit = false;
    private FFmpegFrameGrabber grabber;
    private FFmpegFrameRecorder recorder;
 
    public static void getStart(String sessionId, String rtspUrl, String rtmpUrl) {
        getStop(sessionId);
        Rtsp2RtmpPacket packet = new Rtsp2RtmpPacket();
        PACKETS.put(sessionId, packet);
        new Thread(new Runnable() {
            @Override
            public void run() {
                packet.push(rtspUrl, rtmpUrl);
            }
        }).start();
    }
 
    public static void getStop(String sessionId) {
        try {
            Rtsp2RtmpPacket rtmpPacket = PACKETS.get(sessionId);
            if (rtmpPacket != null) {
                rtmpPacket.setExit(true);
                PACKETS.remove(sessionId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public void push(String rtspUrl, String rtmpUrl) {
        try {
            int width = 1600, height = 900;
            grabber = FFmpegFrameGrabber.createDefault(rtspUrl);
            grabber.setOption("rtsp_transport", "tcp"); // tcp方式防止丢包
            grabber.setImageWidth(width);
            grabber.setImageHeight(height);
            grabber.start();
            recorder = new FFmpegFrameRecorder(rtmpUrl, width, height, grabber.getAudioChannels());
            recorder.setInterleaved(true);
            recorder.setVideoOption("tune", "zerolatency"); // 降低编码延时
            recorder.setVideoOption("preset", "ultrafast"); // 提升编码速度
            recorder.setVideoOption("crf", "28"); // 视频质量参数(详见 https://trac.ffmpeg.org/wiki/Encode/H.264)
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("flv"); // 封装格式flv rtmp使用
            recorder.setVideoBitrate(2000000);
            recorder.setFrameRate(25); // 视频帧率(保证视频质量的情况下最低25，低于25会出现闪屏)
            recorder.setPixelFormat(0);
            recorder.setAudioQuality(0);// 最高质量
            recorder.setAudioBitrate(192000);// 音频比特率
            recorder.setSampleRate(44100);// 音频采样率
            recorder.setAudioChannels(grabber.getAudioChannels());// 双通道(立体声) 2（立体声）；1（单声道）；0（无音频）
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);// 音频编/解码器
            recorder.start();
            while (!exit) {
                Frame frame = grabber.grabImage();
                if (frame == null) {
                    grabber.start();
                    continue;
                }
                recorder.record(frame);
            }
 
            recorder.stop();
            recorder.release();
            grabber.stop();
            grabber.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public boolean isExit() {
        return exit;
    }
 
    public void setExit(boolean exit) {
        this.exit = exit;
    }
 
    public static void main(String[] args) {
        Rtsp2RtmpPacket.getStart("1001", "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov", "rtmp://127.0.0.1:6004/live/test");
    }
 
}