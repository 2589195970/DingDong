import request from '@/utils/request'

// 查询 VIP 佣金策略卡片
export function getVipCommissionCards() {
  return request({
    url: '/agentCommission/vipCards',
    method: 'get'
  })
}
