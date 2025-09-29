
import request from '@/utils/request'

// 订单
export function agentSelectOrderListPage(data) {
  return request({
    url: '/agentOrder/agentSelectOrderListPage',
    method: 'post',
    data: data
  })
}
// 查询用户个人信息
export function getAgentExtendUrlVO() {
  return request({
    url: '/agentExtendUrl/getAgentExtendUrlVO',
    method: 'get'
  })
}
export function selectRevenue() {
  return request({
    url: '/withdrawalRecord/selectRevenue',
    method: 'get'
  })
}

// 查询代理商统计面板数据
export function selectDashboardStatistics(type = 0) {
  return request({
    url: '/agentAppShow/selectDashboardStatistics',
    method: 'get',
    params: { type }
  })
}
