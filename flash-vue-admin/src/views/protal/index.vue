<template >
  <div class="father bodys" >
    <!--前台页面-->


    <div class=" app-container center-flex">
        <!--导航栏-->
            <total/>
            <alent/>
<!--内容
-->
            <contents/>



      <!--内容区-->
    </div>
  </div>
</template>
<script src="./js/protal.js"></script>
<style>
.center-flex {
  justify-content: center; /* 水平居中 */

}

.bodys {
  color: #FFFFFF;
  height: auto;
  padding-left: 15%;
  padding-right: 15%;
}
.vie {
  margin: auto;
  margin-left: 5%;
  margin-right: -10%;
  margin-right: -10%;
}

.father {
  background: #000000;
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
