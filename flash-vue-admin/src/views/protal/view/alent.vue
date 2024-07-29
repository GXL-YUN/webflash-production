<template>


  <div class="top">


    <div v-if="flage==true">
      <add></add>

    </div>
      <div class="">
          <div class="font "><h1>AI Sora Video</h1><br></div>
          <div class="font"><h1>OpenAI Sora Creative video</h1><br></div>
          <div class="container"><h1>And Prompt Collection Platform</h1><br></div>
          <div class="container"><h2>The latest news OpenAI Sora A video about new artificial intelligence technologies</h2><br></div>
          <div class="container"> <h3>93+ Video</h3><br></div>

        <div class="container">

          <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right">

            <el-row>
              <el-col :span="24">
                <el-form-item  prop="email">
                  <div class="test"> <el-input v-model="form.email"></el-input>  <el-button  @click="send()">subscriber</el-button> </div>
                <br>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form-item>
          </el-form-item></div>
      </div>

    <div>


      <el-dialog
        title=""
        :visible.sync="dialogVisible"
        width="50%"
        :before-close="handleClose">
        <login @send-data="getDataFromChild"></login>
      </el-dialog>
    </div>
  </div>



</template>

<script>
import Add from "@/views/protal/view/add";
import {getApiUrl} from "@/utils/utils";
import userApi from '@/api/vide/vide'
import Cookies from 'js-cookie'
import emilApi from "@/api/message/message";
import {getToken} from "@/utils/auth";
import login from '../../login/index'
export default {
  components: {Add,login},
  data() {
    return {
      flage:null,
      form:{
        email:""
      },
      dialogVisible:false,
      uploadUrl: '',
      uploadHeaders: {
        'Authorization': ''
      },
    }
  },
  created() {
    this.init()
  },
  close(){
    this.closeDolig()
  },
  methods: {

    getDataFromChild(data) {
      debugger;
      this.dialogVisible=data
      console.log('从子组件接收的数据:', data);
    },
   //查询当前用户是否是管理员
    init() {
      debugger
      this.getUseres()
      this.uploadUrl = getApiUrl()
      this.uploadHeaders['Authorization'] = getToken()
    },

    mounted: function mounted() {
      debugger
      this.getUseres();
    },
    getUseres() {
      userApi.getUser().then(response => {
        debugger
        this.flage = response.data
      })
    },
    send() {
      //判断是否登录
      debugger

      let  ser =Cookies.get("vue_admin_template_token");
      if (ser == undefined ||ser=='') {
       //未登录
        /*this.$message({
          message: 'not log in',
          type: 'success'
        })*/
        //打开弹窗

        this.dialogVisible=true;
        //this.$router.push('/login');
      }else{
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
      }
    },
    //校验邮箱
     validateEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
     }
  }
}
</script>
<style>
.font{
  font-size: 2rem;
  letter-spacing: -.025em;
  height: 70px;
  text-align: center;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}
.el-input {
  position: relative;
  font-size: 14px;
  display: inline-block;
  width: 50%;
  background: rebeccapurple;
  border: 3px solid #eee9f2;
  border-radius: 10px;
  background: radial-gradient(circle at right bottom, transparent 50%, #fff 50%);
  /*background-size: 50% 100%;*/
  background-repeat: no-repeat;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
}

.test{
  margin-left: -20%;

}


.top{

  margin-top: 7%;
}
.container {

  letter-spacing: -.025em;
  height: 25px;
  text-align: center;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}
</style>
