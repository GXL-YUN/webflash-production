package cn.enilu.wx.util;


import cn.enilu.flash.bean.entity.system.User;
import cn.enilu.flash.bean.util.WxBean;
import cn.enilu.flash.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信获取用户编码数据
 */
@Component
@Slf4j
public class WxUtil {

    /**
     * 根据微信code查询微信用户id
     */
    @Value("${spring.wx.appid}")
    private  String appid;

    @Value("${spring.wx.secret}")
    private  String secret;

    public  WxBean getAppId(String code){
        Map<String, String> param=new HashMap<>();
        param.put("appid",appid);
        param.put("secret",secret);
        param.put("js_code",code);
        param.put("grant_type","authorization_code");
        String boby=HttpUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session",param);
        JSONObject date=new JSONObject(boby);
        WxBean wx=new WxBean();
        wx.setOpenid((String) date.get("openid"));
        wx.setSession_key((String) date.get("session_key"));
        return wx;
    }


    /**
     * 根据微信用户编码  获取用户信息
     */
    public User getByCodeIdUser(){
        return null;
    }

    public static void main(String[] args) {
        //getAppId("0f3KmKFa13xxWK00cRGa1pW1st2KmKFK");
    }

    public WxBean getAppId(String wxmlCode, String appid, String secret) {

        Map<String, String> param=new HashMap<>();
        param.put("appid",appid);
        param.put("secret",secret);
        param.put("js_code",wxmlCode);
        param.put("grant_type","authorization_code");
        String boby=HttpUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session",param);
        JSONObject date=new JSONObject(boby);
        WxBean wx=new WxBean();
        wx.setOpenid((String) date.get("openid"));
        wx.setSession_key((String) date.get("session_key"));
        return wx;
    }
}
