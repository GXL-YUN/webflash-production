<template>

  <!--      <h1>App2组件</h1>
        &lt;!&ndash; <a href="#/home">首页</a>
        <a href="#/movie">电影</a>
        <a href="#/about">关于</a> &ndash;&gt;
        &lt;!&ndash; 当安装和配置了vue-router之后，就可以使用router-link来替代普通的a链接 &ndash;&gt;
        <router-link to="/home">首页</router-link>
        <router-link to="/movie">电影</router-link>
        <router-link >关于</router-link>
      &lt;!&ndash; 只要在项目中安装并且配置了vue-router，就可以使用router-view这个组件 &ndash;&gt;
      &lt;!&ndash; 作用：占位符 &ndash;&gt;
       <router-view/>-->
<div class="father">
  <div class="vie app-container" >
    <el-row :gutter="20">
      <el-col :span="20">
        <div class="grid-content bg-purple"> <carousel/></div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="15">
        <div class="grid-content bg-purple">
          <el-tabs v-model="activeName" @tab-click="handleClick">

            <el-tab-pane label="首页" name="fourth">
              <div>
               <appName/>
              </div>
            </el-tab-pane>
            <el-tab-pane label="在线网址" name="first">
              <div>
                <appName/>
              </div>
            </el-tab-pane>
            <el-tab-pane label="精选文章" name="second">
              <div>
                <appName/>
              </div>
            </el-tab-pane>
            <el-tab-pane label="流程管理" name="third">
              <div>
                <configEdit/>
              </div>
            </el-tab-pane>

          </el-tabs>
        </div>
      </el-col>
      <el-col :span="5">
        <div class="grid-content bg-purple">   <configEdit/></div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="20">
        <div class="grid-content bg-pur">数据</div>
      </el-col>
    </el-row>
  </div></div>
</template>
<script src="../src/api/protal/protal.js"/>
<style>

.vie{
  margin: auto;
  margin-left: 5%;
  margin-right: -10%;
  margin-right: -10%;
}
.father{
  background: #f2f3f5;
}
.el-row {
  margin-bottom: 20px;
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}

.bg-purple {
  background: #ffffff;
}

.bg-pur {
  background: #545454;
  color: white;
  text-align: center;
}

.bg-purple-light {
  background: #e5e9f2;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
</style>