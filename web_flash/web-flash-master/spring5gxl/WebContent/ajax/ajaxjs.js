/**
 *   使用原生js处理后端放回的数据流
 */

/**
 * 创建XMLHttpRequest对象 因为基于XMLHttpRequest对象实现
 * @returns
 */

function creatxhttp(){
	var xhttp;
	if (window.XMLHttpRequest) {
	    xhttp = new XMLHttpRequest();
	    } else {
	    // code for IE6, IE5
	     xhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
}


