import request from '@/utils/request'

export default {
  getList: function (params) {
    return request({
      url: '/message/list',
      method: 'get',
      params
    })
  },
  clear: function () {
    return request({
      url: '/message',
      method: 'delete'
    })
  },
  send: function (emil) {
    return request({
      url: '/message/sendYj',
      method: 'post',
      params :{
        emil: emil
      }
    })
  },

  sendVode: function (emil,fdUrl) {
    return request({
      url: '/message/sendVoid',
      method: 'post',
      params :{
        emil: emil,

        fdUrl:fdUrl

      }
    })
  }
}
