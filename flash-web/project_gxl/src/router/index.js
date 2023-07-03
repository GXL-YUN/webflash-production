//1. 导入 Vue 和 VueRouter 的包
import Vue from 'vue'
import VueRouter from 'vue-router'
import configEdit from "@/view/index/config";
import appName from "@/view/index/app";
import S from "@/view/index/index";
//import carouselname from "@/view/index/carouselView";
//import router from '@router/index.js'
//2. 调用Vue.use()函数，把VueRouter安装为Vue的插件
Vue.use(VueRouter)
//3. 创建路由的实例对象
const router = new VueRouter({
    //routes是一个数组，定义hash地址与组件之间的关系
    routes: [
        // 重定向路由规则
        // 当用户访问 / 的时候，通过redirect属性跳转到/home对应的路由规则
        {path: '/',redirect: 'home'},
        // 路由规则
        {path: '/home', component: configEdit },
        {path: '/movie', component: appName},
        {path: '/about', component: S}
    ]
})
//4. 向外共享路由的实例对象
export default router