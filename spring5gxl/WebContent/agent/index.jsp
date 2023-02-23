<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!doctype html>
<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="shortcut icon" href="${ip}/${project}/images/left.png">
<!-- Bootstrap CSS -->
<link rel="stylesheet" href="${ip}/${project}/css/bootstrap.css">
<link rel="stylesheet"
	href="${ip}/${project}/css/index2/main.css">
<link rel="stylesheet"
	href="${ip}/${project}/css//index2/animate.css">
<link rel="stylesheet"
	href="${ip}/${project}/css/index2/font-icon.css">

<link rel="stylesheet"
	href="${ip}/${project}/css/admin/jqAlert.css">


<script src="${ip}/${project}/js/ui/jqAlert.js"></script>
<script src="${ip}/${project}/js/jquery-3.4.1.min.js"></script>
<script src="${ip}/${project}/ajax/ajax.js"></script>
<script src="${ip}/${project}/js/vue/vue.js"></script>


<title>个人博客</title>
</head>

<body>
	<!--导航-->
	<nav
		class="navbar navbar-expand-lg navbar-light nav-color-nav  navbar-lk text-dark">
		<div class="container">
			<a class="navbar-brand xiyuan" href="#"><img
				src="../images/logo2.png" alt=""></a>
			<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#nabmune" aria-controls="nabmune" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse justify-content-end"
				id="nabmune">
				<ul class="navbar-nav nav-color ">
					<li class="nav-item active "><a class="nav-link"href="${ip}/${project}/">首页 <span class="sr-only">(current)</span></a>
					</li>
					<li class="nav-item "><a class="nav-link"href="${ip}/${project}/agent/list.jsp?mathen=doument/select">分享文章</a>
					</li>
					<li class="nav-item "><a class="nav-link" href="life.jsp">收藏网站</a>
					</li>

					<li class="nav-item "><a class="nav-link" href="about.jsp">开发计划</a>
					</li>
					<li class="nav-item dropdown "><a
						class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
						role="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false">后台设置 </a>
						<div class="dropdown-menu" aria-labelledby="navbarDropdown">
							<a class="dropdown-item"
								href="${ip}/${project}/agent/admin/index.jsp">后台页面</a>
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
			<div class="col-md-8">
				<div class="container">
					<div class="row wow fadeInDown">
						<div id="carouselExampleIndicators" class="carousel slide"
							data-ride="carousel">
							<ol class="carousel-indicators">
								<li data-target="#carouselExampleIndicators" data-slide-to="0"
									class="active"></li>
								<li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
								<li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
							</ol>
							<div id="pic" class="carousel-inner">

								<div class="carousel-item active">
									<img src="${ip}/${project}/images/1.png"
										class="d-block w-100" alt="...">
								</div>
								<div class="carousel-item">
									<img src="${ip}/${project}/images/2.png"
										class="d-block w-100" alt="...">
								</div>
								<div class="carousel-item">
									<img src="${ip}/${project}/images/3.png"
										class="d-block w-100" alt="...">
								</div>


							</div>
							<a class="carousel-control-prev"
								href="#carouselExampleIndicators" role="button"
								data-slide="prev"> <span class="carousel-control-prev-icon"
								aria-hidden="true"></span> <span class="sr-only">Previous</span>
							</a> <a class="carousel-control-next"
								href="#carouselExampleIndicators" role="button"
								data-slide="next"> <span class="carousel-control-next-icon"
								aria-hidden="true"></span> <span class="sr-only">Next</span>
							</a>
						</div>
					</div>

					<div class="row mt-2 wow fadeInDown">
						<div class="col-md-12 bgc">
							<div class="new">
								<span><i class="el-certificate"></i></span> <span>最新笔记</span> <small>New
									Article</small>
							</div>
						</div>
					</div>

					<div class="row mt-2 ">


						<!--笔记展示  -->
						<ul class="book list-unstyled art-list">
							
							
						</ul>
					</div>
				</div>
			</div>

			<!--右边-->
			<div class="col-md-4 ">
				<div class="container">


					<div class="row bgc mt-5 right-author">
						<div class="col-md-12 ">
							<div class="auth-img">
								<img src="${ip}/${project}/images/logo.png" alt="">
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
							<span><i class="el-arrow-up"></i></span> <span>网站定时任务 </span> <small> 开启中
								</small>
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

							<input class="search-submit" value=" " type="submit"> <i
								class="el-search"></i>
						</div>

						<!--</form>-->
					</div>


					<div class="row bgc mt-3">
						<div class="new new-right">
							<span><i class="el-headphones"></i>说说</span> <small>New
								Article</small>
						</div>
						<div class="col-md-12 ">
							<div class="tab-pane" role="tabpanel"
								aria-labelledby="nav-profile-tab">
								<ul class="art-sort word">
									
								</ul>
							</div>


						</div>
					</div>


					<div class="row bgc mt-4">
						<div class="new new-right">

							<span><i class="el-asl"></i>推荐图文</span> <small>New
								Article</small>
						</div>
						<ul class="list-unstyled artimg-list">
							<li class="media">
								<div class="artimg-img">
									<img class="mr-3" src="${ip}/${project}/images/7.jpg"
										alt="Generic placeholder image">
								</div>
								<div class="media-body art-content">
									<h5 class="mt-0 mb-1">js前端实现模糊查询</h5>
									<p>js前端实现模糊查询对于模糊查询，一般都是传关键字给后端</p>
									<ul>
										<li><a title="惜缘2019-04-03发表"><i class="el-time"></i>2019-04-03</a></li>
										<li class="d-none d-sm-none d-md-none d-lg-block"><a
											href="/index/article/index/id/32.jsp" title="查看分类"><i
												class="el-fire"></i>5</a></li>
									</ul>
								</div>
							</li>
							<li class="media">
								<div class="artimg-img">
									<img class="mr-3" src="${ip}/${project}/images/8.jpg"
										alt="Generic placeholder image">
								</div>
								<div class="media-body art-content">
									<h5 class="mt-0 mb-1">js前端实现模糊查询</h5>
									<p>js前端实现模糊查询对于模糊查询，一般都是传关键字给后端</p>
									<ul>
										<li><a title="惜缘2019-04-03发表"><i class="el-time"></i>2019-04-03</a></li>
										<li class="d-none d-sm-none d-md-none d-lg-block"><a
											href="/index/article/index/id/32.jsp" title="查看分类"><i
												class="el-fire"></i>5</a></li>
									</ul>
								</div>
							</li>
							<li class="media">
								<div class="artimg-img">
									<img class="mr-3" src="${ip}/${project}/images/9.jpg"
										alt="Generic placeholder image">
								</div>
								<div class="media-body art-content">
									<h5 class="mt-0 mb-1">js前端实现模糊查询</h5>
									<p>js前端实现模糊查询对于模糊查询，一般都是传关键字给后端</p>
									<ul>
										<li><a title="惜缘2019-04-03发表"><i class="el-time"></i>2019-04-03</a></li>
										<li class="d-none d-sm-none d-md-none d-lg-block"><a
											href="/index/article/index/id/32.jsp" title="查看分类"><i
												class="el-fire"></i>5</a></li>
									</ul>
								</div>
							</li>

						</ul>

					</div>







				</div>
			</div>
		</div>

	</div>

	<!--底部-->
	<footer id="footer">
		<div id="box"
			style="text-align: center; margin-top: 5px; padding-top: 15px; padding-left: 25%; padding-right: 25%; padding-bottom: 20px; padding-bottom: 20px; background-color: #585555; color: white; font-size: 10px;">
			<br> Copyright © 2021 数据集合服务 All Rights Reserved 陕ICP备 001号
			https://beian.miit.gov.cn/ <br> 版权所有：谷鑫磊 的个人博客 <br>
			服务热线：有事留言 我会看见 <br> 服务地址：无家可归的人儿没有固定地址
		</div>

		<!-- <div id="hea" class="citable"data-url="${ip}/${project}:8081/selectindex"  data-name="hea">
	<div v-for="item in items" :key="item.message" >
		{{ item.doument_name }}
	
	</div>
