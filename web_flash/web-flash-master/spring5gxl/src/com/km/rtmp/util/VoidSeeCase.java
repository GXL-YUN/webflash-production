package com.km.rtmp.util;

import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

public class VoidSeeCase {
	
	public static void testzc() throws FrameGrabber.Exception
	{
		String rtsp = "rtsp://39.103.237.224/347014.sdp";
 
		FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtsp);
		grabber.setOption("rtsp_transport", "tcp"); // 使用tcp的方式，不然会丢包很严重
		grabber.setImageWidth(960);
		grabber.setImageHeight(540);
		grabber.start();
 
		CanvasFrame canvasFrame = new CanvasFrame("正茂");// 创建窗口
		canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置窗口关闭程序是否也跟随关闭
		canvasFrame.setAlwaysOnTop(true);
 
		OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
		int ex = 0;
		while (true)
		{
			Frame frame = grabber.grabImage();
			canvasFrame.showImage(frame);
            //程序到这里其实已经实现了预览的功能了，下面的方法就是将流保存成图片
 
 
			//opencv_core.Mat
			Mat mat = converter.convertToMat(frame);
			opencv_imgcodecs.imwrite("E:\\tp\\" + ex + ".png", mat);
			
			ex++;
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			} // 1000毫秒刷新一次圖像
		}
 
	}
	public static void main(String[] args)
	{
		try
		{
			testzc();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
