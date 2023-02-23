package com.km.voide.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;

public class clientConn {
	java.io.OutputStream out;
	java.io.InputStream in;
	
	
	public void conn() {
		try {
			
			//创建客户端对象并连接服务器
			java.net.Socket client = new java.net.Socket("192.168.0.109",9999);
			//初始化输入输出流
			in = client.getInputStream();
			out = client.getOutputStream();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("程序出错");	
		}
		
		
	}
	
	
	public void sendVideo(BufferedImage image) {
		
			DataOutputStream dout = new DataOutputStream(out);
			
			//用int发送视频
			
				try {
					//先发送长度，再发数据
					dout.writeInt(image.getWidth());
					dout.writeInt(image.getHeight());
					for(int i=0 ; i<image.getWidth();i++) {
						for(int j=0 ; j<image.getHeight();j++) {
								dout.writeInt(image.getRGB(i, j));
								
						}	
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
										
	}

}

