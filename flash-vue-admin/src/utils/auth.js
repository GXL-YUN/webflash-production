import Cookies from 'js-cookie'

const TokenKey = 'vue_admin_template_token'

export function getToken() {
  return Cookies.get(TokenKey)
}
//获取某个cooking值
export function getKeyToken(key) {
  return Cookies.get(key)
}
//某个cooking值设置
export function setKeyToken(key, tokens) {
  return Cookies.set(key, tokens)
}
export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
