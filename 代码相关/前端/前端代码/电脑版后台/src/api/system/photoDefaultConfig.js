import request from '@/utils/request'

// 查询照片默认配置列表
export function listPhotoDefaultConfig(query) {
  return request({
    url: '/system/photoConfig/list',
    method: 'get',
    params: query
  })
}

// 查询照片默认配置详细
export function getPhotoDefaultConfig(configId) {
  return request({
    url: '/system/photoConfig/' + configId,
    method: 'get'
  })
}

// 根据配置名称查询配置
export function getPhotoDefaultConfigByName(configName) {
  return request({
    url: '/system/photoConfig/name/' + configName,
    method: 'get'
  })
}

// 新增照片默认配置
export function addPhotoDefaultConfig(data) {
  return request({
    url: '/system/photoConfig',
    method: 'post',
    data: data
  })
}

// 修改照片默认配置
export function updatePhotoDefaultConfig(data) {
  return request({
    url: '/system/photoConfig',
    method: 'put',
    data: data
  })
}

// 删除照片默认配置
export function delPhotoDefaultConfig(configId) {
  return request({
    url: '/system/photoConfig/' + configId,
    method: 'delete'
  })
}

// 批量删除照片默认配置
export function delPhotoDefaultConfigs(configIds) {
  return request({
    url: '/system/photoConfig/' + configIds,
    method: 'delete'
  })
}

// 更新配置状态
export function updateConfigStatus(configId, isActive) {
  return request({
    url: '/system/photoConfig/status',
    method: 'put',
    params: {
      configId: configId,
      isActive: isActive
    }
  })
}

// 查询启用的默认模板配置列表
export function getDefaultTemplates() {
  return request({
    url: '/system/photoConfig/defaultTemplates',
    method: 'get'
  })
}

// 根据配置类型查询配置列表
export function getConfigsByType(configType, isActive) {
  return request({
    url: '/system/photoConfig/type/' + configType,
    method: 'get',
    params: {
      isActive: isActive
    }
  })
}

// 复制配置
export function copyConfig(sourceConfigId, newConfigName) {
  return request({
    url: '/system/photoConfig/copy/' + sourceConfigId,
    method: 'post',
    params: {
      newConfigName: newConfigName
    }
  })
}

// 校验配置名称是否唯一
export function checkConfigNameUnique(configName, configId) {
  return request({
    url: '/system/photoConfig/checkConfigNameUnique',
    method: 'get',
    params: {
      configName: configName,
      configId: configId
    }
  })
}

// 获取配置类型选项
export function getConfigTypeOptions() {
  return request({
    url: '/system/photoConfig/configTypeOptions',
    method: 'get'
  })
}

// 获取启用状态选项
export function getIsActiveOptions() {
  return request({
    url: '/system/photoConfig/isActiveOptions',
    method: 'get'
  })
}

// 导出照片默认配置
export function exportPhotoDefaultConfig(query) {
  return request({
    url: '/system/photoConfig/export',
    method: 'post',
    data: query
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

// 删除示例图片
export function deleteExampleImage(fileName) {
  return request({
    url: '/system/photoConfig/deleteExample',
    method: 'delete',
    params: {
      fileName: fileName
    }
  })
}