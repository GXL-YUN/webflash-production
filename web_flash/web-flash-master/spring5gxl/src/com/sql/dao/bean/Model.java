package com.sql.dao.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.sun.java.swing.plaf.motif.resources.motif;

public abstract class Model {

	private static final Object Object = null;
	// 表名
	private String table;
	// 字段
	private String text;
	// 值
	private String value;
	// 主键
	private String fd_id;

	
	public String fd_start;
	
	
	
	public String getFd_start() {
		
		if(fd_start==null) {
			fd_start="1";
		}
		
		return fd_start;
	}


	public void setFd_start(String fd_start) {
		this.fd_start = fd_start;
	}


	/**
	 * 获取主键主键
	 * 
	 * @return
	 */
	public static String getFd_id() {
		
		
		return generateID(System.currentTimeMillis());
		  //获取当前时间 
		/**Date date = new Date(); 
		SimpleDateFormat dateFormat= new
		SimpleDateFormat("ddhhmmss"); 
		String string = dateFormat.format(date).toString();
		 
		String str = "abcdefghijl0123456789";
		String str1 = "0123456789";
		Random random = new Random();
		String sb ="1";
		for (int i = 0; i < 18; i++) {
			if(i/2==0) {
				int number = random.nextInt(10);
				sb=sb+(str1.charAt(number));
			}else {
				int number = random.nextInt(21);
				sb=sb+(str.toCharArray()[number]);
			}
		}
		return  sb;
		**/
	}
	
	/**
	 * 根據時間等到一個32的主鍵
	 * @param time
	 * @return
	 */
	private static String generateID(long time) {
		// TODO Auto-generated method stub
		String rtnVal = Long.toHexString(time);
		rtnVal += UUID.randomUUID();
		rtnVal = rtnVal.replaceAll("-", "");
		return rtnVal.substring(0, 32);
	}


	public static void main(String[] args) {
		System.out.println(""+Model.getFd_id());
	}

	/**
	 * 
	 * @param fd_id
	 */
	public void setFd_id(String fd_id) {
		this.fd_id = fd_id;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * 获取本类的属性名
	 * 
	 * @return
	 */
	public String getText() {
		// TODO Auto-generated method stub
		// 获取属性名称
		Field[] field = this.getClass().getDeclaredFields();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < field.length; i++) {
			field[i].setAccessible(true);
			list.add(field[i].getName());
		}
		list.sort(null);

		// System.out.println("list"+list.toString());
		String str = "";
		for (String s : list) {
			if (str == "") {
				str = s;
			} else {
				str = str + "," + s;
			}
		}
		return str + ",fd_id"+ ",fd_start";
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 获取本类属性值
	 * 
	 * @param text
	 */
	public String getValue(Object obj) {
		Field[] field = this.getClass().getDeclaredFields();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < field.length; i++) {
			field[i].setAccessible(true);
			list.add(field[i].getName());
		}
		list.sort(null);
		String str = "";
		int j = 0;
		try {
			Class cla = Class.forName(this.getClass().getName());

			for (String f : list) {
				// 首字母大写
				// System.out.println("eshi"+f.substring(0, 1).toUpperCase() +f.substring(1));
				Method method = cla.getMethod("get" + f.substring(0, 1).toUpperCase() + f.substring(1), null);
				if ("".equals(str)) {
					j++;
					str = "'" + str + method.invoke(obj) + "'";
				} else {
					if (method.invoke(obj) == null && "int".equals(field[j].getGenericType().toString())) {// "int".equals(field[1].getGenericType())&&null
						j++;
						str = str + "," + method.invoke(obj) + "";
					} else {
						j++;
						str = str + ",'" + method.invoke(obj) + "'";
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println("参数传递异常");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("str"+str);
		return str + ",'" + this.getFd_id() + "'" + ",'" + this.getFd_start() + "'";
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 模型转map类型
	 * 
	 * object 传入实例对象
	 * 
	 * @param obj
	 * @return
	 */
	public Map modeltoMap(Object obj) {
		Map map = new HashMap<String, String>();
		map.put("table", this.getTable());
		map.put("text", this.getText());
		map.put("val", this.getValue(obj));
		return map;
	}

}
