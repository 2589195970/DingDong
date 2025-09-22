
import request from '@/utils/request'

// 修改用户个人信息
export function agentSelectProductListPage(data) {
  return request({
    url: '/agentProduct/agentSelectProductListPage',
    method: 'post',
    data: data
  })
}
// 订单
export function agentSelectOrderListPage(data) {
  return request({
    url: '/agentOrder/agentSelectOrderListPage',
    method: 'post',
    data: data
  })
}

// 修改佣金
export function updateProductCommission(data) {
  return request({
    url: "/agentProduct/updateProductCommission",
    method: "post",
    data: data,
  });
}
// 查询提现记录
export function selectAgentCommissionConfig(data) {
  return request({
    url: "/agentCommission/selectAgentCommissionConfig",
    method: "post",
    data: data,
  })
}
// 查询提现记录
export function agentUpdateCommissionConfig(data) {
  return request({
    url: "/agentCommission/agentUpdateCommissionConfig",
    method: "post",
    data: data,
  })
}