</div> -->


	</footer>

	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->

	<script src="./js/bootstrap.js"></script>
	<script src="./js/index2/wow.js"></script>

	<script>
		new WOW().init();//把wow初始化
		book();
		time();
		word();
		//定时任务
		
		function time(){
			var json=getajax('${ip}/${project}/selectTack?index=1&count=5');	
			  for(var i=0;i<json.length;i++){
	        	  $(".time").append("<li>"+json[i].fd_name+"<sapn style='float: right;'>"+formatDate(json[i].create_time)+"</span> </li>" );
	          }
		}
		
		
		
		function word(){
			var json=getajax('${ip}/${project}/pic/select?index=1&count=5');	
			  for(var i=0;i<json.length;i++){
	        	  $(".word").append("<li><span>"+(i+1)+"</span>"+json[i].fd_name+"<b></b></li>" );
	          }
		}
		//文章数据
		function  book(){
			  var json=getajax('${project}/doument/select?index=1&count=10');	
			  for(var i=0;i<json.length;i++){
	        	  $(".book").append("<li class='media wow bounceInRight' data-wow-duration='1s'data-wow-delay='0.7s'><div class='art-img'>"+
									"<img class='mr-3' src='${ip}/${project}/images/"+(i+1)+".jpg'"+
										"alt='Generic placeholder image'></div><div class='media-body art-content'>"+
									"<h5 class='mt-0 mb-1'>"+
										"<a href='${ip}/${project}/agent/ind.jsp?math=doument/findid&id="+json[i].fd_id+"'>"+json[i].fd_name+"</a>"+
									"</h5>"+
									"<p>"+json[i].fd_content+"</p>"+
									"<ul>"+
									"	<li><a title=''><i class='el-time'></i>"+formatDate(json[i].create_time)+"</a></li>"+
									"	<li class='d-none d-sm-none d-md-none d-lg-block'></i>惜缘</li>"+
									"	<li class='d-none d-sm-none d-md-none d-lg-block'><a"+
									"		href='/agent/ind.jsp?math=doument/findid&id'"+json[i].fd_id+
									"		title='查看分类'><i class='el-th-list'></i>生活</a></li>"+
									"</ul>"+
								"</div>"+
						"	</li>"
	        			  );
	          }
		}	
		
		
	</script>
	<script>
		//通过请求数据获取轮播图片
	
	</script>
</body>

</html>