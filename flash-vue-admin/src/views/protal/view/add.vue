<template>
<!--新建页面
-->


    <div class="block">


      <el-button type="text" @click="dialogVisible = true">新增视频</el-button>



      <div>
      <el-dialog
        title="提示"
        :visible.sync="dialogVisible"
        width="40%"
        :before-close="handleClose" style="z-index: 10000;">

        <el-form :model="ruleVideForm" :rules="rules" ref="ruleVideForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="视频描述" prop="name">

            <el-input v-model="ruleVideForm.massage"></el-input>
          </el-form-item>

          <el-form-item label="题图"  v-if="ifUpload">
            <el-upload
              class="video-uploader"
              action="/dev-api/file"
              :headers="uploadHeaders"
              :on-success="handImgleSuccess"
              :on-error="handleError"
              limit="1"
             >
              <el-button slot="trigger" size="small" type="primary">选择图片</el-button>
              <div slot="tip" class="el-upload__tip">只能上传.png/.jpg格式文件</div>
            </el-upload>
          </el-form-item>

          <el-form-item label="视频"  v-if="ifUpload">
            <el-upload
              class="video-uploader"
              action="/dev-api/file"
              :headers="uploadHeaders"
              :on-success="handleSuccess"
              :on-error="handleError"
              :before-upload="beforeUpload"
              limit="1"
              accept="video/mp4, video/ogg, video/*">
              <el-button slot="trigger" size="small" type="primary">选择视频</el-button>
              <div slot="tip" class="el-upload__tip">只能上传.mp4/.ogg格式视频文件</div>
            </el-upload>
          </el-form-item>



          <el-form-item>
            <el-button type="primary" @click="save">立即创建</el-button>

          </el-form-item>



        </el-form>
      </el-dialog>
      </div>

    </div>

</template>

<script src="../js/add.js"></script>

<style scoped>



.tinymce-container {
  position: relative;
  line-height: normal;
}

.tinymce-container >>> .mce-fullscreen {
  z-index: 10000;
}

.tinymce-textarea {
  visibility: hidden;
  z-index: -1;
}

.editor-custom-btn-container {
  position: absolute;
  right: 4px;
  top: 4px;
  /*z-index: 2005;*/
}

.fullscreen .editor-custom-btn-container {
  z-index: 10000;
  position: fixed;
}

.editor-upload-btn {
  display: inline-block;
}
</style>
