import request from '@/utils/request'

// 分页查询VIP升级日志
export function listVipUpgradeLog(query) {
  return request({
    url: '/vip/upgradeLog/list',
    method: 'post',
    data: query
  })
}

// 导出VIP升级日志
export function exportVipUpgradeLog(ids) {
  return request({
    url: '/vip/upgradeLog/export',
    method: 'post',
    data: ids || []
  })
}
