
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>主页</title>
		<script src="${ip}/${project}/js/jquery-3.4.1.min.js"></script>
        <script src="${ip}/${project}/ajax/ajax.js"></script>
	</head>
	<body>
<script src="${ip}/${project}/js/ui/chart.min.js"></script>
  
  <!-- 图片下载和在线预览 -->
  
  <!--图像数据   参考https://how2j.cn/k/chartjs/chartjs-bar/2057.html#nowhere  -->
<div id="w" style="width:1000px">
    
</div>
<script>
var url = "/${project}/test/te";		// 请求的url + id
var xhr = new XMLHttpRequest();
xhr.open("GET", url, true)
xhr.responseType = "blob";
xhr.setRequestHeader("client_type", "DESKTOP_WEB");
//xhr.setRequestHeader("Authorization", token);			// 自定义请求头
xhr.onload = function(){
	if(this.status == 200){
		var blob = this.response;
		var fileName = "ss.doc";
		if(window.navigator.msSaveOrOpenBlob){			// IE浏览器下
			navigator.msSaveBlob(blob, fileName);
		} else {
			var  link = document.createElement("a");
			debugger;
			link.innerText='下载';
			link.href = window.URL.createObjectURL(blob);
			link.download = fileName;
			//link.click();
			//let url =window.URL.createObjectURL(new Blob([res]))
			//window.URL.revokeObjectURL(link.href);
			var oB2 = document.getElementById('w');
			oB2.appendChild(link);
		}
	}
};
xhr.send()
</script>
	</body>
</html>
