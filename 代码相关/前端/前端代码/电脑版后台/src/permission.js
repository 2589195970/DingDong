import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isRelogin } from '@/utils/request'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/register', '/forgot-password']
const realNameEntry = '/finance/personal-info'
const realNameWhitelist = [realNameEntry]
let hasShownRealNameTip = false

const isRealNameRoute = (path = '') => realNameWhitelist.some(route => path.startsWith(route))

const resolveRealNameStatus = () => {
  const account = store.getters.agentAccount || {}
  if (account.realNameStatus !== undefined && account.realNameStatus !== null) {
    return Number(account.realNameStatus)
  }
  if (account.isRealName !== undefined) {
    return account.isRealName ? 1 : 0
  }
  if (account.yisRealName !== undefined) {
    return account.yisRealName ? 1 : 0
  }
  return null
}

const needsRealNameVerification = () => {
  const status = resolveRealNameStatus()
  if (status === null) {
    return false
  }
  return status === 0
}

const guardRealName = (to, from, next) => {
  if (!getToken()) {
    hasShownRealNameTip = false
    return false
  }
  if (!store.getters.roles || store.getters.roles.length === 0) {
    return false
  }
  if (needsRealNameVerification()) {
    if (isRealNameRoute(to.path)) {
      return false
    }
    if (!hasShownRealNameTip) {
      Message.warning('请先完成实名认证')
      hasShownRealNameTip = true
    }
    next({ path: realNameEntry, replace: true })
    NProgress.done()
    return true
  } else {
    hasShownRealNameTip = false
  }
  return false
}

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      if (store.getters.roles.length === 0) {
        isRelogin.show = true
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then(() => {
          isRelogin.show = false
          store.dispatch('GenerateRoutes').then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            router.addRoutes(accessRoutes) // 动态添加可访问路由表
            if (!guardRealName(to, from, next)) {
              next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
            }
          })
        }).catch(err => {
            store.dispatch('LogOut').then(() => {
              Message.error(err)
              next({ path: '/' })
            })
          })
      } else {
        if (!guardRealName(to, from, next)) {
          next()
        }
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${encodeURIComponent(to.fullPath)}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
