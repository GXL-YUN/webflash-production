//ajax数据

//时间戳转时间
function formatDate(time) {
	var date = new Date(parseInt(time));
	var year = date.getFullYear();
	var mon = date.getMonth() + 1;
	var day = date.getDate();
	return year + '/' + mon + '/' + day;
}

//获取url参数
function getpan(variable)
{
       var query = window.location.search.substring(1);
       var vars = query.split("&");
       for (var i=0;i<vars.length;i++) {
               var pair = vars[i].split("=");
               if(pair[0] == variable){return pair[1];}
       }
       return(false);
}
// 状态改变
function changestart(num) {
	if (num == 0) {
		return "开启";
	}
	return "关闭";
}
// 删除数据数据
function delecttr(t1, t2) {

	var msg = "您真的确定要删除吗？\n\n请确认！";
	if (confirm(msg) == true) {
		$("." + t2).remove();

		delect(t1, t2);
		return true;
	} else {
		return false;
	}

}

// .citablepager查找到标签并且进行数据的循环
// 查询数据
function getajax(url) {
	var date;
	// 执行ajax
	var dataurl = getip() + "/" + url;

	$.ajax({
		type : "get",
		url : dataurl,
		async : false,
		dataType : "json",
		success : function(jsonData, status, xhr) {
			// getsuccess("数据加载成功");
			date = jsonData;
		},
		error : function() {
			getinfo("数据错误，未显示");
		}
	});
	return date;
}
// 删除数据
function delect(url, id) {
	var date;
	// 执行ajax
	var dataurl = getip() + "/" + url + "?fd_id=" + id;
	$.ajax({
		type : "get",
		url : dataurl,
		async : false,
		dataType : "json",
		success : function(jsonData, status, xhr) {
			getsuccess("数据删除成功");
			date = jsonData;
		},
		error : function() {
			getinfo("数据错误，请检查数据格式");
		}
	});
	return date;
}

function getip() {
	var curPath = window.document.location.href;
	var pathname = window.document.location.pathname;
	var pos = curPath.indexOf(pathname);
	var localhostPath = curPath.substring(0, pos);

	return localhostPath;
}

// 遮罩
function coverDiv() {
	var procbg = document.createElement("div"); // 首先创建一个div
	procbg.setAttribute("id", "mybg"); // 定义该div的id
	procbg.style.background = "rgba(74, 168, 206, 0.65)";
	procbg.style.width = "100%";
	procbg.style.height = "100%";
	procbg.style.position = "fixed";
	procbg.style.top = "0";
	procbg.style.left = "0";
	procbg.style.zIndex = "500";
	procbg.style.opacity = "0.6";
	procbg.style.filter = "Alpha(opacity=70)";
	document.body.appendChild(procbg);
}
// 取消遮罩

function hide() {

	/* document.getElementById('light').style.display="none"; */

	$("div[class='xucun_content']").hide();

	var body = document.getElementsByTagName("body");

	var mybg = document.getElementById("mybg");

	body[0].removeChild(mybg);

}

/* * 提示信息 */
// info,success,warning,error
function getinfo(massage) {
	let type = 'info';
	var options = {
		content : "提示内容:" + massage,
		type : type,// info,success,warning,error
	// autoClose : false
	}
	$.jqAlert(options);
}

function getsuccess(massage) {
	let type = 'success';
	var options = {
		content : "提示内容:" + massage,
		type : type,// info,success,warning,error
	// autoClose : false
	}
	$.jqAlert(options);
}

function getwarning(massage) {
	let type = 'warning';
	var options = {
		content : "提示内容:" + massage,
		type : type,// info,success,warning,error
	// autoClose : false
	}
	$.jqAlert(options);
}

function geterror(massage) {
	let type = 'error';
	var options = {
		content : "提示内容:" + massage,
		type : type,// info,success,warning,error
	// autoClose : false
	}
	$.jqAlert(options);
}
(function($) {
	$
			.extend({
				jqAlert : function(option) {
					let _this = this;
					var settings = {
						type : 'info', // info,success,warning,error
						content : '提示内容:',
						autoClose : true
					};
					var $dom = $('.my_alert-wrapper');
					if ($dom.length === 0) {
						$(document.body).append(
								'<div class="my_alert-wrapper"></div>');
					}
					$dom = $('.my_alert-wrapper');
					$.extend(settings, option);
					let box = $('<div class="my_alertBox" animation=""></div>');
					box.addClass('my_alertBox--' + settings.type);
					let typeIcon = $('<i class="my_alert-icon iconfont"></i>');
					typeIcon.addClass('icon-alert-' + settings.type);
					let contentBox = $('<div class="my_alert-content"></div>');
					contentBox.text(settings.content);
					let closeIcon = $('<i class="my_alert-closebtn iconfont icon-close"></i>');
					box.append(typeIcon).append(contentBox).append(closeIcon);
					$dom.append(box);
					if (settings.autoClose === true) {
						setTimeout(function() {
							box.remove();
						}, 4 * 1000);
					}
					closeIcon.on('click', function() {
						box.remove();
					});
				}
			});
})(jQuery);


//获取css外联文件中属性和属性值

function getstyle(){
	var cssfile=document.styleSheets[0].cssRules;
	return cssfile;
}

