package com.km.voide;

import java.net.*;
import java.io.*;

import javax.swing.*;

import javax.swing.JOptionPane;

public class S extends JFrame
{
	public S() 
	{
		try
		{
			ServerSocket serverSocket=new ServerSocket(9999);
			System.out.println("服务器正在监听……………… ");
			Socket socket=serverSocket.accept();
			
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			
			System.out.println("一个客户端建立连接");
			
			while(true)
			{
				String content=dis.readUTF();
				System.out.println("客户端对服务器说：  "+content);
			}

		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}

	public static void main(String [] Args)
	{
		S form=new S();
	}
}
