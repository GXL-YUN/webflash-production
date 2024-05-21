<template>
  <div class="app-container">


    <el-table :data="list" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row
              @current-change="handleCurrentChange">
      <el-table-column label="ID">
        <template slot-scope="scope">
          {{scope.row.id}}
        </template>
      </el-table-column>
      <el-table-column label="网址说明">
        <template slot-scope="scope">
          {{scope.row.title}}
        </template>
      </el-table-column>
      <el-table-column label="类别">
        <template slot-scope="scope">
          {{scope.row.type}}
        </template>
      </el-table-column>
      <el-table-column label="url">
        <template slot-scope="scope">

          <el-link @click="get_jecoco_report(scope.row.url)">进入</el-link>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script src="./banner.js"></script>


<style rel="stylesheet/scss" lang="scss" scoped>
@import "src/styles/common.scss";
</style>
