import request from "@/utils/request";

// 查询下级代理列表
export function selectChildAgentList(data) {
  return request({
    url: "/agentAppShow/getMyDirectTeamList",
    method: "post",
    data: data,
  });
}

// 获取代理统计数据
export function getAgentStats() {
  return request({
    url: "/agentManagement/getAgentStats",
    method: "get",
  });
}

// 获取代理详情
export function getAgentDetail(agentId) {
  return request({
    url: `/agentManagement/getAgentDetail/${agentId}`,
    method: "get",
  });
}

// 更新代理状态
export function updateAgentStatus(data) {
  return request({
    url: "/agentManagement/updateAgentStatus",
    method: "post",
    data: data,
  });
}

// 调整代理余额
export function adjustAgentBalance(data) {
  return request({
    url: "/agentManagement/adjustAgentBalance",
    method: "post",
    data: data,
  });
}

// 获取代理余额记录
export function getAgentBalanceRecords(agentId, data) {
  return request({
    url: `/agentManagement/getAgentBalanceRecords/${agentId}`,
    method: "post",
    data: data,
  });
}

// 重置代理密码
export function resetAgentPassword(agentId) {
  return request({
    url: `/agentManagement/resetAgentPassword/${agentId}`,
    method: "post",
  });
}

// 设置代理订单加密状态
export function setAgentEncryptStatus(data) {
  return request({
    url: "/agentManagement/setAgentEncryptStatus",
    method: "post",
    data: data,
  });
}

// 获取代理层级关系
export function getAgentHierarchy(agentId) {
  return request({
    url: `/agentManagement/getAgentHierarchy/${agentId}`,
    method: "get",
  });
}

// 批量操作代理
export function batchOperateAgents(data) {
  return request({
    url: "/agentManagement/batchOperateAgents",
    method: "post",
    data: data,
  });
}

// 导出代理列表
export function exportAgentList(data) {
  return request({
    url: "/agentManagement/exportAgentList",
    method: "post",
    data: data,
    responseType: 'blob'
  });
}