
<!--公共组件-->
<template>
  <div :class="classObj" class="app-wrapper">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
    <!--    导航栏  导航栏-->
    <sidebar class="sidebar-container" />
    <!--  view 展示页面数据  -->
    <div class="main-container">
      <div :class="{'fixed-header':fixedHeader}">
        <navbar />
        <tags-view v-if="needTagsView" />
      </div>
      <app-main />
    </div>
  </div>




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

<script>
import { Navbar, Sidebar, AppMain, TagsView } from './components'
import { configEdit, appName, seName, carousel } from '../protal/protal'
import ResizeMixin from './mixin/ResizeHandler'
export default {
  name: 'Layout',
  components: {
    Navbar,
    Sidebar,
    AppMain,
    TagsView
  },
  mixins: [ResizeMixin],
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    },
    device() {
      return this.$store.state.app.device
    },
    fixedHeader() {
      return this.$store.state.settings.fixedHeader
    },
    needTagsView() {
      return this.$store.state.settings.tagsView
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    }
  }
}
</script>
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
<style lang="scss" scoped>
  @import "~@/styles/mixin.scss";
  @import "~@/styles/variables.scss";

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;
    &.mobile.openSidebar{
      position: fixed;
      top: 0;
    }
  }
  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    width: calc(100% - #{$sideBarWidth});
    transition: width 0.28s;
  }

  .hideSidebar .fixed-header {
    width: calc(100% - 54px)
  }

  .mobile .fixed-header {
    width: 100%;
  }
</style>
