package com.km.voide;

import java.net.*;
import java.io.*;

import javax.swing.*;

import javax.swing.JOptionPane;

public class C extends JFrame
{
	public C()
	{
		try
		{
			System.out.println("客户端正在发送……………………");
			Socket socket=new Socket("127.0.0.1",9999);
			DataOutputStream pw=new DataOutputStream(socket.getOutputStream());

			InputStreamReader sr=new InputStreamReader(socket.getInputStream());
			BufferedReader br=new BufferedReader(sr);
					
			InputStreamReader ir=new InputStreamReader(System.in);
			BufferedReader bufferedReader=new BufferedReader(ir);
			
			while(true)
			{
				String content=bufferedReader.readLine();
				if(content=="break") break;
				pw.writeUTF(content);
				pw.flush();
			}
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}
	public static void main(String [] Args)
	{
		C form=new C();
	}
	
}