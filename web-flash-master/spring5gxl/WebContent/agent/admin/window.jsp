<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns=" http://www.w3.org/1999/xhtml">
<head>
  <title>图标</title>
  
  	   <link href="${ip}/${project}/css/admin/icons.min.css" rel="stylesheet" type="text/css" />
  	   <link href="${ip}/${project}/css/admin/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script src="${ip}/${project}/js/jquery-3.4.1.min.js"></script>
		<script src="${ip}/${project}/ajax/ajax.js"></script>
</head>
<body >
  <div id="ico" style="width: 1821px;"></div>
  
</body>
  <script type="text/javascript">
  var cssfile=getstyle();
  for(var i=0;i<cssfile.length;i++){
		//debugger;
		//.bxs-joystick-alt::before
		var myStr=cssfile[i].selectorText;
		try{
			var c=myStr.indexOf('-');
			var j=myStr.indexOf(':');
			var bx = myStr.substring(1,c); 
			var bxm = myStr.substring(1,j); 
			$("#ico").append( "<button class='"+bx+" "+bxm+"'style='font-size: 4em;'></button>");
		}
		catch(e){
			console.log(e+myStr);
			continue;
		}
	} 
  </script>
</html>

