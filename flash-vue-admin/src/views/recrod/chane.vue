<template>

  <el-button type="text" @click="dialogFormVisible = true">打开嵌套表单的 Dialog</el-button>
  <el-dialog title="收货地址" :visible.sync="dialogFormVisible">
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="档案名称">
        <el-input v-model="form.name"></el-input>
      </el-form-item>


      <el-form-item label="档案标题">
        <el-input v-model="form.title"></el-input>
      </el-form-item>


      <el-form-item label="档案编号">
        <el-input v-model="form.docNumber"></el-input>
      </el-form-item>


      <el-form-item label="老师名称">
        <el-input v-model="form.teacher"></el-input>
      </el-form-item>


      <el-form-item label="作者">
        <el-input v-model="form.author"></el-input>
      </el-form-item>

      <el-form-item label="存放内容">
        <el-input type="textarea" v-model="form.content"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">立即创建</el-button>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="onSubmit = false">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script src="./edit.js"></script>
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

