package com.km.rtmp.bean;

/**
1. 基础视频流
*/
public class BStreamer {
   private int width = 640;
   private int height = 480;
   private String url;

   public BStreamer(String url) {
       this.url = url;
   }

   public BStreamer(String url, int w, int h) {
       this.url = url;
       if (w > 0 && h > 0) {
           this.width = w;
           this.height = h;
       }
   }
   public int getWidth() {
       return width;
   }

   public void setWidth(int width) {
       this.width = width;
   }

   public int getHeight() {
       return height;
   }

   public void setHeight(int height) {
       this.height = height;
   }

   public String getUrl() {
       return url;
   }

   public void setUrl(String url) {
       this.url = url;
   }
}
