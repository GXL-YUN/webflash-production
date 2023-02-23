package com.km.voide;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
	private Socket socket;
    private InputStream inputStream;
    public Client(String ip, int port){
        try {
            this.socket = new Socket(ip,port);
            this.inputStream = socket.getInputStream();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 封装客户端传输到服务端数据
     */
    public void up(String massage) {
    	
    	//获取输出流数据
    	try {
    		DataOutputStream pw=new DataOutputStream(socket.getOutputStream());
    		InputStreamReader ir=new InputStreamReader(System.in);
			BufferedReader bufferedReader=new BufferedReader(ir);
			
			while(true)
			{
				String content=bufferedReader.readLine();
				if(content=="break") break;
				pw.writeUTF(content);
				pw.flush();
			}
			
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	
    }
    
    /**
     * 封装读取信息方法
     */
    public void receiveMsg(){
        int perWord = 0;
        while (true){
            try {
                perWord = this.inputStream.read();
                if (perWord == -1)
                    break;
                System.out.print((char) perWord);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}