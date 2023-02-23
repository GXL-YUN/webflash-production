package com.km.voide;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.util.PortUitl;

public class Server {
	static int index = 0;

	public static void main(String[] args) {
		
		Socket socket;
		try {
			
			PortUitl.kill("18888");
			ServerSocket serverSocket = new ServerSocket(18888);// 需要捕捉异常，好像涉及到网络连接就会强制要求
			System.out.println("等待连接。。。");
			// 持续保持等待连接状态
			while (true) {
				// 获取Socket对象
				
				socket = serverSocket.accept();// 阻塞式连接，没有连接就不会执行后续代码
				System.out.println("连接成功~");
				Server.getClientMsg(socket);
				//服务器端发送数据
				Server.setm(socket, "holler word  wode shuijia   nide nme hdsj fdsjhf sjfv cx llsd vjx sjeoif jx bmdhs lpw bfzksba ppjnd ");
				
				
				try {
					 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
	                    //Log.e("Client","完毕");
	                    String s=in.readLine();
				   	    System.out.println("ss"+s);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//获取客户端数据  
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 读取流
	 * 
	 * @param inStream
	 * @return 字节数组
	 * @throws IOException 
	 * @throws Exception
	 */
	private static void getClientMsg(Socket socket) throws IOException {

		DataInputStream dis = new DataInputStream(socket.getInputStream());
		
		System.out.println("一个客户端建立连接");
		
		while(true)
		{
			String content=dis.readUTF();
			System.out.println("客户端对服务器说：  "+content);
		}
	    }
	public static void setm(Socket socket, String massage) {
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
			outputStream.write(new String(massage).getBytes(StandardCharsets.UTF_8));
			//outputStream.close();// 关闭输出流，表明输出结束
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getm(Socket socket) {
		int perWord = 0;
		while (true) {
			try {
				perWord = socket.getInputStream().read();
				if (perWord == -1)
					break;
				System.out.print((char) perWord);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
