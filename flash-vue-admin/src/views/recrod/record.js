import recordApi from '@/api/record/record'
import permission from '@/directive/permission/index.js'

export default {
  name: 'record',
  directives: {permission},
  data() {
    return {
      formVisible: false,
      formTitle: '添加栏目',
      deptList: [],
      isAdd: true,
      form: {
        id: '',
        name: '',
        title: '',
        docNumber: '',
        teacher: '',
        author: '',
        content: ''
      },
      total: 0,
      list: null,
      listLoading: true,
      selRow: {},
      dialogFormVisible: false,
      formLabelWidth: '120px'
    }
  },
  created() {
    this.init()
  },
  methods: {

    back() {
      this.$router.go(-1)
    },
    editItem(record) {

      this.selRow = Object.assign({}, record);
      this.edit()
    },
    edit() {
      if (this.checkSel()) {
        this.isAdd = false
        this.form = this.selRow
        //this.formTitle = '编辑消息模板'
       this.dialogFormVisible = true
      }
    },
    checkSel() {
      if (this.selRow && this.selRow.id) {
        return true
      }
      this.$message({
        message: this.$t('common.mustSelectOne'),
        type: 'warning'
      })
      return false
    },

   //新建页面数据
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
    },
    //删除数据
    removeItem(id) {
      this.listLoading = true
      recordApi.remove({id: id}).then(response => {
        this.list = response.data
        this.listLoading = false
        this.fetchData()
      })
     //更新数据
    },
    init() {
      this.fetchData()
    },
    //获取数据
    fetchData() {
      this.listLoading = true
      recordApi.getList().then(response => {
        this.list = response.data
        this.listLoading = false
      })
    },
  }
}
