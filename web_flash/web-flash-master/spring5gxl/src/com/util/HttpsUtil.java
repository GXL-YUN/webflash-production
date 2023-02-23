package com.util;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.alibaba.fastjson.JSONObject;



public class HttpsUtil {
	
	
	
	
	public static JSONObject get(String urlPath,String param) throws Exception {
		
		JSONObject parseObject=null; //返回数据
		// 建立连接
		URL url = new URL(urlPath);
		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		// 设置参数
		httpConn.setDoOutput(true); // 需要输出
		httpConn.setDoInput(true); // 需要输入
		httpConn.setUseCaches(false); // 不允许缓存
		httpConn.setRequestMethod("POST"); // 设置POST方式连接
		// 设置请求属性
		httpConn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
		httpConn.setRequestProperty("Charset", "UTF-8");
		// 设置连接超时
		httpConn.setConnectTimeout(300 * 1000);
		// 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
		httpConn.connect();
		// 建立输入流，向指向的URL传入参数
		DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
		dos.writeBytes(param);
		dos.flush();

		// 获得响应状态
		int resultCode = httpConn.getResponseCode();
		StringBuffer sb = new StringBuffer();
		String readLine = new String();
		BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
		while ((readLine = responseReader.readLine()) != null) {
			sb.append(readLine).append("\n");
		}
		dos.close();
		responseReader.close();
		try {
			 parseObject = JSONObject.parseObject(sb.toString());
		}catch (Exception e) {
			// TODO: handle exception
			 parseObject=new JSONObject();
			parseObject.put("data", sb.toString());
		}
		
	return parseObject;

	}

	static // its测试
			HostnameVerifier hv = new HostnameVerifier() {
		public boolean verify(String urlHostName, SSLSession session) {
			//System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
			return true;

		}
	};

	private static void trustAllHttpsCertificates() throws Exception {
		// 创建TrustManager数组对象
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		// 创建TrustManager对象，通过实现TrustManager接口，创建类对象
		javax.net.ssl.TrustManager tm = new miTM();
		// 创建的TrustManager对象装进数组中
		trustAllCerts[0] = tm;
		// 创建SSLContext对象
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		// 初始化SSLContext对象
		sc.init(null, trustAllCerts, null);
		// 设置HttpsURLConnection的属性DefaultSSLSocketFactory为创建的SSLContext对象的属性
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}
	
	
	public static void main(String[] args) {
		try {
			//HttpsUtil.get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}