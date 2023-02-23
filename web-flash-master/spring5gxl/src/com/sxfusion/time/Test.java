package com.sxfusion.time;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sql.dao.bean.Msql;
import com.sql.dao.mapper.InsertDateMapper;
import com.sql.dao.service.FindModelService;
import com.sxfusion.demo.entity.Music;
import com.util.HttpClientT;
import com.util.HttpsUtil;

public class Test  implements Job {
	@Autowired
	private InsertDateMapper i;
	
	@Autowired
	private FindModelService s;
	
	public void work() {
		System.out.println("定时任务配置文件");   //http://baobab.kaiyanapp.com/api/v4/tabs/selected?
		// 图片接口  https://api.uomg.com/api/rand.music?sort=热歌榜&format=json
		
		//获取新闻数据
		JSONObject doGet=null;
		try {
			doGet = HttpsUtil.get("https://api.iyk0.com/yi","ss");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//
		
	      String jsonArray = doGet.getString("data");
	        System.out.println(jsonArray);
	          Msql msql=new Msql();
			  msql.setTable("doument_main");
			  try {
				  Music music=new Music();
				  music.setTable("pic_main");
				  System.out.println(music.getFd_id());
				  music.setFd_name(jsonArray);
				  boolean insert = i.insert(music.modeltoMap(music));
				//  System.out.println(insert);
				//System.out.println("se"+s.findOlist(msql));
				
				//System.out.println(list);
			  }catch (Exception e) {
				// TODO: handle exception
				  e.printStackTrace();  
			}
		//音乐类
    }

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		   System.out.println("开启任务中11");
	}

}
