package com.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.servlet.http.HttpServletResponse;

public class FileUtil {
    
    /**
     * 将图片，或者文件转成base64字符串
     * @param imageFile
     * @return
     */
    public static String getImageStr(File imageFile) {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(imageFile));
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            String imageStr = new String(Base64.getEncoder().encode(data));
            return imageStr;
        } catch (IOException e) {//
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * 将base64串 转成byte数组，并保存到某路径下的图片中
     * @param imageStr
     * @return
     */
    public static File getImageFile(String imageStr) {
        File file = new File("D:\\" + System.currentTimeMillis() + ".png");
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] data = Base64.getDecoder().decode(imageStr);
            out.write(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    
     /**
     *  根据字节流获取图片类型
     * */
    public static String getImageType(byte[] imageBytes) {
        try {
            MemoryCacheImageInputStream memoryCacheImageInputStream = new MemoryCacheImageInputStream(new ByteArrayInputStream(imageBytes));
            Iterator<ImageReader> itr = ImageIO.getImageReaders(memoryCacheImageInputStream);
            while (itr.hasNext()) {
                ImageReader reader = itr.next();
                String imageName = reader.getClass().getSimpleName();
                if (imageName != null) {
                    if ("JPEGImageReader".equalsIgnoreCase(imageName)){
                        return "jpeg";
                    }
                    if ("JPGImageReader".equalsIgnoreCase(imageName)) {
                        return "jpg";
                    }
                    if ("pngImageReader".equalsIgnoreCase(imageName)) {
                        return "png";
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
    public HttpServletResponse download(String path, HttpServletResponse response) {
        try {
          // path是指欲下载的文件的路径。
          File file = new File(path);
          // 取得文件名。
          String filename = file.getName();
          // 取得文件的后缀名。
          String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

          // 以流的形式下载文件。
          InputStream fis = new BufferedInputStream(new FileInputStream(path));
          byte[] buffer = new byte[fis.available()];
          fis.read(buffer);
          fis.close();
          // 清空response
          response.reset();
          // 设置response的Header
          response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
          response.addHeader("Content-Length", "" + file.length());
          OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
          response.setContentType("application/octet-stream");
          toClient.write(buffer);
          toClient.flush();
          toClient.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        return response;
      }

      public void downloadLocal(HttpServletResponse response) throws FileNotFoundException {
        // 下载本地文件
        String fileName = "Operator.doc".toString(); // 文件的默认保存名
        // 读到流中
        InputStream inStream = new FileInputStream("c:/Operator.doc");// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
          while ((len = inStream.read(b)) > 0)
            response.getOutputStream().write(b, 0, len);
          inStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      public void downloadNet(HttpServletResponse response) throws MalformedURLException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL("windine.blogdriver.com/logo.gif");

        try {
          URLConnection conn = url.openConnection();
          InputStream inStream = conn.getInputStream();
          FileOutputStream fs = new FileOutputStream("c:/abc.gif");

          byte[] buffer = new byte[1204];
          int length;
          while ((byteread = inStream.read(buffer)) != -1) {
            bytesum += byteread;
            System.out.println(bytesum);
            fs.write(buffer, 0, byteread);
          }
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }


    //支持在线打开文件的一种方式

    public void downLoad(String filePath, HttpServletResponse response, boolean isOnLine) throws Exception {
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
      }


}
 