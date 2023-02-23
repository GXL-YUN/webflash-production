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
    
<link rel="stylesheet"
	href="${ip}/${project}/css/admin/jqAlert.css">


<script src="${ip}/${project}/js/ui/jqAlert.js"></script>
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

			<!--右边-->
			<div class="col-md-4 ">
				<div class="container">


					<div class="row bgc mt-5 right-author">
						<div class="col-md-12 ">
							<div class="auth-img">
								<img src="../images/logo.png" alt="">
							</div>
							<div class="auth-name">
								<span>gxl</span>
								<p>一个后端开发</p>
							</div>
							<div class="auth-info">
								<ul>
									<li><strong>2</strong><span>今日访问量</span></li>
									<li><strong>2</strong><span>今日访问量</span></li>
									<li><strong>2</strong><span>今日访问量</span></li>
								</ul>
							</div>

						</div>
					</div>


					<div class="row bgc mt-4">
						<div class="new new-right">
							<span><i class="el-arrow-up"></i></span>
							<span>网站公告</span>
							<small>New Article</small>
						</div>
						<div class="col-md-12 ">
							<div class="notice">
								<ul class="time">
								</ul>
							</div>

						</div>
					</div>


					<div class="row bgc mt-3">
						<!--<form>-->
						<div class="search">

							<input type="text" class="search-input" placeholder="First name">

							<input class="search-submit" value=" " type="submit">
							<i class="el-search"></i>
						</div>

						<!--</form>-->
					</div>


					<div class="row bgc mt-3">
						<div class="new new-right">
							<span><i class="el-headphones"></i>说说</span>
							<small>New Article</small>
						</div>
						<div class="col-md-12 ">
							<div class="tab-pane" role="tabpanel" aria-labelledby="nav-profile-tab">
								<ul class="art-sort word">	
								</ul>
							</div>


						</div>
					</div>
				</div>
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
	time();
	word();
	//定时任务
	
	function time(){
		var json=getajax('${project}/selectTack?index=1&count=5');	
		  for(var i=0;i<json.length;i++){
        	  $(".time").append("<li>"+json[i].fd_name+"<sapn style='float: right;'>"+formatDate(json[i].create_time)+"</span> </li>" );
          }
	}
	
	
	
	function word(){
		var json=getajax('${project}/pic/select?index=1&count=5');	
		  for(var i=0;i<json.length;i++){
        	  $(".word").append("<li><span>"+(i+1)+"</span>"+json[i].fd_name+"<b></b></li>" );
          }
	}
	//文章数据
	function  book(){
		var name=getpan("mathen");
		//var str=name+'?index=1&count=10';
		var str="${project}/"+name+'?index=1&count=10';
		var json=getajax(str);
		  for(var i=0;i<json.length;i++){			  
        	  $(".book").append(" <div class='right'>"+
  					"<h5 class='p'> "+json[i].fd_subject+"</h5>"+
                     "  <div style='height: 50px;text-overflow: ellipsis;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 2;-webkit-box-orient: vertical;'>"+
                     
                     json[i].fd_content+"</div>"+
  					"	 <div> "+formatDate(json[i].create_time)+"</div>"+
  			"</div>"
        			  
        	);
          }
	}
	
	</script>
</body>

</html>