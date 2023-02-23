<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Video Test</title>
</head>
<body>
<link href="https://vjs.zencdn.net/7.11.4/video-js.css" rel="stylesheet" />
<script src="https://vjs.zencdn.net/7.11.4/video.min.js"></script>
<script src="videojs-rtsp.min.js"></script>

<video id="my-video-1" class="video-js vjs-big-play-centered" controls forcesize width="640" height="240"
    data-setup='{"techOrder": ["html5","rtsp"]}'>
    <source src="rtsp://39.103.237.224/347014.sdp" type='video/rtsp' />
</video>

</body>
</html>
