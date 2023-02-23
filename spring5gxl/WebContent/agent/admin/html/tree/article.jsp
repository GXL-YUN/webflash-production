<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
    
    <link rel="stylesheet" type="text/css" href="${ip}/${project}/css/admincommon.css"/>
    <link rel="stylesheet" type="text/css" href="${ip}/${project}/css/admin/main.css"/>
    <script type="text/javascript" src="${ip}/${project}/js/admin/modernizr.min.js"></script>
    <script type="text/javascript" src=" ${ip}/${project}/js/ui/dialog.js"></script>
    <script type="text/javascript" src="${ip}/${project}/js/jquery-1.8.0.min.js"></script>
    
    	<link rel="stylesheet" href="${ip}/${project}/css/admin/pagejs.css">
    <script src=" ${ip}/${project}/js/ui/page.js"></script>
	<link rel="stylesheet" href="${ip}/${project}//css/admin/jqAlert.css">
	<script src="${ip}/${project}/ajax/ajax.js"></script>
	
</head>
<body>
    <!--/sidebar-->
    <div >
        <div class="crumb-wrap">
            <div class="crumb-list"><i class="icon-font"></i><a href="index.jsp">首页</a><span class="crumb-step">&gt;</span><span class="crumb-name">美词管理</span></div>
        </div>
        <div class="search-wrap">
            <div class="search-content">
                <form action="#" method="post">
                    <table class="search-tab">
                        <tr>
                            <th width="120">选择分类:</th>
                            <td>
                                <select name="search-sort" id="">
                                    <option value="">全部</option>
                                    <option value="19">精品界面</option><option value="20">推荐界面</option>
                                </select>
                            </td>
                            <th width="70">关键字:</th>
                            <td><input class="common-text" placeholder="关键字" name="keywords" value="" id="" type="text"></td>
                            <td><input class="btn btn-primary btn2" name="sub" value="查询" type="submit"></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
        <div class="result-wrap">
            <form name="myform" id="myform" method="post">
                <div class="result-title">
                    <div class="result-list">
                       
                    </div>
                </div>
                <div class="result-content">
                    <table class="result-tab contentdate" width="100%">
                        <tr>
                            <th class="tc" width="5%"><input class="allChoose" name="" type="checkbox"></th>
                            <th style="white-space: nowrap;">排序</th>
                            <th style="white-space: nowrap;">美文</th>
                           
                             <th style="white-space: nowrap;">创建时间</th>
                             <th style="white-space: nowrap;">操作</th>
                        </tr>
                        
                    </table>
                 <div class="paging"></div>
                </div>
            </form>
        </div>
    </div>
    
    
       <!-- js添加数据 -->
   
   <script type="text/javascript">
	 var count=getajax(${project}/'pic/count');
		let paging =new Paging({total:count,})
			 
	   //查询数据
		get(1,10); 
	   function get(index,count){
 
	   //查询数据
        var json=getajax('${project}/pic/select?index='+index+'&count='+count);
        
        $(".list").remove();
 		for(var i=0;i<json.length;i++){
        	  $(".contentdate").append("<tr class='list "+json[i].fd_id+"'>"+
                      "<td class='tc '><input name='id[]' value='59' type='checkbox'></td>"+
                         "<td>"+i+"</td>"+
                        " <td>"+json[i].fd_name+"</td>"+

                         "<td>"+formatDate(json[i].create_time)+"</td>"+
                         "<td>"+
                            "<a class='link-update' href='"+json[i].fd_id+"'>修改</a>"+
                            "<a class='link-del' onclick=delecttr('pic/delete','"+json[i].fd_id+"')>删除</a>"+
                         "</td>"+
                    "</tr>");
          }
 		
 		//删除数据
 		
		   }
 		//删除数据
 	
 
 		
   </script>
</body>
</html>