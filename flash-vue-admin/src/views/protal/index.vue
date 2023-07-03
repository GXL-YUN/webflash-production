<template>

  <div class="father">


    <!--前台页面-->
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
                  <files/>
                </div>
              </el-tab-pane>
              <el-tab-pane label="在线网址" name="first">
                <div>
                  <appName/>
                </div>
              </el-tab-pane>

              <el-tab-pane label="收藏网址" name="second">
                <div>
                  <bannerTag/>
                </div>
              </el-tab-pane>
              <el-tab-pane label="文件下载" name="third">
                <div>
                  <file/>
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

<script src="./protal.js"></script>
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
