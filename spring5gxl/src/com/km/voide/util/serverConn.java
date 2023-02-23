package com.km.voide.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;

import javax.swing.JFrame;

public class serverConn {
    
	java.io.InputStream in;
	java.io.OutputStream out;
	
	Graphics g;
	
	public serverConn(Graphics g) {
		this.g = g;
	}
	//建立服务器函数,port为端口号
	public void setServer(int port) {	
		try {
			//创建服务器
			//建立服务器对象
			java.net.ServerSocket ss = new java.net.ServerSocket(port);
			System.out.println("服务器创建成功"+port);
			//等待客户端对象连接
			java.net.Socket client = ss.accept();
			System.out.println("有个客户机成功连接客户机");

			//初始化输入输出流
			in = client.getInputStream();
			out = client.getOutputStream();
			
			//数据流
			java.io.DataInputStream din = new java.io.DataInputStream(in);
				    
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("连接失败");
			
//			e.printStackTrace();
		}
	}
	
	//接受客户端传来的视频数据，并绘制
	public void recVideo() {
		DataInputStream din = new DataInputStream(in);
		while(true) {
		
		try {
			int Width = din.readInt();
			int Hight = din.readInt();
			int[][] video = new int[Width][Hight];
			for (int i=0; i<Width; i++) {
				for (int j=0; j<Hight; j++) {
					video[i][j] = din.readInt();		
				}
			}
			//画出图像
			drawVideo(video);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	}
	
	public void drawVideo(int[][] video) {
		BufferedImage buffer = new BufferedImage(video.length,video[0].length,BufferedImage.TYPE_INT_RGB);
		//获取缓存画布
		Graphics bufferg = buffer.getGraphics();
		
				
		for (int i=0;i<video.length;i++) {
			for (int j=0;j<video[0].length;j++) {
				Color color = new Color(video[i][j]);
				bufferg.setColor(color);
				bufferg.drawLine(i, j, i, j);			
			}
		}
		
		g.drawImage(buffer, 300, 300, null);
	}
	
}
