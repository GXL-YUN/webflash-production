export default {
  name: 'files',
  data() {
    return {
      form: {
        fileList: [], // 文件列表
      },
      id: '11',
    }
  },
  methods: {
    // 选中文件
    onChange(file) {
      this.form.fileList.push(file)
      //传递参数文件到副组件数据  附件文件数据列表
      this.$emit("getData", file);
    },

    // 移除文件
    onRemove() {
      this.form.fileList = []
      this.$refs.upload.clearFiles()
    },

    // 确定
    onSure() {
      debugger;
      const formData = new FormData()
      //把接口需要的参数带进去
      formData.append('id', this.id)
      formData.append('file', this.fileList[0].raw)
      //点击确定按钮调接口
      console.log(formData)
      return formData;

    },
  },
}
