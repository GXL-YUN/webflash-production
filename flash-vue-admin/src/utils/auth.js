import Cookies from 'js-cookie'

const TokenKey = 'vue_admin_template_token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}


//设置cook
export function setTokens(name,token) {
  return Cookies.set(name, token)
}
//清除cook
export function removeTokens(name) {
  return Cookies.remove(name)
}
//获取cook
export function getTokens(name) {
  return Cookies.get(name)
}

