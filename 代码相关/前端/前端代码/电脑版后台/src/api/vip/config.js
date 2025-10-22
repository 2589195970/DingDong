import request from '@/utils/request'

// 分页查询VIP配置
export function listVipConfig(query) {
  return request({
    url: '/vip/config/list',
    method: 'post',
    data: query
  })
}

// 查询VIP配置详情
export function getVipConfig(id) {
  return request({
    url: `/vip/config/${id}`,
    method: 'get'
  })
}

// 新增VIP配置
export function addVipConfig(data) {
  return request({
    url: '/vip/config',
    method: 'post',
    data: data
  })
}

// 修改VIP配置
export function updateVipConfig(id, data) {
  return request({
    url: `/vip/config/${id}`,
    method: 'put',
    data: data
  })
}

// 删除VIP配置
export function delVipConfig(ids) {
  return request({
    url: `/vip/config/${ids}`,
    method: 'delete'
  })
}

// 切换启用状态
export function toggleVipConfig(id, enable) {
  return request({
    url: `/vip/config/${id}/toggle`,
    method: 'put',
    params: { enable }
  })
}

// 导出VIP配置
export function exportVipConfig(ids) {
  return request({
    url: '/vip/config/export',
    method: 'post',
    data: ids || []
  })
}
