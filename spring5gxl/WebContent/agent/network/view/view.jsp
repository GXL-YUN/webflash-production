<%@ page language="java" contentType="text/html; charset=Utf-8"
    pageEncoding="Utf-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="apple-itunes-app" content="app-id=1013455241"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=10" >
    <title>来自V1921A的实时视频</title>
    <link rel="stylesheet" href="${ip}/${project}/ret/bootstrap.css" />
    <link rel="shortcut icon" href="${ip}/${project}/ret/favicon.ico" />
    <script type="text/javascript" src="${ip}/${project}/ret/flv.min.js"></script>
    <!--[if lt IE 9]>
    <script src="${ip}/${project}/ret/html5shiv.min.js"></script>
    <script src="${ip}/${project}/ret/${ip}/${project}/retpond.min.js"></script>
    <![endif]-->
    <style type="text/css">
        .scrollable-menu {
        height: auto;
        max-height: 200px;
        overflow-x: hidden;
        }
    </style>
</head>
<body>
<div class="container text-center">
    <h2>来自V1921A的实时视频</h2>
    <div>
        <video id="videoElement" class="img-thumbnail" controls autoplay >
            The browser is too old which doesn't support HTML5 video.
            浏览器太老了不支持html5视频。
        </video>
        <script type="text/javascript">
        if (flvjs.isSupported()) {
            var videoElement = document.getElementById('videoElement');
            var flvPlayer = flvjs.createPlayer({
                                               type: 'flv',
                                               url: 'http://192.168.0.111:8081/live.flv',
                                               isLive: true,
                                               withCredentials: true
                                               }, {
                                               fixAudioTimestampGap: false
                                               });
                                               flvPlayer.attachMediaElement(videoElement);
                                               flvPlayer.load();
        }
        if(window.navigator.userAgent.indexOf("Chrome") == -1 && window.navigator.userAgent.indexOf("Firefox") == -1 && window.navigator.userAgent.indexOf("Edge") == -1) {
                    alert('Currently supports Google Chrome browser, Mozilla Firefox and Microsoft Edge ONLY!');
        }
        </script>
    </div>
    <div class="btn-group">
        <button type="button" class="btn btn-danger btn-default" onclick="$.get('/getsnapshot', null, function(data) { $('#sslbutton').notify(data, 'success');});">快照</button>
        <div id="sslbutton" class="btn-group dropup">
            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" onclick="$.get('/getarchives', null, function(data) { $('#snapshotlist').html('');$('#snapshotlist').html(data); });">存档<span class="caret"></span>
            </button>
            <ul id="snapshotlist" class="dropdown-menu scrollable-menu" role="menu">
            </ul>
        </div>
        <button class="btn btn-default" data-toggle="modal" data-target="#myModal" onclick="$.get('/serverinfo', null, function(data) { $('#svrinfo').html(data); });">服务器信息</button>
        <button type="button" class="btn btn-default" onclick="rotateVideo()" id="rotate">旋转</button>
        <button type="button" class="btn btn-default" onclick="$.get('/get_motion_status', null, function(data) { if(data == '0') $('#myMotionTurnOnModal').modal(); else if(data == '1') $('#myMotionTurnOffModal').modal(); });">运动检测</button>
        <button type="button" class="btn btn-default" onclick="$.get('/light', null, function(data) { if(data != '') { $('#alertinfo').html(data); $('#myAlertModal').modal(); } });">灯光</button>
        <button type="button" class="btn btn-default" onclick="$.get('/camswitch', null, function(data) { if(data != '') { $('#alertinfo').html(data); $('#myAlertModal').modal(); } else { location.reload();} });">切换</button>
        <div id="sizebutton" class="btn-group dropup">
            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" onclick="$.get('/size', null, function(data) { $('#sizelist').html('');$('#sizelist').html(data); });">
                分辨率 <span class="caret"></span>
            </button>
            <ul id="sizelist" class="dropdown-menu scrollable-menu" role="menu">
            </ul>
        </div>
    </div>
    <div><a href="http://www.shenyaocn.com/" class="btn btn-link" target="_blank">© 2022 沈垚/ShenYao China</a>|<a href="https://play.google.com/store/apps/details?id=com.shenyaocn.android.WebCam" class="btn btn-link" target="_blank">IP摄像头</a>|<a href="https://www.microsoft.com/store/apps/9WZDNCRDMDRM" class="btn btn-link" target="_blank">for Windows</a>|<a href="https://itunes.apple.com/app/id1176420716" class="btn btn-link" target="_blank">for Mac</a>|<a href="https://itunes.apple.com/app/id990605467" class="btn btn-link" target="_blank">for iOS</a></div>
    <div><a href="https://github.com/shenyaocn/IP-Camera-Bridge" class="btn btn-link" target="_blank">IP Camera Bridge</a></div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h4 class="modal-title">IP摄像头服务器信息</h4>
            </div>
            <div class="modal-body">
                <div id="svrinfo"></div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myAlertModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h4 class="modal-title">:(</h4>
            </div>
            <div class="modal-body">
                <div id="alertinfo"></div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myMotionTurnOnModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h4 class="modal-title">运动检测</h4>
            </div>
            <div class="modal-body">
                <div>打开运动检测后，IP摄像头服务器会在检测到运动时自动录像并存储，您还可以通过Web或OneDrive查看。当15秒内检测不到运动后，录像将自动停止。打开前，请确保设备有足够的可用空间，并将设备放置于平稳的地方。运动检测会延迟10秒开启。</div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="$.get('/toggle_motion', null, function(data) { $('#myMotionTurnOnModal').modal('hide'); });">打开运动检测</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myMotionTurnOffModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h4 class="modal-title">运动检测</h4>
            </div>
            <div class="modal-body">
                <div>关闭运动检测 ?</div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="$.get('/toggle_motion', null, function(data) { $('#myMotionTurnOffModal').modal('hide'); });">关闭运动检测</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ip}/${project}/ret/jquery.js"></script>
<script type="text/javascript" src="${ip}/${project}/ret/bootstrap.js"></script>
<script type="text/javascript" src="${ip}/${project}/ret/notify.js"></script>
<script type="text/javascript">
    function onItemClick(id){
        $.get('/'+ id, null, function(data) {
              if(data != '') {
                $('#alertinfo').html(data);
                $('#myAlertModal').modal();
              }else { location.reload();}
              });
              return false;
    }
    var angle = 0;
    function rotateVideo() {
        angle = (angle + 90) % 360;
        if (angle % 180 == 0) {
            $("#videoElement").css({ "transform": "rotate(" + angle + "deg)" });
        } else {
            var img = document.getElementById("videoElement");
            var w = img.clientWidth.toFixed(3);
            var h = img.clientHeight.toFixed(3);
            var s = h / w;
            $("#videoElement").css({ "transform": "rotate(" + angle + "deg) scale(" + s + ")" });
        }
    }
</script>
</body>
</html>