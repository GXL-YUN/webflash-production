<template>


  <div>
    <el-row class="">
      <div class="block image-container" v-for="date in list" :key="date">
<!--查看页面  查看视频
-->
        <a  @click="SelectVode(date.videId)">
          <div class="texts text-overlay">{{ date.massage }}</div>
        <el-image
          style="width: 280px; height: 300px;border-radius: 10px;margin: 12px;"
          :src="date.imgId"
          :fit="date.massage"></el-image>
        </a>
      </div>
    </el-row>

<!--   查看详细视频 -->
    <el-dialog
      title="视频查看"
      :visible.sync="dialogVisibles"
      width="50%"

      style="z-index: 100; background: #000000">
      <video width="100%" controls>
        <source :src="urlVodeMap" type="video/mp4">
        您的浏览器不支持视频标签。
      </video>
    </el-dialog>




  </div>
</template>


<script>
import articleApi from "@/api/cms/article";
import {getApiUrl} from "@/utils/utils";
import vodeApi from '@/api/vide/vide'
export default {
  data() {
    return {
      list:"",
      urlVodeMap:"",
      dialogVisibles:false,
    }
  },
  created() {
    this.init()
  },
  //初始化数据
  methods:{
    SelectVode(videId){
      this.urlVodeMap=getApiUrl() + '/file/getVideo.do?idFile='+videId

      this.dialogVisibles=true
    },
    init() {
      this.fetchDataVode()
    },
    fetchDataVode() {
      vodeApi.getList().then(response => {
        this.list = response.data.records
        for (const index in this.list) {
          const item = this.list[index]
          let img= item.imgId
          item.imgId = getApiUrl() + '/file/getImgStream?idFile=' +img
        }
      })
    }
  } ,mounted() {
    // 当组件挂载到DOM上后调用方法
    this.fetchData();
  },
}
</script>
<style>
.image-container {
  position: relative;
  display: inline-block; /* 或者其他适合的布局方式 */
}



.image-container .texts {
  position: absolute;
  top: 5%; /* 可以调整位置 */
  left: 10%; /* 可以调整位置 */
  color: white; /* 文字颜色 */
  font-size: 20px; /* 文字大小 */
  padding: 10px; /* 文字周围的空间 */
  z-index: 100;
}


.block{
  float: left;
}
.vie {
  margin: auto;
  margin-left: 5%;
  margin-right: -10%;
  margin-right: -10%;
}

.father {
  background: #f2f3f5;
}

.el-row {
  margin-bottom: 20px;
}

.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
  background: #99a9bf;
}

.bg-purple {
  background: #ffffff;
}

.bg-pur {
  background: #545454;
  color: white;
  text-align: center;
}

.bg-purple-light {
  background: #e5e9f2;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
</style>
