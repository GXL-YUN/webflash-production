package com.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	public static void zip(File[] src,File tar) {
		try {
			FileOutputStream fos = new FileOutputStream(tar);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for(int i=0; i<src.length; ++i) {
				String fileName = src[i].getName();//获得文件名
				FileInputStream fis = new FileInputStream(src[i]);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len = -1;
				byte[] buf = new byte[1024];
				while((len = fis.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
				baos.close();
				fis.close();
				byte[] fileContent = baos.toByteArray();//获得文件内容
				ZipEntry zipEntry = new ZipEntry(fileName);//利用文件名创建条目
				zos.putNextEntry(zipEntry);//插入条目
				zos.write(fileContent);//写入条目内容
				zos.closeEntry();//关闭条目
			}
			zos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void unZip(File zipFile, File path) {
		try {
			if(!path.exists()) {
				path.mkdirs();
			}
			FileInputStream fis = new FileInputStream(zipFile);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry zipEntry = null;
			while((zipEntry = zis.getNextEntry()) != null) {//获取条目
				String fileName = zipEntry.getName();//获取文件名
				File file = new File(path.getAbsolutePath() + "/./" + fileName);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len = -1;
				byte[] buf = new byte[1024];
				while((len = zis.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
				baos.close();
				byte[] fileContent = baos.toByteArray();//获取条目内容（即文件内容）
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(fileContent);
				fos.close();
			}
			
			zis.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
