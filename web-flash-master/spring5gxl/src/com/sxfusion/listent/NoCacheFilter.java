package com.sxfusion.listent;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sxfusion.demo.service.impl.Run;
/**
* @ClassName: NoCacheFilter
* @Description: 清除浏览器缓存过滤器
* @author gxl
* @date 2021年12月12日 上午9:18:45
 */
public class NoCacheFilter implements Filter{

	private static Logger logger = Logger.getLogger(NoCacheFilter.class);
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
         HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;
            //禁止浏览器缓存所有动态页面
            response.setHeader("Pragma","no-cache");    
            response.setHeader("Cache-Control","no-cache");    
            response.setDateHeader("Expires", -1);   
            
            gethttp(request, response);
            chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	logger.error("----禁止浏览器缓存过滤器初始化----");
    }
    
    @Override
    public void destroy() {
    	logger.error("----禁止浏览器缓存过滤器销毁----");
    }

    
    /**
     * 获取项目名称 和项目域名
     * @param request
     * @param response
     */
    public void gethttp(HttpServletRequest request, HttpServletResponse response) {
    	//String url = request.getScheme()+"://"+ request.getServerName()+request.getRequestURI()+"?"+request.getQueryString();
    	//System.out.println("获取全路径（协议类型：//域名/项目名/命名空间/action名称?其他参数）url="+url);
    	String url2=request.getScheme()+"://"+ request.getServerName();
    	//System.out.println("协议名：//域名="+url2);


    	//System.out.println("获取项目名="+request.getRequestURI());
    	
    	String project="spring5gxl";
    	
    	
    	//判断一下就好   
    	//有项目名添加   无则为空  
         request.setAttribute("ip",url2 );
    	 request.setAttribute("project",project );
    	//System.out.println("获取参数="+request.getServerName());
    	//System.out.println("获取全路径="+request.getRequestURL());
    }
    
    
}
