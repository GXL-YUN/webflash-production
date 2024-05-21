<template>
  <div>
    <el-button @click="goPreview">点击预览word文件</el-button>
    <el-button @click="downLoad">点击下载word文件</el-button>
    <div class="docWrap">
      <!-- 预览文件的地方（用于渲染） -->
      <div ref="file"></div>
    </div>
  </div>
</template>

<script>
// 引入axios用来发请求
import axios from "axios";
// 引入docx-preview插件
let docx = require("docx-preview");
export default {
  mounted() {
    console.log("使用插件的renderAsync方法来渲染", docx); //
  },
  methods: {
    // 预览
    goPreview() {
      axios({
        method: "get",
        responseType: "blob", // 因为是流文件，所以要指定blob类型
        url: "http://ashuai.work:10000/getDoc", // 自己的服务器，提供的一个word下载文件接口
      }).then(({ data }) => {
        console.log(data); // 后端返回的是流文件
        docx.renderAsync(data, this.$refs.file); // 渲染到页面
      });
    },
    // 下载
    downLoad() {
      axios({
        method: "get",
        responseType: "blob", // 因为是流文件，所以要指定blob类型
        url: "http://ashuai.work:10000/getDoc", // 自己的服务器，提供的一个word下载文件接口
      }).then(({ data }) => {
        console.log(data); // 后端返回的是流文件
        const blob = new Blob([data]); // 把得到的结果用流对象转一下
        var a = document.createElement("a"); //创建一个<a></a>标签
        a.href = URL.createObjectURL(blob); // 将流文件写入a标签的href属性值
        a.download = "出师表.docx"; //设置文件名
        a.style.display = "none"; // 障眼法藏起来a标签
        document.body.appendChild(a); // 将a标签追加到文档对象中
        a.click(); // 模拟点击了a标签，会触发a标签的href的读取，浏览器就会自动下载了
        a.remove(); // 一次性的，用完就删除a标签
      });
    },
  },
};
</script>
<style >
.docWrap {
  width: 900px;
  overflow-x: auto;
}
</style>
