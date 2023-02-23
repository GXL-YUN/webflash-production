package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.mysql.jdbc.ConnectionImpl;

public class FileInsert {
	
	/**
	 * 获取本地文件，读取本地文件里的kbase语句
	 * 递归进行文件数据的存储
	 */
    public  void bulklod(String  str) throws Exception,FileNotFoundException {
        // 获取目标服务器 kbase path
        try{
                String inputPath = str;
                File file = new File(inputPath);      //获取其file对象
                File[] fs = file.listFiles();     //遍历path下的文件和目录，放在File数组中

                for(File f:fs){                //遍历File[]数组
                    //判断是否是文件或者文件夹
                    if (!f.isDirectory()) {  //另外可用fileName.endsWith("txt")来过滤出以txt结尾的文件
                    	String fileName = f.getName();  //获取文件和目录名
                    	//进行数据库的操作   存储文件名称   和文件路径   文件类型     数据格式      备注信息   
                    	//做成一个   定时任务       后台配置  单独一个数据页面   进行数据文件存储路径的配置 和数据文化   
                    	//截取后缀作为数据的类型
                    	String type=fileName;
                    	System.out.println("文件名称"+fileName+"：路径"+f);
                    }else {
                    	//文件夹数据
                    	bulklod(f.getPath());
                    }
                }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //new KbaseConnection().release(conn);
        }

    }

    public static void main(String[] args) throws Exception{
        new FileInsert().bulklod("D:\\tools");
    }


}
