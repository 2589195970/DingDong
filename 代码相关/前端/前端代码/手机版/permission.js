import { getToken } from '@/utils/auth'
import store from '@/store'
import constant from '@/utils/constant'

// 登录页面
const loginPage = "/pages/login"
  
// 页面白名单
const whiteList = [
  '/pages/login','/pages/login-sjihao','/pages/register', '/pages/common/webview/index'
]

const realNamePage = constant.REAL_NAME_PAGE || '/pages/mine/realname/index'
const realNameWhitelist = [realNamePage]
let hasShownRealNameTip = false

// 检查地址白名单
function checkWhite(url) {
  const path = url.split('?')[0]
  return whiteList.indexOf(path) !== -1
}

function isRealNameRoute(url = '') {
  const path = url.split('?')[0]
  return realNameWhitelist.indexOf(path) !== -1
}

function resolveRealNameStatus() {
  const account = store.getters.agentAccount || {}
  if (account.realNameStatus !== undefined && account.realNameStatus !== null) {
    return Number(account.realNameStatus)
  }
  if (account.isRealName !== undefined) {
    return account.isRealName ? constant.REAL_NAME_STATUS.VERIFIED : constant.REAL_NAME_STATUS.UNVERIFIED
  }
  return null
}

function needsRealNameVerification() {
  const status = resolveRealNameStatus()
  if (status === null) {
    return false
  }
  return status === constant.REAL_NAME_STATUS.UNVERIFIED
}

function guardRealName(url) {
  if (!getToken()) {
    hasShownRealNameTip = false
    return true
  }
  if (needsRealNameVerification()) {
    if (isRealNameRoute(url)) {
      return true
    }
    if (!hasShownRealNameTip) {
      uni.showToast({
        title: '请先完成实名认证',
        icon: 'none'
      })
      hasShownRealNameTip = true
    }
    setTimeout(() => {
      uni.reLaunch({ url: realNamePage })
    }, 0)
    return false
  } else {
    hasShownRealNameTip = false
  }
  return true
}

// 页面跳转验证拦截器
let list = ["navigateTo", "redirectTo", "reLaunch", "switchTab"]
list.forEach(item => {
  uni.addInterceptor(item, {
    invoke(to) {
      if (getToken()) {
        if (to.url === loginPage) {
          uni.reLaunch({ url: "/" })
          return false
        }
        if (!guardRealName(to.url)) {
          return false
        }
        return true
      } else {
        if (checkWhite(to.url)) {
          return true
        }
        uni.reLaunch({ url: loginPage })
        return false
      }
    },
    fail(err) {
      console.log(err)
    }
  })
})
