import recordApi from '@/api/record/record'

export default {
  name: 'recordApi',
  props: {
    id: {
      type: String,
      default: function () {
        return 'vue-tinymce-' + +new Date() + ((Math.random() * 1000).toFixed(0) + '')
      }
    },
    value: {
      type: String,
      default: ''
    },
    toolbar: {
      type: Array,
      required: false,
      default() {
        return []
      }
    },
    menubar: {
      type: String,
      default: 'file edit insert view format table'
    },
    height: {
      type: Number,
      required: false,
      default: 360
    }
  },
  data() {
    return {
      uploadUrl: '',
      uploadFileId: '',
      uploadHeaders: {
        'Authorization': ''
      },
      loadingInstance: {},
      form: {
        id: '111',
        name: '11',
        title: '111',
        docNumber: '111',
        teacher: '11',
        author: '11',
        content: '111111'
      },
      articleImg: '',
      ifUpload: true,
      options: [],
      hasChange: false,
      hasInit: false,
      tinymceId: this.id,
      fullscreen: false,
      languageTypeList: {
        'en': 'en',
        'zh': 'zh_CN'
      },
      dialogFormVisible: false,
      formLabelWidth: '120px'
    }
  },

  methods: {
    onSubmit() {

      console.log('submit!');
      console.log('执行接口数据添加成功');
      this.$refs['form'].validate((valid) => {

        if (valid) {
          const content = this.form.content.split('%').join('%25')
          recordApi.upDate({
            id: this.form.id,
            title: this.form.title,
            name: this.form.name,
            docNumber: this.form.docNumber,
            author: this.form.author,
            teacher: this.form.teacher,
            content: content,
            img: this.form.img
          }).then(response => {
            this.$message({
              message: this.$t('common.optionSuccess'),
              type: 'success'
            })
            this.back()
          })
        } else {
          return false
        }
      })
    }
  }
}
