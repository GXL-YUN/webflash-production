package com.km.rtmp.util;

import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameGrabber.Exception;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;

public class SeeVodeUtil {
	public static void main(String[] args) throws Exception {
		String file = "rtsp://39.103.237.224/554/test";
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(file);
        grabber.setOption("rtsp_transport", "tcp"); // 使用tcp的方式，不然会丢包很严重
        // 一直报错的原因！！！就是因为是 2560 * 1440的太大了。。
        grabber.setImageWidth(960);
        grabber.setImageHeight(540);
        System.out.println("grabber start");
        grabber.start();
        CanvasFrame canvasFrame = new CanvasFrame("sdsaf");
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasFrame.setAlwaysOnTop(true);
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
       // OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        while (true){
            Frame frame = grabber.grabImage();
             Mat mat = converter.convertToMat(frame);
            canvasFrame.showImage(frame);
        }

	}

}
