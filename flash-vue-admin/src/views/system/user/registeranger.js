import {deleteUser, getList, saveUser, remove, setRole, changeStatus, resetPassword} from '@/api/system/user'
import deptApi from '@/api/system/dept'
import {parseTime} from '@/utils/index'
import roleApi from '@/api/system/role'
// 权限判断指令
import permission from '@/directive/permission/index.js'
import  emilApi from '@/api/message/message'
import {getTokens} from "@/utils/auth";
import Cookies from 'js-cookie'
export default {
  name: 'mgr',
  directives: {permission},
  data() {
    return {
      roleDialog: {
        visible: false,
        roles: [],
        roleTree: [],
        checkedRoleKeys: [],
        defaultProps: {
          id: 'id',
          label: 'name',
          children: 'children'
        }
      },
      statusList: [
        {label: '启用', value: '1'},
        {label: '冻结', value: '2'}
      ],
      formVisible: false,
      formTitle: '添加用户',
      deptTree: {
        data: [],
      },
      isAdd: true,


      form: {
        id: '',
        actioncodeNew:'',
        endcodeData:"",
        account: '',
        name: '',
        birthday: '',
        sex: 1,
        email: '',
        password: '',
        rePassword: '',
        dept: '',
        statusBool: true,
        deptid: undefined
      },
      rules: {
        account: [
          {required: true, message: 'Please enter your login account', trigger: 'blur'},
          {min: 3, max: 20, message: 'The length is 2 to 20 characters', trigger: 'blur'}
        ],
        name: [
          {required: true, message: 'Please enter your username', trigger: 'blur'},
          {min: 2, max: 20, message: 'The length is 2 to 20 characters', trigger: 'blur'}
        ],
        deptid: [
          {required: true, message: 'Please select a department', trigger: 'blur'}
        ],
        email: [
          {required: true, message: 'Please enter email', trigger: 'blur'}
        ],

        endcodeData: [
          {required: true, message: 'please enter the verification code ', trigger: 'blur'}
        ]
      },
      listQuery: {
        page: 1,
        limit: 20,
        account: undefined,
        name: undefined,
        deptid: undefined,
        phone: undefined,
        status: undefined,
        sex: undefined
      },
      total: 0,
      list: null,
      listLoading: true,
      selRow: {}
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  created() {
    this.init()
  },
  methods: {


    reset() {
      this.listQuery.account = ''
      this.listQuery.name = ''
      this.listQuery.page = 1
      this.listQuery.deptid = ''
      this.listQuery.status = ''
      this.listQuery.phone = ''
      this.listQuery.sex = ''
      this.fetchData()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleClose() {

    },
    fetchNext() {
      this.listQuery.page = this.listQuery.page + 1
      this.fetchData()
    },
    fetchPrev() {
      this.listQuery.page = this.listQuery.page - 1
      this.fetchData()
    },
    fetchPage(page) {
      this.listQuery.page = page
      this.fetchData()
    },
    changeSize(limit) {
      this.listQuery.limit = limit
      this.fetchData()
    },
    handleCurrentChange(currentRow, oldCurrentRow) {
      this.selRow = currentRow
    },
    resetForm() {
      this.form = {
        id: '',
        account: '',
        name: '',
        birthday: '',
        sex: 1,
        email: '',
        password: '',
        rePassword: '',
        dept: '',
        statusBool: true,
        deptid: undefined
      }
    },
    add() {
      this.resetForm()
      this.formTitle = 'add users'
      this.formVisible = true
      this.isAdd = true
    },


//发送邮箱验证码
    sendEmli(){


      //判断邮箱是否输入
      //已登录发送视频到邮箱
      if(this.form.email!=""){

        if(this.validateEmail(this.form.email)){

          emilApi.sendVode(this.form.email,this.uploadUrl).then(response => {
            this.$message({
              message: 'send successfully',
              type: 'success'
            })

          })
        }else{
          this.$message({
            message:  'Incorrect email address',
            type: 'error'
          })
        }
      }else{

        this.$message({
          message: 'No email',
          type: 'error'
        })
      }




    },
    //校验邮箱
    validateEmail(email) {
      const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return regex.test(email);
    },
    changeUserStatus(record) {
      changeStatus(record.id).then(response => {
        this.$message({
          message: 'submit successfully',
          type: 'success'
        })
        this.fetchData()
      })
    },
    validPasswd() {
      if (!this.isAdd) {
        return true
      }

      if (this.form.password !== this.form.rePassword) {
        this.$message({
          message: 'The password before and after is inconsistent',
          type: 'error'
        })
        return false
      }
debugger
   let code=   Cookies.get("code_vue_g")
      if (code !== this.form.endcodeData) {
        this.$message({
          message: 'The auth code is incorrect',
          type: 'error'
        })
        return false
      }


      if (this.form.password === '' || this.form.rePassword === '') {
        this.$message({
          message: 'The password cannot be empty',
          type: 'error'
        })
        return false
      }
      Cookies.remove("code_vue_g")
      return true
    },
    saveUser() {
      debugger
      var self = this
      this.$refs['form'].validate((valid) => {
        debugger
        if (valid) {
          if (this.validPasswd()) {
            var form = self.form
            console.log('form.status', form.status);
           /* if (form.statusBool === true) {
           */   //启用
              form.status = 1
          /*  } else {
              //冻结
              form.status = 2
            }*/
            form.birthday = parseTime(form.birthday, '{y}-{m}-{d}')
            form.createtime = parseTime(form.createtime)
            form.dept = null
            saveUser(form).then(response => {
              this.form.account=""
              this. form.name=""
              this. form.rePassword=""
              this.  form.password=""
              this.  form.email=""
              this.  form.endcodeData=""
              this.$message({
                message: 'submit successfully',
                type: 'success'
              })
              this.formVisible = false
            })
          }
        } else {
          return false
        }
      })
    },
    checkSel() {
      if (this.selRow && this.selRow.id) {
        return true
      }
      this.$message({
        message: 'Please select the operation item',
        type: 'warning'
      })
      return false
    },
    editItem(record) {
      this.selRow = Object.assign({}, record);
      this.edit()
    },
    edit() {
      if (this.checkSel()) {
        this.isAdd = false
        let form = Object.assign({}, this.selRow);
        form.statusBool = form.statusName === '启用'
        form.password = ''
        this.form = form
        this.formTitle = '修改用户'
        this.formVisible = true
      }
    },
    removeItem(record) {
      this.selRow = record
      this.remove()
    },
    remove() {
      if (this.checkSel()) {
        var id = this.selRow.id

        this.$confirm('确定删除该记录?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          remove(id).then(response => {
            this.$message({
              message: '删除成功',
              type: 'success'
            })
            this.fetchData()
          })
        }).catch(() => {
        })
      }
    },

    resetPwd() {
      if (this.checkSel()) {
        var id = this.selRow.id
        this.$confirm('密码将重置为111111?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          resetPassword(id).then(response => {
            this.$message({
              message: '重置密码成功',
              type: 'success'
            })
          }).catch(err => {
            this.$notify.error({
              title: '错误',
              message: err,
            })
          })
        }).catch(() => {
        })
      }
    },
    chooseDept(data, node) {
      this.listQuery.deptid = data.id
      this.search()
    },
    openRoleItem(record) {
      this.selRow = record
      this.openRole()
    },
    openRole() {
      if (this.checkSel()) {
        roleApi.roleTreeListByIdUser(this.selRow.id).then(response => {
          this.roleDialog.roles = response.data.treeData
          this.roleDialog.checkedRoleKeys = response.data.checkedIds
          this.roleDialog.visible = true
        })
      }
    },
    setRole() {
      var checkedRoleKeys = this.$refs.roleTree.getCheckedKeys()
      var roleIds = ''
      for (var index in checkedRoleKeys) {
        roleIds += checkedRoleKeys[index] + ','
      }
      var data = {
        userId: this.selRow.id,
        roleIds: roleIds
      }
      setRole(data).then(response => {
        this.roleDialog.visible = false
        this.fetchData()
        this.form.account=""
        this. form.name=""
        this. form.rePassword=""
        this.  form.password=""
        this.$message({
          message: 'submit successfully',
          type: 'success'
        })


      })
    }

  }
}
