<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>

		<script src="${ip}/${project}/js/jquery-3.4.1.min.js"></script>
        <script src="${ip}/${project}/ajax/ajax.js"></script>
<body>

<!-- 图片流处理数据 -->
<!--       <img src="http://localhost/test/test"/>
 -->

<script type="text/javascript">

longPolling();

function longPolling() {
 $.ajax({
        type:'POST',
        url:'${ip}/${project}/count',
        dataType:'json',
        data: {"timed": new Date().getTime()},
        timeout: 2000,
        success:function(result){
           alert(result[2].count);
           
           longPolling()
         },
        error:function(XMLHttpRequest, textStatus, errorThrown){            
            console.error("加载数据失败");                         
        },
                    
    });
}
</script>


</body>
</html>

