import Vue from 'vue'
import App from './App.vue'

// 导入路由模块，拿到路由的实例对象
import router from '@/router/index.js'
import Axios from 'axios'

Vue.config.productionTip = false
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN'
Vue.use(ElementUI,{size:'mdeium',locale})

// 配置请求的根路径
Axios.defaults.baseURL = '/api'
new Vue({
  render: h => h(App),
//在Vue项目中，要想把路由用起来，必须把路由实例对象，通过下面的方式进行挂载
//router:路由的实例对象
  router:router
}).$mount('#app')
new Vue({
  render: h => h(App),
}).$mount('#app')
