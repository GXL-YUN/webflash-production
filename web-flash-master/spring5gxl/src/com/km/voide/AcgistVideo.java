package com.km.voide;




import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import com.sql.dao.service.icmp.FindModelServiceIcmp;
 
/**
 * 推荐JDK8及以上（适应lambda表达式），需导入lib下三个Jar包，支持摄像头选择、开始摄像、停止摄像
 */
public class AcgistVideo  {
	
	
	private static Logger logger = Logger.getLogger(AcgistVideo.class);
	public static void main(String[] args) throws Exception, InterruptedException
	{
	    //OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);  
	    //grabber.start();   //开始获取摄像头数据
	    CanvasFrame canvas = new CanvasFrame("摄像头");//新建一个窗口
	    canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    canvas.setAlwaysOnTop(true);
	    
	    while(true)
	    {
	       /** if(!canvas.isDisplayable())
	        {//窗口是否关闭
	            grabber.stop();//停止抓取
	            System.exit(2);//退出
	        }*/
			//System.out.println(grab);
	        
	       // Frame grab = grabber.grab();
	        canvas.showImage(AcgistVideo.getvoid());//获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像

	        Thread.sleep(10);//50毫秒刷新一次图像
	    }
	}
	
	
	
	/**
	 * 获取本地数据发送服务器端
	 * @return
	 */
	
	
			public static Frame getvoid() {
				 OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
				 Frame grab = null;
				  try {
					   grabber.start();   //开始获取摄像头数据
					grab = grabber.grab();
				} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
					// TODO Auto-generated catch block
					logger.error("链接失败");
					
				}
				return grab;
				 
			}

	
}