package com.km.voide.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.sarxos.webcam.Webcam;
import com.km.voide.util.clientUI;
import com.km.voide.util.serverUI;
import com.sql.dao.bean.OneTable;
import com.util.ImageUtils;


@RestController
@RequestMapping("test")
public class TestColltion {

	@RequestMapping("s")
	@ResponseBody
	public Integer s() {
		serverUI clien = new serverUI();
		clien.main(null);
		return null;

	}

	@RequestMapping("c")
	@ResponseBody
	public Integer c() {
		clientUI c = new clientUI();
		c.setUI();

		return null;

	}

	
	/**
	 * 在线预览图片数据
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("test")
	@ResponseBody
	public  static String  test(HttpServletResponse response , HttpServletRequest request ) throws IOException {
		// get image获取图片
		
		String contextPath = request.getContextPath();
		
		
			BufferedImage image = null;
			Webcam webcam = Webcam.getDefault();
			webcam.open();
			//image = webcam.getImage();
			//InputStream in = ImageUtils.bufferedImageToInputStream(image);
			//byte[] bytesImage = ImageUtils.bytesImage(in);
			
			InputStream readImage = ImageUtils.readImage("D:\\datas\\weixin\\WeChat Files\\wxid_wc5abg3o0ikt22\\FileStorage\\File\\2022-02\\远大Oracle集成\\泛微oa开发\\泛微oa开发\\Oracle对接\\页面截图\\付款单.png");
			
			String im = ImageUtils.imageBase64(readImage);			
			InputStream ss = ImageUtils.base64Stream(im);
			BufferedImage handleImage = ImageUtils.handleImage(ss, 100, 100, 1.00F);
			
			//System.out.println("获取数据图片" + bytesImage.toString());
			ServletOutputStream os = response.getOutputStream();
			ImageIO.write(handleImage, "PNG", os);

			os.flush();
		    os.close();
			webcam.close();
			
			return  "ok";
	}
	
	
	
	/**
	 * 在线预览pdf文件 数据  代码  
	 * 
	 * 2022 3.7   gxl
	 * @param response
	 * @throws IOException
	 */
	   @ResponseBody
	    @RequestMapping("view")
	    public void download( HttpServletResponse response
	                        ) throws IOException {
	        String filePath = "D:\\datas\\weixin\\WeChat Files\\wxid_wc5abg3o0ikt22\\FileStorage\\File\\2022-03\\招标文件（信息化二期建设）_.pdf";
	        System.out.println("filePath:" + filePath);
	        File f = new File(filePath);
	        if (!f.exists()) {
	            response.sendError(404, "File not found!");
	            return;
	        }
	        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
	        byte[] bs = new byte[1024];
	        int len = 0;
	        response.reset(); // 非常重要
	        if (true) { // 在线打开方式
	            URL u = new URL("file:///" + filePath);
	            String contentType = u.openConnection().getContentType();
	            response.setContentType(contentType);
	            response.setHeader("Content-Disposition", "inline;filename="
	                    + "2019年上半年英语四级笔试准考证(戴林峰).pdf");
	            // 文件名应该编码成utf-8，注意：使用时，我们可忽略这句
	        } else {
	            // 纯下载方式
	            response.setContentType("application/x-msdownload");
	            response.setHeader("Content-Disposition", "attachment;filename="
	                    + "2019年上半年英语四级笔试准考证(戴林峰).pdf");
	        }
	        OutputStream out = response.getOutputStream();
	        while ((len = br.read(bs)) > 0) {
	            out.write(bs, 0, len);
	        }
	        out.flush();
	        out.close();
	        br.close();
	    }
	@RequestMapping("te")
	@ResponseBody
	public  static void  te(HttpServletResponse response , HttpServletRequest request ) throws IOException {
		// get image获取图片
		
		try {
          //  String url="D:\\datas\\weixin\\WeChat Files\\wxid_wc5abg3o0ikt22\\FileStorage\\File\\2022-02\\远大Oracle集成\\泛微oa开发\\泛微oa开发\\Oracle对接\\页面截图\\付款单.png";
           String url="‪D:\\tools\\jdk内存.txt";
            
           // 下载本地文件
           String fileName = "Operator.doc".toString(); // 文件的默认保存名
           // 读到流中
           InputStream inStream = new FileInputStream("url");// 文件的存放路径
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	@RequestMapping(value = "/device/IoReadImage")
    public String IoReadImage(String imgName,HttpServletRequest request,HttpServletResponse response) throws IOException {
        ServletOutputStream out = null;
        FileInputStream ips = null;
        try {
            //获取图片存放路径
            String imgPath =imgName;
            ips = new FileInputStream(new File(imgPath));
            response.setContentType("multipart/form-data");
            out = response.getOutputStream();
            //读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = ips.read(buffer)) != -1){
                out.write(buffer,0,len);
            }
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            out.close();
            ips.close();
        }
        return null;
    }
	
	/**
     * 获取视频流
     * @param response
     * @param videoId 视频存放信息索引
     * @return
     * @author xWang
     * @Date 2020-05-20
     */
	
	/**
    @RequestMapping("/getVideo/{videoId}")
    public void getVideo(HttpServletRequest request,HttpServletResponse response,@PathVariable Integer videoId)
    {
        //视频资源存储信息
        VideoSource videoSource = videoSourceService.selectById(videoId);
        response.reset();
        //获取从那个字节开始读取文件
        String rangeString = request.getHeader("Range");

        try {
            //获取响应的输出流
            OutputStream outputStream = response.getOutputStream();
            File file = new File(videoSource.getFileAddress());
            if(file.exists()){
                RandomAccessFile targetFile = new RandomAccessFile(file, "r");
                long fileLength = targetFile.length();
                //播放
                if(rangeString != null){

                    long range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
                    //设置内容类型
                    response.setHeader("Content-Type", "video/mp4");
                    //设置此次相应返回的数据长度
                    response.setHeader("Content-Length", String.valueOf(fileLength - range));
                    //设置此次相应返回的数据范围
                    response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
                    //返回码需要为206，而不是200
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设定文件读取开始位置（以字节为单位）
                    targetFile.seek(range);
                }else {//下载

                    //设置响应头，把文件名字设置好
                    response.setHeader("Content-Disposition", "attachment; filename="+videoSource.getFileName() );
                    //设置文件长度
                    response.setHeader("Content-Length", String.valueOf(fileLength));
                    //解决编码问题
                    response.setHeader("Content-Type","application/octet-stream");
                }


                byte[] cache = new byte[1024 * 300];
                int flag;
                while ((flag = targetFile.read(cache))!=-1){
                    outputStream.write(cache, 0, flag);
                }
            }else {
                String message = "file:"+videoSource.getFileName()+" not exists";
                //解决编码问题
                response.setHeader("Content-Type","application/json");
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
            }

            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }
**/
}
