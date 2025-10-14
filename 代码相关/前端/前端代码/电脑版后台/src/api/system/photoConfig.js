import request from '@/utils/request'

// 查询照片配置列表
export function listPhotoConfig(query) {
  return request({
    url: '/system/photoConfig/list',
    method: 'get',
    params: query
  })
}

// 查询照片配置详细
export function getPhotoConfig(configId) {
  return request({
    url: '/system/photoConfig/' + configId,
    method: 'get'
  })
}

// 获取默认照片配置
export function getDefaultPhotoConfig() {
  return request({
    url: '/system/photoConfig/default',
    method: 'get'
  })
}

// 新增照片配置
export function addPhotoConfig(data) {
  return request({
    url: '/system/photoConfig',
    method: 'post',
    data: data
  })
}

// 修改照片配置
export function updatePhotoConfig(data) {
  return request({
    url: '/system/photoConfig',
    method: 'put',
    data: data
  })
}

// 删除照片配置
export function delPhotoConfig(configId) {
  return request({
    url: '/system/photoConfig/' + configId,
    method: 'delete'
  })
}

// 应用照片配置到商品
export function applyPhotoConfigToProduct(productId, configId) {
  return request({
    url: '/system/photoConfig/apply',
    method: 'post',
    data: {
      productId: productId,
      configId: configId
    }
  })
}

// 上传示例图片
export function uploadExampleImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/system/photoConfig/uploadExample',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取商品照片配置
export function getProductPhotoConfig(productId) {
  return request({
    url: '/system/photoConfig/product/' + productId,
    method: 'get'
  })
}

// 更新商品照片配置
export function updateProductPhotoConfig(data) {
  return request({
    url: '/system/photoConfig/product',
    method: 'put',
    data: data
  })
}