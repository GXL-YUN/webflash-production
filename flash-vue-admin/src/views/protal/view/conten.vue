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
        <el-button type="danger" icon="el-icon-delete" circle @click="delect(date.videId)"></el-button>
      </div>
    </el-row>

<!--   查看详细视频 -->
    <el-dialog
      title="Video viewing"
      :visible.sync="dialogVisibles"
      :before-close="handleClose"
      width="50%"
      style="z-index: 100; background: #000000">
      <video v-if="videoPlaying" width="100%" controls>
        <source :src="urlVodeMap" type="video/mp4">
        Your browser does not support video tags.
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
      videoPlaying: false,
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

    //删除
    delect(videId){
      this.$confirm('确认删除？')
        .then(_ => {
            // 确认则关闭对话框
          vodeApi.remove(
            videId
          ).then(response => {
            this.$message({
              message: this.$t('common.optionSuccess'),
              type: 'success'
            })
            this.back()
          })
        }).catch(_ => {
                // 取消则不做任何操作

      });






    },


    //关闭弹窗
    handleClose(done) {
      // 在这里编写你想在关闭前执行的代码
      // 例如：弹出确认对话框
    /*  this.$confirm('确认关闭？')
        .then(_ => {*/
          // 确认则关闭对话框
          this.videoPlaying = !this.videoPlaying;
          done();
      /*  })
        .catch(_ => {
          // 取消则不做任何操作

        });*/
    },
    SelectVode(videId){
      //显示voder
      this.videoPlaying = !this.videoPlaying;
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
