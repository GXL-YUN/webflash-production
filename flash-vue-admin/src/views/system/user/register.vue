<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right">
    <el-row>
      <el-col :span="24">
        <el-form-item label="账户" prop="account">
          <el-input v-model="form.account" minlength=1></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" minlength=1></el-input>
        </el-form-item>
      </el-col>

      <el-col :span="24">
        <el-form-item label="性别">
          <el-radio-group v-model="form.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
<!--      <el-col :span="24">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
      </el-col>-->
      <el-col :span="24" v-show="isAdd">
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24" v-show="isAdd">
        <el-form-item label="确认密码" prop="rePassword">
          <el-input v-model="form.rePassword" type="password"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
      </el-col>
<!--      <el-col :span="24">
        <el-form-item label="所属部门" prop="deptid">
          <treeselect v-model="form.deptid" :options="deptTree.data" placeholder="请选择所属部门"/>
        </el-form-item>
      </el-col>-->
      <el-col :span="24">
        <el-form-item label="是否启用">
          <el-switch v-model="form.statusBool"></el-switch>
        </el-form-item>
      </el-col>
<!--      <el-col :span="24">
        <el-form-item label="出生日期">
          <el-date-picker type="date" placeholder="选择日期" v-model="form.birthday" style="width: 100%;">

          </el-date-picker>
        </el-form-item>
      </el-col>-->
    </el-row>
    <el-form-item>
      <el-button type="primary" @click="saveUser">{{ $t('button.submit') }}</el-button>
      <el-button @click.native="formVisible = false">{{ $t('button.cancel') }}</el-button>
    </el-form-item>
  </el-form>



</template>
<script src="./user.js"></script>
<style rel="stylesheet/scss" lang="scss" scoped>
@import "src/styles/common.scss";
</style>
