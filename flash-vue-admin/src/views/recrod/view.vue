<template>
  <div class="app-container">
   <div class="block">
        <el-row>
          <el-col :span="24">
            <el-button type="success" size="mini" icon="el-icon-plus"  @click="dialogFormVisible = true" >{{ $t('button.add') }}</el-button>
            <!--<el-button type="primary" size="mini"  icon="el-icon-edit" @click.native="edit" v-permission="['/cms/articleEdit']">{{ $t('button.edit') }}</el-button>
            <el-button type="danger" size="mini"  icon="el-icon-delete" @click.native="remove" v-permission="['/article/remove']">{{ $t('button.delete') }}</el-button>
          -->
          </el-col>
        </el-row>
      </div>
      <el-dialog title="档案信息修改新增" :visible.sync="dialogFormVisible">
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="档案名称">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="档案标题">
            <el-input v-model="form.title"></el-input>
          </el-form-item>

          <el-form-item label="档案编号">
            <el-input v-model="form.docNumber"></el-input>
          </el-form-item>

          <el-form-item label="老师名称">
            <el-input v-model="form.teacher"></el-input>
          </el-form-item>

          <el-form-item label="作者">
            <el-input v-model="form.author"></el-input>
          </el-form-item>

          <el-form-item label="存放内容">
            <el-input type="textarea" v-model="form.content"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="onSubmit">确 定</el-button>
        </div>
      </el-dialog>

      <el-table :data="list" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row
          @current-change="handleCurrentChange">

          <el-table-column label="ID">
              <template slot-scope="scope">
                  {{scope.row.id}}
              </template>
          </el-table-column>
          <el-table-column label="档案名称">
              <template slot-scope="scope">
                  {{scope.row.name}}
              </template>
          </el-table-column>
          <el-table-column label="档案标题">
              <template slot-scope="scope">
                  {{scope.row.title}}
              </template>
          </el-table-column>

          <el-table-column label="档案编号">
              <template slot-scope="scope">
                  {{scope.row.docNumber}}
              </template>
          </el-table-column>

          <el-table-column label="所属老师">
              <template slot-scope="scope">
                  {{scope.row.teacher}}
              </template>
          </el-table-column>
          <el-table-column label="存放内容">
              <template slot-scope="scope">
                  {{scope.row.content}}
              </template>
          </el-table-column>
          <el-table-column label="操作">
              <template slot-scope="scope">
                  <el-button type="text" size="mini" icon="el-icon-edit" @click.native="editItem(scope.row)"
                      v-permission="['/channel/edit']">{{ $t('button.edit') }}</el-button>

                  <el-button type="text" size="mini" icon="el-icon-delete" @click.native="removeItem(scope.row.id)"
                      v-permission="['/channel/remove']">{{ $t('button.delete') }}</el-button>
              </template>
          </el-table-column>
      </el-table>
  </div>
</template>
<script src="./record.js"></script>
<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>
