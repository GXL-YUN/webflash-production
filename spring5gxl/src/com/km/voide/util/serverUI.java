package com.km.voide.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.IOException;

import javax.swing.JFrame;

/**
 * 服务器端搭建   
 * 实现视频接受数据
 * @author Lenovo
 *
 */
public class serverUI extends JFrame{
    Graphics g;
	public void setUI() {
		this.setTitle("服务器界面");
		this.setSize(new Dimension(600,600));
		this.setVisible(true);
		this.setDefaultCloseOperation(3);
		g = this.getGraphics();
				
		//服务器通信板块，端口9999
		serverConn serverconn = new serverConn(g);
		serverconn.setServer(9999);
		
		serverconn.recVideo();
	}
	
	
	
	public void drawVideo(int[][] video) {
		for (int i=0;i<video.length;i++) {
			for (int j=0;j<video[0].length;j++) {
				Color color = new Color(video[i][j]);
				g.setColor(color);
				g.drawLine(i, j, i, j);
				
			}
		}
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        serverUI s = new serverUI();
        s.setUI();
	}

}
