import request from '@/utils/request'

export default {
  //查询数据路由
  getList: function (params) {
    return request({
      url: '/record/list',
      method: 'get',
      params
    })
  },
//删除路由
  remove: function (params) {
    return request({
      url: '/record',
      method: 'delete',
      params
    })
  },
  //更新路由
  upDate: function (params) {
    return request({
      url: '/record',
      method: 'post',
      data: params
    })
  },
}
