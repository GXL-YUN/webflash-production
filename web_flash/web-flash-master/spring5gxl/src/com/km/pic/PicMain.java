package com.km.pic;

import java.io.InputStream;

import com.util.ImageUtils;

public class PicMain {
	
	public static void main(String[] args) {
		
		
		//根据图片地址获取图片流数据
		InputStream readImage = ImageUtils.readImage("D:\\datas\\weixin\\WeChat Files\\wxid_wc5abg3o0ikt22\\FileStorage\\File\\2022-02\\远大Oracle集成\\泛微oa开发\\泛微oa开发\\Oracle对接\\页面截图\\付款单.png");
		
		String im = ImageUtils.imageBase64(readImage);
		String imageBase64 = ImageUtils.imageBase64("D:\\datas\\weixin\\WeChat Files\\wxid_wc5abg3o0ikt22\\FileStorage\\File\\2022-02\\远大Oracle集成\\泛微oa开发\\泛微oa开发\\Oracle对接\\页面截图\\付款单.png");
		
		
		
		System.out.println(imageBase64);
		
		
		
	}

}
