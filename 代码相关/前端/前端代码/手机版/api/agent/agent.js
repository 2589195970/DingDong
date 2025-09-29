import request from '@/utils/request'

// 查询代理商注册统计数据（按时间维度）
export function selectRegistrationStatistics(params) {
  return request({
    url: '/agentAccount/selectRegistrationStatistics',
    method: 'get',
    params: params
  })
}