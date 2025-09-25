import request from '@/utils/request'

// 获取订单统计数据
export function getOrderStatistics() {
  return request({
    url: '/order/getOrderStatistics',
    method: 'get'
  })
}

// 获取订单列表
export function getOrderList(query) {
  return request({
    url: '/order/list',
    method: 'get',
    params: query
  })
}

// 获取订单详情
export function getOrderDetail(orderId) {
  return request({
    url: `/order/${orderId}`,
    method: 'get'
  })
}

// 新增订单
export function addOrder(data) {
  return request({
    url: '/order',
    method: 'post',
    data: data
  })
}

// 修改订单
export function updateOrder(data) {
  return request({
    url: '/order',
    method: 'put',
    data: data
  })
}

// 删除订单
export function deleteOrder(orderId) {
  return request({
    url: `/order/${orderId}`,
    method: 'delete'
  })
}

// 获取订单状态列表
export function getOrderStatusList() {
  return request({
    url: '/order/status/list',
    method: 'get'
  })
}

// 更新订单状态
export function updateOrderStatus(orderId, status) {
  return request({
    url: `/order/${orderId}/status`,
    method: 'put',
    data: { status }
  })
}

// 批量更新订单状态
export function batchUpdateOrderStatus(orderIds, status) {
  return request({
    url: '/order/batch/status',
    method: 'put',
    data: { orderIds, status }
  })
}

// 获取订单趋势数据
export function getOrderTrendData(query) {
  return request({
    url: '/order/trend',
    method: 'get',
    params: query
  })
}

// 获取订单分析数据
export function getOrderAnalysisData(query) {
  return request({
    url: '/order/analysis',
    method: 'get',
    params: query
  })
}

// 导出订单数据
export function exportOrderData(query) {
  return request({
    url: '/order/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

// 获取订单统计报表
export function getOrderReport(query) {
  return request({
    url: '/order/report',
    method: 'get',
    params: query
  })
}

// 获取今日代理订单排名
export function getTodayAgentOrderRanking() {
  return request({
    url: '/order/getTodayAgentOrderRanking',
    method: 'get'
  })
}

// 获取订单趋势数据（30天）
export function getOrderTrend() {
  return request({
    url: '/order/getOrderTrend',
    method: 'get'
  })
}
