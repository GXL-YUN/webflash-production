<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>

    
    <link rel="stylesheet" type="text/css" href="${ip}/${project}/css/admin/form.css"/>
                <link rel="stylesheet" type="text/css" href="${ip}/${project}/css/admin/bootstrap.min.css"/>
</head>

<body>
<form action="${ip}/${project}/url/insert"  onsubmit="insert()" method="post">
    <div class="col-md-12">
      
        
        <div class="form-group">
            <label class="col-sm-3 control-label">标题名称：</label>
            <div class="col-sm-9">
                <input name="fd_name" class="form-control" placeholder="请输入文本" type="text">
            </div>
        </div>
        <div class="form-group ui-sortable-helper">
            <label class="col-sm-3 control-label">url地址：</label>
            <div class="col-sm-9">
                <input name="fd_url" class="form-control" placeholder="请输入文本" type="text">
            </div>
        </div>
        <div class="form-group ui-sortable-helper">
            <label class="col-sm-3 control-label">简要信息：</label>
            <div class="col-sm-9">
                <input name="fd_massage" class="form-control" placeholder="请输入文本" type="text">
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="form-group">
            <div class="col-sm-12 col-sm-offset-3">
                <button type="submit" class="btn btn-primary">提交</button>
                <button onclick="$.modal.close()" class="btn btn-danger" type="button">关闭</button>
            </div>
        </div>
    </div>

</form>

</body>
</html>



