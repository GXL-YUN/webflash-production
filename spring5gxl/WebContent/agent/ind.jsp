
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!doctype html>
<html lang="en">

<head>
	<!-- Required meta tags -->
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

	<!-- Bootstrap CSS -->
	<link rel="stylesheet" href="${ip}/${project}/css/bootstrap.css">
	<link rel="stylesheet" href="${ip}/${project}/css/index2/main.css">
	<link rel="stylesheet" href="${ip}/${project}/css//index2/animate.css">
	<link rel="stylesheet" href="${ip}/${project}/css/index2/font-icon.css">
	<link rel="stylesheet" href="${ip}/${project}/css/list.css">
	<script src="${ip}/${project}/js/jquery-3.4.1.min.js"></script>
    <script src="${ip}/${project}/ajax/ajax.js"></script>
	<title>个人博客</title>
</head>

<body>
	<!--导航-->
	<nav class="navbar navbar-expand-lg navbar-light nav-color-nav  navbar-lk text-dark">
		<div class="container">
			<a class="navbar-brand xiyuan" href="#"><img src="${ip}/${project}/images/logo2.png" alt=""></a>
			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#nabmune"
				aria-controls="nabmune" aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse justify-content-end" id="nabmune">
				<ul class="navbar-nav nav-color ">
					<li class="nav-item active "><a class="nav-link"href="${ip}/${project}/">首页 <span class="sr-only">(current)</span></a>
					</li>
					<li class="nav-item "><a class="nav-link"href="${ip}/${project}/agent/list.html?mathen=doument/select">分享文章</a>
					</li>
					<li class="nav-item "><a class="nav-link" href="life.html">收藏网站</a>
					</li>

					<li class="nav-item "><a class="nav-link" href="about.html">开发计划</a>
					</li>
					<li class="nav-item dropdown "><a
						class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
						role="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false">后台设置 </a>
						<div class="dropdown-menu" aria-labelledby="navbarDropdown">
							<a class="dropdown-item"
								href="${ip}/${project}/agent/admin/index.html">后台页面</a>
							<div class="dropdown-divider"></div>

						</div></li>


				</ul>
				<!--<form class="form-inline my-2 my-lg-0">
                <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>-->
			</div>
		</div>
	</nav>

	<!--内容-->
	<div class="container mt-3">
		<div class="row">

			<!-- 左边 -->
			<div class="book col-md-8">

				<div class="tiger">当前位置》内容</div>

		

			</div>

		
		</div>

	</div>

	<!--底部-->
	<footer id="footer">
	
		
	<!-- 底部内容 -->

	<div
	style=" text-align: center; margin-top: 5px; padding-top: 15px; padding-left: 25%; padding-right: 25%; padding-bottom: 20px; padding-bottom: 20px;  background-color: #585555; color: white; font-size: 10px; ">
	<br> Copyright © 2021 数据集合服务 All Rights Reserved 陕ICP备 001号 https://beian.miit.gov.cn/
	<br> 版权所有：谷鑫磊 的个人博客
	<br> 服务热线：有事留言 我会看见
	<br> 服务地址：无家可归的人儿没有固定地址
</div>
	</footer>

	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="./js/jquery-3.4.1.min.js"></script>

	<script src="./js/bootstrap.js"></script>

	<script type="text/javascript">
	

	//获取数据
	function data(){
		var name=getpan("mathen");
		alert(name);
	}
	
	book();

	//文章数据
	function  book(){
		var math=getpan("math");
		var id=getpan("id");
		var str=math+'?fd_id='+id;

		var json=getajax(str);
		  for(var i=0;i<json.length;i++){			  
        	  $(".book").append(" <div class='right1'>"+
  					"<h3 class='p'align= 'center';> "+json[i].fd_subject+"</h3>"+
  					"	 <div align= 'center';> "+formatDate(json[i].create_time)+"</div>"+
  					"  <div>"+
                     
                     json[i].fd_massage+"</div>"+
  					
  			"</div>"
        			  
        	);
          }
	}
	
	</script>
</body>

</html>