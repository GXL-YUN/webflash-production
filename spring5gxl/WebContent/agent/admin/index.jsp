<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>左侧导航栏</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" type="text/css" href="${ip}/${project}/css/admin/page.css" />
		<link href="${ip}/${project}/css/admin/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<link href="${ip}/${project}/css/admin/icons.min.css" rel="stylesheet" type="text/css" />

	</head>
	<body>
		<div class="page" id="app" style="float: left;">
			<div class="nav-left" v-show="navLeftFlag" ref="navLeft">l
				<div class="LogoName">
					WDTLoong Cloud
				</div>
				<div class="navDiv">
					<div class="navName">功能界面数据</div>
					<div class="nav-list">
						<ul>
							<li class="nav-tab a_active waves-effect">
								<a href="html/home.jsp" class="li-a active" target="iframe"><i class='bx bx-home-smile'></i> 主页
									<span class="badge badge-pill badge-primary float-right">3</span>
								</a>
							</li>
							<li class="nav-tab nav-ul">
								<a href="javascript:void[0]" class="li-a"><i class='bx bx-edit'></i> 学习管理
									<i class='bx bx-chevron-right' style="float: right;"></i></a>
								<div class="nav-box">
									<a href="${ip}/${project}/agent/admin/html/tree/book.jsp" class="li-a-a" target="iframe">笔记管理</a>
								</div>
								<div class="nav-box">
									<a href="${ip}/${project}/agent/admin/html/tree/url.jsp" class="li-a-a" target="iframe">网址管理</a>
								</div>
								
								<div class="nav-box">
									<a href="${ip}/${project}/agent/admin/html/tree/article.jsp" class="li-a-a" target="iframe">美文管理</a>
								</div>
							</li>
							
							<li class="nav-tab nav-ul">
								<a href="javascript:void[0]" class="li-a"><i class='bx bx-edit'></i> 项目管理
									<i class='bx bx-chevron-right' style="float: right;"></i></a>
								<div class="nav-box">
									<a href="${ip}/${project}/agent/admin/html/tree/project.jsp" class="li-a-a" target="iframe">程序管理</a>
								</div>
								
							</li>
							
							<li class="nav-tab nav-ul">
								<a href="javascript:void[0]" class="li-a"><i class='bx bx-edit'></i> 定时任务
									<i class='bx bx-chevron-right' style="float: right;"></i></a>
								<div class="nav-box">
									<a href="${ip}/${project}/agent/admin/html/tree/tasktree.jsp" class="li-a-a" target="iframe">任务管理</a>
								</div>
							</li>
							
						</ul>
					</div>
				</div>
			</div>
			<div class="nav-right" ref="navRight">
				<div class="nav-top">
					<button type="button" class="btn btn-primary btn-sm hiddenBtn" style="line-height: 10px;" @click="isShowLeft">
						<i class="bx bx-grid-alt" style="font-size: 16px;"></i>
					</button>
					<!-- <button type="button" class="btn btn-primary btn-sm showBtn" style="line-height: 10px; display: none;" onclick="showNavRight">
						<i class="bx bx-expand" style="font-size: 16px;"></i>
					</button> -->
				</div>
				<div class="content-page" ref="cPage">
					<iframe name="iframe" width="100%" height="100%" frameborder="0" src="html/home.jsp"></iframe>
				</div>
			</div>
		</div>
		<script src="js/vue.min.js"></script>
		<script src="https://www.jq22.com/jquery/jquery-1.10.2.js"></script>
		<script type="text/javascript">
			$(function() {
				let navflag = false;
				$('.nav-tab').click(function() {
					$(this).siblings().each(function() {
						$(this).removeClass('a_active');
						// $(this).removeClass('a_active');
						$(this).find('.nav-box').css('height', '0')
						//关闭右侧箭头
						if ($(this).attr('class').indexOf('nav-ul') != -1) {
							$(this).find('.bx-chevron-right').css('transform', 'rotateZ(0deg)')
							$(this).find('.bx-chevron-right').css('transition', 'all .5s')
							$(this).removeClass('nav-show')
							// $(this).find('div').removeClass('nav-box')
						}
					})
					//当前选中
					$(this).addClass('a_active')
					$(this).find('.li-a').addClass('active')
					// 打开右侧箭头
					$(this).find('.bx-chevron-right').css('transform', 'rotateZ(90deg)')
					$(this).find('.bx-chevron-right').css('transition', 'all .5s')
					$(this).addClass('nav-show')
					// $(this).find('div').addClass('nav-box')
				})
				/* 二级菜单a点击事件 */
				$(".li-a-a").click(function() {
					$(".li-a-a").each(function() {
						$(this).removeClass('active-li-a');
					})
					$(this).addClass('active-li-a');
				})

			})
			const vue = new Vue({
				el: '#app',
				data: {
					navLeftFlag: true
				},
				methods: {
					isShowLeft() {
						if (this.navLeftFlag ) {
							this.$refs.navRight.style.paddingLeft = '0px'
							this.$refs.cPage.style.left = '0px';
							this.navLeftFlag = false;
							// this.$refs.navLeft.style.width = '0px';
							// setTimeout(()=>{
							// 	this.navLeftFlag = false;
							// },200)
						} else {
							this.$refs.navRight.style.paddingLeft = '240px';
							this.$refs.cPage.style.left = '240px';
							this.navLeftFlag = true;
							// this.$refs.navLeft.style.width = '240px';
							// setTimeout(()=>{
							// 	this.navLeftFlag = true;
							// },200)
						}
					}
				}
			})
		</script>
	</body>
</html>
