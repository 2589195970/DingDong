
import request from '@/utils/request'

// 订单
export function selectWithdrawalAPPStatistics() {
  return request({
    url: '/agentAppShow/selectWithdrawalAPPStatistics',
    method: 'get',
  })
}
// 查询用户个人信息
export function selectAgentOrderAPPStatistics(data) {
  return request({
    url: '/agentAppShow/selectAgentOrderAPPStatistics?type='+data,
    method: 'get'
  })
}
export function selectChildAgentStatistics(data) {
  return request({
    url: '/agentAccount/selectChildAgentStatistics',
    method: 'post',
	data: data
  })
}

// 查询用户个人信息
export function selectActivateAgentOrderAPPStatistics(data) {
  return request({
    url: '/agentAppShow/selectActivateAgentOrderAPPStatistics?type='+data,
    method: 'get'
  })
}
// 订单
export function selectChildAgentList(data) {
  return request({
    url: '/agentAccount/selectChildAgentList',
    method: 'post',
    data: data
  })
}
