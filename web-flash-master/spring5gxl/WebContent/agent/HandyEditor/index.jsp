<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>HandyEditor</title>

  </head>
  <body>


    <textarea id="editor" name="editor" rows="5" style="display: none;"></textarea>
    <br>
    <button onclick="getHtml()">获取HTML</button>&nbsp;&nbsp;
    <button onclick="getText()">获取纯文本</button>
    <br>
    <div id="text"></div>
    <!-- <br><br>
    <h4>使用方法：</h4>
    <h5>1、引入HandyEditor的javaScript文件</h5>
    <pre>&lt;script src="HandyEditor.min.js"&gt;&lt;/script&gt;</pre>
    <h5>2、在您的javaScript代码中添加HandyEditor代码</h5>
    <pre>var he = HE.getEditor('editor');//其中的editor是文本输入框的id</pre>
    <h5>3、获取编辑器中的内容</h5>
    <pre>he.getHtml();//获取HTML代码</pre>
    <pre>he.getText();//获取纯文本</pre> -->
   <!--  <br>
    更多高级用法请访问<a href="http://www.catfish-cms.com" target="_blank">Catfish(鲶鱼) CMS官方网站</a>或者<a href="http://he.catfish-cms.com" target="_blank">HandyEditor</a>详情页
    <br> -->
    <script src="${ip}/${project}/agent/HandyEditor/HandyEditor.min.js"></script>
    <script type="text/javascript">
      var he = HE.getEditor('editor');
      function getHtml(){
    	  opener.chan(he.getHtml());
    	  //opener.parentPara=he.getHtml();
    	  //document.write(he.getHtml());
          // alert(he.getHtml());
    	  window.close();
        
      }
      function getText(){
        alert(he.getText());
      }
    </script>
    
    
  </body>
</html>