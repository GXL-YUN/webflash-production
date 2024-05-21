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
      url: '/vode/del.do',
      method: 'delete',
      params: {
        id: id
      }
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
