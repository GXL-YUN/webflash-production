import editorImage from '@/components/Tinymce'
import {Loading} from 'element-ui'
import plugins from '../../cms/article/plugins'
import toolbar from '../../cms/article/toolbar'
import articleApi from '@/api/vide/vide'
import channelApi from '@/api/cms/channel'
import {getApiUrl} from '@/utils/utils'
import {getToken} from '@/utils/auth'

export default {

  name: 'editvoid',
  components: {editorImage},
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
      ruleVideForm: {
        id: '',
        imgId: '',
        massage: '',
        videId: ""
      },


      dialogVisible: false,
      uploadUrl: '',
      uploadFileId: '',
      uploadHeaders: {
        'Authorization': ''
      },
      loadingInstance: {},
      formimg: {
        title: '',
        author: '',
        idChannel: '1',
        content: '',
        img: ''
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
      }
    }
  },
  computed: {
    language() {
      return this.languageTypeList[this.$store.getters.language]
    }
  },
  watch: {
    value(val) {
      if (!this.hasChange && this.hasInit) {
        this.$nextTick(() =>
          window.tinymce.get(this.tinymceId).setContent(val || ''))
      }
    },
    language() {
      this.destroyTinymce()
      this.$nextTick(() => this.initTinymce())
    }
  },
  mounted() {
    this.init()
    this.initTinymce()
  },
  activated() {
    this.initTinymce()
  },
  deactivated() {
    this.destroyTinymce()
  },
  destroyed() {
    this.destroyTinymce()
  },
  methods: {

    init() {
      this.uploadUrl = getApiUrl() + '/file'
      this.uploadHeaders['Authorization'] = getToken()
  /*    const id = this.$route.query.id*/
     /* if (id) {
        articleApi.get(id).then(response => {
          this.form = response.data
          this.setContent(response.data.content)
          this.articleImg = this.uploadUrl + '/getImgStream?idFile=' + response.data.img
          this.ifUpload = false
        })
      }*/
     /* channelApi.getList().then(response => {
        this.options = response.data
      })*/
    },
    initTinymce() {
      const _this = this
      window.tinymce.init({
        value: 'aaaaaa',
        language: this.language,
        selector: `#${this.tinymceId}`,
        height: this.height,
        body_class: 'panel-body ',
        object_resizing: false,
        toolbar: this.toolbar.length > 0 ? this.toolbar : toolbar,
        menubar: this.menubar,
        plugins: plugins,
        end_container_on_empty_block: true,
        powerpaste_word_import: 'clean',
        code_dialog_height: 450,
        code_dialog_width: 1000,
        advlist_bullet_styles: 'square',
        advlist_number_styles: 'default',
        imagetools_cors_hosts: ['www.tinymce.com', 'codepen.io'],
        default_link_target: '_blank',
        link_title: false,
        nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
        init_instance_callback: editor => {
          if (_this.value) {
            editor.setContent(_this.value)
          }
          _this.hasInit = true
          editor.on('NodeChange Change KeyUp SetContent', () => {
            this.hasChange = true
            const content = editor.getContent()
            this.form.content = content
          })
        },
        setup(editor) {
          editor.on('FullscreenStateChanged', (e) => {
            _this.fullscreen = e.state
          })
        }
      })
    },
    destroyTinymce() {
      const tinymce = window.tinymce.get(this.tinymceId)
      if (this.fullscreen) {
        tinymce.execCommand('mceFullScreen')
      }

      if (tinymce) {
        tinymce.destroy()
      }
    },
    setContent(value) {
      window.tinymce.get(this.tinymceId).setContent(value)
    },
    getContent() {
      window.tinymce.get(this.tinymceId).getContent()
    },
    imageSuccessCBK(arr) {
      const _this = this
      arr.forEach(v => {
        window.tinymce.get(_this.tinymceId).insertContent(`<img class="wscnph" src="${v.url}" >`)
      })
    },
    save() {


      if(this.ruleVideForm.videId==""&&this.ruleVideForm.imgId){

        this.$message({
          message: '附件上传未完成',
          type: 'error'
        })
      }else{

      this.$refs['ruleVideForm'].validate((valid) => {
        debugger;
        if (valid) {
          //const content = this.ruleVideForm.content.split('%').join('%25')
          articleApi.save({
            id: this.ruleVideForm.id,
            imgId: this.ruleVideForm.imgId,
            massage: this.ruleVideForm.massage,
            videId: this.ruleVideForm.videId
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
    },
    back() {

        this.ruleVideForm.id=""
        this.ruleVideForm.imgId=""
        this.ruleVideForm.massage=""
        this.ruleVideForm.videId=""
      //需要将from表单数据清除
      this.dialogVisible=false//将对话框移动
     // this.$router.go(-1)
    },

    //文件視頻處理
    beforeUpload(file) {
      const isVideo = file.type === 'video/mp4' || file.type === 'video/ogg';
      const isLt500M = file.size / 1024 / 1024 < 100;
      if (!isVideo) {
        this.$message.error('上传的视频只能是 MP4 或 OGG 格式!');
      }
      if (!isLt500M) {
        this.$message.error('上传视频大小不能超过 100MB!');
      }
      return isVideo && isLt500M;
    },
    handleSuccess(response, file, fileList) {
      // 处理成功的响应
      debugger;
      this.ruleVideForm.videId =response.data.id;
      //this.ruleVideForm.imgId =response.data.id;
      console.log('Upload success:', response);
    },
    handleError(err, file, fileList) {
      // 处理上传错误
      console.error('Upload error:', err);
    },


    //图片处理 数据

    handImgleSuccess(response, file, fileList) {
      // 处理成功的响应
      debugger;
      //this.ruleVideForm.videId =response.data.id;
      this.ruleVideForm.imgId =response.data.id;
      console.log('Upload success:', response);
    },




    handleBeforeUpload() {
      if (this.uploadFileId !== '') {
        this.$message({
          message: this.$t('common.mustSelectOne'),
          type: 'warning'
        })
        return false
      }
      this.loadingInstance = Loading.service({
        lock: true,
        text: this.$t('common.uploading'),
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    },
    handleUploadSuccess(response, raw) {
      this.loadingInstance.close()
      if (response.code === 20000) {
        this.form.img = response.data.id
      } else {
        this.$message({
          message: this.$t('common.uploadError'),
          type: 'error'
        })
      }
    },
    uploadImg() {
      this.ifUpload = !this.ifUpload
    }
  }
}
