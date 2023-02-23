package com.km.context.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
 
/**
 *@title : JavaClass 
 *@author:zyh
 *@createDate:2018/9/13 21:46
 *
 **/
@RequestMapping(value = "/read")
@Controller
public class Deam {

    @RequestMapping(value = "/doc",method = RequestMethod.GET)
    @ResponseBody
    public void getImage(String path, HttpServletRequest request, HttpServletResponse response) {
        
    	String filePath="D:\\tools\\1111.doc";
    	boolean isOnLine=false;
    	try {
        	 File f = new File(filePath);
             if (!f.exists()) {
               response.sendError(404, "File not found!");
               return;
             }
             BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
             byte[] buf = new byte[1024];
             int len = 0;

             response.reset(); // 非常重要
             if (isOnLine) { // 在线打开方式
               URL u = new URL("file:///" + filePath);
               
              // responseType:"multipart/form-data"
               response.setContentType(u.openConnection().getContentType());
               response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
               // 文件名应该编码成UTF-8
             } else { // 纯下载方式
               response.setContentType("application/x-msdownload");
               response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
             }
             OutputStream out = response.getOutputStream();
             while ((len = br.read(buf)) > 0)
               out.write(buf, 0, len);
             br.close();
             out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //return response;
    }

 
    @RequestMapping(value = "/imgRes",method = RequestMethod.POST)
    @ResponseBody
    public HttpServletResponse  getImageRes(String path, HttpServletRequest request, HttpServletResponse response) {
        try {
            String url="D:\\temp-appImg\\20180912\\7cd2e1a3-a087-4e25-aac8-2bdf8e274c6f.png";
            File file = new File(url);
            String l=request.getRealPath("/")+"/"+url;
            String filename = file.getName();
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("image/png");

            OutputStream toClient = new BufferedOutputStream(response.getOutputStream(),888888);
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    @RequestMapping(value = "/noLogin/readImageFile1", method = RequestMethod.POST)
    @ResponseBody
    public void getUrlFile(String url, HttpServletRequest request, HttpServletResponse response) {
        String serverUrl = "D:\\temp-appImg\\";
        String imgUrl = serverUrl + url;
        File file = new File(imgUrl);
        // 后缀名
        String suffixName = url.substring(url.lastIndexOf("."));
        String imgType = "image/" + suffixName;
        //判断文件是否存在如果不存在就返回默认图标
        if (!(file.exists() && file.canRead())) {
            file = new File(request.getSession().getServletContext().getRealPath("/")
                    + "resource/icons/auth/root.png");
        }
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            int length = inputStream.read(data);
            inputStream.close();
            //setContentType("text/plain; charset=utf-8"); 文本
            response.setContentType(imgType + ";charset=utf-8");
            OutputStream stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}