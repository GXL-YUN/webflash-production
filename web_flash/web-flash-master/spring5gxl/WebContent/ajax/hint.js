/**
 * 
 */
  //<link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">  
  //<script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>


/**
 * 使用的前提是引入文件
 */


/**
 * 弹出式提示框，默认1.2秒自动消失
 * @param message 提示信息
 * @param style 提示样式，有alert-success、alert-danger、alert-warning、alert-info
 * @param time 消失时间
 */

//提示


var prompt = function (message, style, time)
{
    style = (style === undefined) ? 'alert-success' : style;
    time = (time === undefined) ? 1200 : time;
    $('<div id="promptModal">')
        .appendTo('body')
        .addClass('alert '+ style)
        .css({"display":"block",
            "z-index":99999,
          // "left":($(document.body).outerWidth(true) - 120) / 2,
            "top":($(window).height() - 45) / 2,
            "position": "absolute",
            "padding": "20px",
            "border-radius": "5px"})
        .html(message)
        .show()
        .delay(time)
        .fadeOut(10,function(){
            $('#promptModal').remove();
        });
};
 
// 成功提示

    prompt("移动端不支持附件下载,\n如需查看请复制到浏览器", 'alert-success', 3000);

 
// 失败提示

    prompt(message, 'alert-danger', time);

 
// 提醒

    prompt(message, 'alert-warning', time);

 
// 信息提示

    prompt(message, 'alert-info', time);

 
// 信息提示

    prompt(message, 'alert-pormpt', time);


