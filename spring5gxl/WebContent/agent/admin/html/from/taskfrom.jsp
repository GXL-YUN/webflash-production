<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>

    
    <link rel="stylesheet" type="text/css" href="${ip}/${project}/css/admin/form.css"/>
                <link rel="stylesheet" type="text/css" href="${ip}/${project}/css/admin/bootstrap.min.css"/>
</head>

<body>

<form action="${ip}/${project}/inserttype"  onsubmit="insert()" method="post">
<div class="col-md-12">
    <div class="form-group">
        <label class="col-sm-3 control-label">任务名称:</label>
        <div class="col-sm-9">
            <input name="fd_name" class="form-control" placeholder="请输入文本" type="text">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">路径:</label>
        <div class="col-sm-9">
            <input name="fd_class" class="form-control" placeholder="请输入文本" type="text">
        </div>
    </div>
    
<label class="col-sm-3 control-label">切换按钮：</label>
  
    <div class="form-group">
        <label class="col-sm-3 control-label">任务组名称:</label>
        <div class="col-sm-9">
            <input name="fd_groupa" class="form-control" placeholder="请输入文本" type="text">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">触发器名:</label>
        <div class="col-sm-9">
            <input name="fd_groupb" class="form-control" placeholder="请输入文本" type="text">
        </div>
    </div>
    
    <div class="form-group">
        <label class="col-sm-3 control-label">触发器组名:</label>
        <div class="col-sm-9">
            <input name="fd_groupc" class="form-control" placeholder="请输入文本" type="text">
        </div>
    </div>
    
    
    
    
    <div class="form-group">
        <label class="col-sm-3 control-label">定时时间:</label>
        <div class="col-sm-9">
            <input name="fd_time" class="form-control" placeholder="请输入文本" type="text">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">信息:</label>
        <div class="col-sm-9">
            <input name="fd_massage" class="form-control" placeholder="请输入文本" type="text">
        </div>
    </div>
  
      <div class="form-group">
        <label class="col-sm-3 control-label">接口路径:</label>
        <div class="col-sm-9">
            <input name="fd_url" class="form-control" placeholder="请输入文本" type="text">
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
</body>

<script type="text/javascript">
  
  function insert(){
	  
		return false; // 必须返回false，否则表单会自己再做一次提交操作，并且页面跳转 
  }
  
  </script>
</html>



