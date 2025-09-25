import request from '@/utils/request'

// 获取产品分类统计数量
export function getProductCategoryCount() {
  return request({
    url: '/product/getProductCategoryCount',
    method: 'get'
  })
}

// 获取产品列表
export function getProductList(query) {
  return request({
    url: '/product/list',
    method: 'get',
    params: query
  })
}

// 获取产品详情
export function getProductDetail(productId) {
  return request({
    url: `/product/${productId}`,
    method: 'get'
  })
}

// 新增产品
export function addProduct(data) {
  return request({
    url: '/product',
    method: 'post',
    data: data
  })
}

// 修改产品
export function updateProduct(data) {
  return request({
    url: '/product',
    method: 'put',
    data: data
  })
}

// 删除产品
export function deleteProduct(productId) {
  return request({
    url: `/product/${productId}`,
    method: 'delete'
  })
}

// 获取产品统计数据
export function getProductStatistics(query) {
  return request({
    url: '/product/statistics',
    method: 'get',
    params: query
  })
}

// 获取产品类型列表
export function getProductTypes() {
  return request({
    url: '/product/types',
    method: 'get'
  })
}

// 批量删除产品
export function batchDeleteProduct(productIds) {
  return request({
    url: '/product/batch',
    method: 'delete',
    data: productIds
  })
}

// 更新产品状态
export function updateProductStatus(productId, status) {
  return request({
    url: `/product/${productId}/status`,
    method: 'put',
    data: { status }
  })
}

// 获取产品销售统计
export function getProductSalesStats(query) {
  return request({
    url: '/product/sales/stats',
    method: 'get',
    params: query
  })
}

