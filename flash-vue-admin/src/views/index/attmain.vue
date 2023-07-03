<template><el-upload
  class="upload-demo"
  ref="upload"
  action="https://jsonplaceholder.typicode.com/posts/"
  :on-preview="handlePreview"
  :on-remove="handleRemove"
  :on-change="onAdd"
  :file-list="fileList"
  :auto-upload="false">
<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
<el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
<div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
</el-upload>
</template>
<script>
export default {
  name: 'files',
  data() {
    return {
      fileList: [
        {
          "status": "ready",
          "name": "联想截图_20230323182402.png",
          "size": 486414,
          "percentage": 0,
          "uid": 1683856582101,
          "url":""
        }

      ],
    };
  },
  methods: {

    onAdd(file,s) {
      debugger;
      this.fileList.push(file)
      //fileList.push(file)
      //传递参数文件到副组件数据  附件文件数据列表
      this.$emit("giveFather",  this.fileList);
    },


    submitUpload() {
      this.$refs.upload.submit();
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    }
  }
}
</script>
