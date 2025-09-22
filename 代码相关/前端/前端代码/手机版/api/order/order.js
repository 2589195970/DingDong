
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
