/**
 * 
 * @param {*} title  //弹框标-数据
 * @param {*} width  //宽度
 * @param {*} height //高度
 * @param {*} content  //弹出层id
 * @param {*} url   //路径
 */


function CreatePopLayerDiv(title, width, height, content, url) {
  var titles = title || "标题";
  var Iheight = $(window).height();
  var Iwidth = $(window).width();
  var heights = height || 300;
  var widths = width || 500;
  var Oheight = (Iheight - heights) / 2;
  var Owidth = (Iwidth - widths) / 2;
  var data;

  //通过ajax获取页面数据
  $.ajax({
    type: "GET",
    url: url,
    async: false,    //用同步方式
    dataType: 'text',
    success: function (Data) {
      data = Data;
    },
    error: function (arg3) {
      console.log("页面数据获取失败:" + url);
    }
  });
  var contents = data || "内容";
  var div = "<div id='InDiv' style='width:" + Iwidth + "px;height:" + Iheight
    + "px;background-color:#888;position:absolute;z-index:10000;top:0;left:0;opacity:0.7; '>" +
    "<div id='offDiv' style='width:" + widths + "px;height:" + heights + "px;left:" + Owidth +
    "px;top:" + Oheight + "px;background-color:white;position:absolute;z-index:10001;overflow:auto'>" +
    "<h4 id='HTitle' style='margin:0px;padding:3px;background:#336699;opacity:0.9;border:1px solid ##336699;height:20px;line-height:20px;font:12px Verdana, Geneva, Arial, Helvetica, sans-serif;color:white;cursor:pointer;text-align:left;'>"
    + titles + "<a id='AClose' onclick='btnCloses()' style='float:right;'>关闭</a></h4><div id='Content'>"
    + contents + "</div></div></div>";

  $(document.body).append(div);
  if (url != "") {
    $("#Content").load(url);
  }
}


function dialog(title, width, height, content, url) {
  var titles = title || "标题";
  var Iheight = $(window).height();
  var Iwidth = $(window).width();
  var heights = height || 300;
  var widths = width || 500;
  var Oheight = (Iheight - heights) / 2;
  var Owidth = (Iwidth - widths) / 2;
  var data;

  //通过ajax获取页面数据
  $.ajax({
    type: "GET",
    url: url,
    async: false,    //用同步方式
    dataType: 'text',
    success: function (Data) {
    	
    	var flage=Data.includes("gxl");

      data = Data;
    },
    error: function (arg3) {
      console.log("页面数据获取失败:" + url);
    }
  });
  var contents = data || "内容";
  var div = "<div id='InDiv' style='width:" + Iwidth + "px;height:" + Iheight
    + "px;background-color:#888;position:absolute;z-index:10000;top:0;left:0;opacity:0.9;'>" +
    "<div id='offDiv' style='width:" + widths + "px;height:" + heights + "px;left:" + Owidth +
    "px;top:" + Oheight + "px;background-color:white;position:absolute;z-index:10001;overflow:auto'>" +
    "<h4 id='HTitle' style='margin:0px;padding:3px;background:#336699;opacity:0.9;border:1px solid ##336699;height:20px;line-height:20px;font:12px Verdana, Geneva, Arial, Helvetica, sans-serif;color:white;cursor:pointer;text-align:left;'>"
    + titles + "<a id='AClose' onclick='btnCloses()' style='float:right;'>关闭</a></h4><div id='Content'>"
    + contents + "</div></div></div>";

  $(document.body).append(div);
  if (url != "") {
    $("#Content").load(url);
  }
}

/**
 * 两种方式
 */


function btnCloses() {
  RemoveDiv();
}
//移除弹出层
function RemoveDiv() {
  $("#AClose").remove();
  $("#HTitle").remove();
  $("#offDiv").remove();
  $("#InDiv").remove();
}