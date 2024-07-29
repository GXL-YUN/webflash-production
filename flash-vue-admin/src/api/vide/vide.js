import request from '@/utils/request'

export default {
  getList: function (params) {
    return request({
      url: '/vode/list.do',
      method: 'get',
      params:{

        limit: 1000
      }
    })
  },
  save: function (params) {
    return request({
      url: '/vode/save.do',
      method: 'post',
      data: params
    })
  },
  remove: function (id) {
    return request({
      url: '/vode',
      method: 'delete',
      params: {
        id: id
      }
    })
  },
  getUser: function() {
    return request({
      url: '/account/getUser',
      method: 'get'
    })
  },

  getUserIsLogin: function() {
    return request({
      url: '/account/isLogin',
      method: 'get'
    })
  },
  get: function (id) {
    return request({
      url: '/vode/getById.do',
      method: 'get',
      params: {
        id: id
      }
    })
  }
}
