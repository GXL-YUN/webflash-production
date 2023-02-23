package com.km.voide.util;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;

/**
 * 客户端实现
 * 视频发送数据
 * @author Lenovo
 *
 */
public class clientUI extends javax.swing.JFrame{
	//获取客户端画布
			Graphics g;
	
	public void setUI() {
		this.setTitle("客户端视频");
		this.setSize(new Dimension(600,600));
		this.setVisible(true);
		this.setDefaultCloseOperation(3);
	    g = this.getGraphics();
		//打开网络连接模块
		clientConn clientconn = new clientConn();
		clientconn.conn();
		
		setWebcam(clientconn);
		
	}
	
	//获取摄像头权限并获得图片
	public void setWebcam(clientConn clientconn) {
		// get default webcam and open it获取网络摄像头设置并打开
		Webcam webcam = Webcam.getDefault();
		webcam.open();
       
		while(true) {
		// get image获取图片
		BufferedImage image = webcam.getImage();
		drawImage(g,image);
		//写入服务器  推送
		clientconn.sendVideo(image);
		try {
			
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	public void drawImage(Graphics g,BufferedImage image) {
		g.drawImage(image,300,300,null);		
	}
	//返回
	public BufferedImage drawImage(BufferedImage image) {
		return image;		
	}
	public static void main(String[] args) {
		clientUI c = new clientUI();
		c.setUI();
	}
}

