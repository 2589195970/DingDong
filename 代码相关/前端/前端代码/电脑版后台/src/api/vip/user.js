import request from '@/utils/request'

// 分页查询VIP用户
export function listVipUser(query) {
  return request({
    url: '/vip/user/list',
    method: 'post',
    data: query
  })
}

// 查询VIP用户详情
export function getVipUser(id) {
  return request({
    url: `/vip/user/${id}`,
    method: 'get'
  })
}

// 新增VIP用户
export function addVipUser(data) {
  return request({
    url: '/vip/user',
    method: 'post',
    data: data
  })
}

// 修改VIP用户
export function updateVipUser(id, data) {
  return request({
    url: `/vip/user/${id}`,
    method: 'put',
    data: data
  })
}

// 删除VIP用户
export function delVipUser(ids) {
  return request({
    url: `/vip/user/${ids}`,
    method: 'delete'
  })
}

// 导出VIP用户
export function exportVipUser(ids) {
  return request({
    url: '/vip/user/export',
    method: 'post',
    data: ids || []
  })
}

// 设置VIP等级
export function setVipLevel(data) {
  return request({
    url: '/vip/user/setLevel',
    method: 'put',
    data: data
  })
}

// 补录缺失的VIP用户
export function supplementVipUser() {
  return request({
    url: '/vip/user/supplement',
    method: 'post'
  })
}
