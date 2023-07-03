import request from '@/utils/request'


export  default {
  getList: function (params) {
    return request({
      url: '/fileMgr/list',
      method: 'get',
      params
    })
  },
  getPngList: function (params) {
    return request({
      url: '/fileMgr/listPng',
      method: 'get',
      params
    })
  },

}

