package com.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

import com.km.task.entity.Task;


/**
 * 数据库相关操作
 * 
 * @author 叶中奇
 * 
 */
public abstract class DbUtils {

	private static long delta = 0;

	private static long updateTime = 0;

	private static Thread updateTimeThread = null;

	/**
	 * 对象转成String
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String obj2String(Serializable obj) throws IOException {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			return new String(Base64.encodeBase64(baos.toByteArray()));
		} finally {
			if (oos != null)
				oos.close();
			if (baos != null)
				baos.close();
		}
	}

	/**
	 * String转成对象
	 * 
	 * @param s
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static Object string2Obj(String s) throws IOException, SQLException,
			ClassNotFoundException {
		ObjectInputStream ois = null;
		ByteArrayInputStream bis = null;
		try {
			bis = new ByteArrayInputStream(Base64.decodeBase64(s.getBytes()));
			ois = new ObjectInputStream(bis);
			return ois.readObject();
		} finally {
			if (ois != null)
				ois.close();
			if (bis != null)
				bis.close();
		}
	}

}
