<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>

    
    <link rel="stylesheet" type="text/css" href="${ip}/${project}/css/admin/form.css"/>
                <link rel="stylesheet" type="text/css" href="${ip}/${project}/css/admin/bootstrap.min.css"/>
                <link rel="stylesheet" type="text/css" href="${ip}/${project}/HandyEditor/external/css/HandyEditor.css"/>
                <link rel="stylesheet" type="text/css" href="${ip}/${project}/agent/HandyEditor/skin/HandyEditor.css"/>
                 <script src="${ip}/${project}/agent/HandyEditor/lang/zh-jian/zh-jian.js"></script>
</head>

<body>

<form action="${ip}/${project}/doument/insert"  onsubmit="insert()" target="myIframe" method="post">
<div class="col-md-6">
    <div class="form-group">
        <label class="col-sm-3 control-label">标题名：</label>
        <div class="col-sm-9">
            <input name="fd_name" class="form-control" placeholder="请输入文本" type="text">
        </div>
    </div>
</div>
<div class="col-md-6">
    <div class="form-group">
        <label class="col-sm-3 control-label">标题名：</label>
        <div class="col-sm-9">
            <input name="fd_subject" class="form-control" placeholder="请输入文本" type="text">
        </div>
    </div>
</div>
<div class="col-md-12">
    <div class="form-group ui-sortable-helper">
        <label class="col-sm-4 control-label">主要内容：</label>
        <div class="col-sm-9">
            <input name="fd_content" class="form-control" placeholder="请输入文本" type="text">
        </div>
    </div>
</div>
<div class="col-md-12">
    <div class="form-group">
        <label class="col-sm-3 control-label">笔记内容：</label>
        <div class="col-sm-9">
          <textarea id="editor" name="fd_massage" class="form-control"placeholder="请输入文本" style="margin-top: 0px; margin-bottom: 0px; height: 386px;" style="display: none;"></textarea>           
       <button onclick="window.open('${ip}/${project}/agent/HandyEditor/index.jsp')">打开新窗口 美化页面</button>
     </div>
    </div>
    
  
    <div class="form-group">
        <div class="col-sm-12 col-sm-offset-3">
            <input type="submit" class="btn btn-primary">
            <button  class="btn btn-danger"  onclick='btnCloses()' type="button">关闭<tton>
        </div>
    </div>
</div>
</form>  
    <iframe id="myIframe" name="myIframe" style="  opacity: 0;"></iframe>
    
    <script type="text/javascript">
    
      function insert(){
	     getsuccess("数据添加成功");
       }
      
       function chan(val){
    	  $("#editor").val(val)
       }
    </script>
</body>
</html>