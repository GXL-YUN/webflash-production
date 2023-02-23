package com.km.voide;

public class User {
	public static void main(String[] args) {
		
		
		System.out.println();
		Client client2 = new Client("127.0.0.1", 18888);
		client2.receiveMsg();
		client2.up("11111111111111111111111"+"\r\n");
	}
}
