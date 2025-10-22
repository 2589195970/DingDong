import request from '@/utils/request'

// 获取代理商排名分页数据
// 返回数据结构：{ code, message, data: { pageNo, pageSize, totalPage, totalRows, rows } }
export function getAgentRankingPage(data) {
  return request({
    url: '/agentManagement/selectAgentRankingPage',
    method: 'post',
    data: data
  })
}

// 获取代理商详情
export function getAgentDetail(agentCode) {
  return request({
    url: `/agentManagement/detail/${agentCode}`,
    method: 'get'
  })
}

// 获取代理商统计信息
export function getAgentStatistics(agentCode) {
  return request({
    url: `/agentManagement/statistics/${agentCode}`,
    method: 'get'
  })
}
