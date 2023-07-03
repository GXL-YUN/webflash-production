import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import {getToken, setToken} from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import i18n from '@/lang'
import {login} from "@/api/user";

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist


//路由拦截。
router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()
  // set page title,如果不使用国际化,to.meta.title
  // document.title = getPageTitle(i18n.t(to.meta.title))
  document.title = getPageTitle(i18n.t('route.'+to.name))

  // determine whether the user has logged in
  //获取验证值

  const hasToken = getToken()
  if (hasToken) {
  //判断是否可以登录
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {

      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      console.log("登录hasRoles"+hasRoles);
      if (hasRoles) {
        next()
      } else {
        try {
          // get user info
          const userInfo = await store.dispatch('user/getInfo')
          router.addRoutes(userInfo.routes)
          next({ ...to, replace: true })
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token 判断是否有登录 */
    console.log("to.path+"+to.path);
    if (whiteList.indexOf(to.path) !== -1) {
       //in the free login whitelist, go directly免登录 白名单
      next()
    } else {
      console.log("登录1");
       //other pages that do not have permission to access are redirected to the login page.
     next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
