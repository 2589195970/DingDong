import request from "@/utils/request";
// 分页查询提现申请列表
export function selectWithdrawalApplicationListPage(data) {
  return request({
    url: "/withdrawalApplication/selectWithdrawalApplicationListPage",
    method: "post",
    data: data,
  });
}
export function updateWithdrawalApplication(data) {
  return request({
    url: "/withdrawalApplication/updateWithdrawalApplication",
    method: "post",
    data: data,
  });
}

export function deletelithdrawalApplication() {
  return request({
    url: "/withdrawalApplication/deletelithdrawalApplication",
    method: "get",
  });
}
// 代理商新增申请提现
export function addAgentWithdrawalApplication(data) {
  return request({
    url: "/agentWithdrawalApplication/addAgentWithdrawalApplication ",
    method: "post",
    data: data
  })
}
// 代理商查询申请提现
export function selectAgentWithdrawalApplicationListPage(data) {
  return request({
    url: "/agentWithdrawalApplication/selectAgentWithdrawalApplicationListPage",
    method: "post",
    data: data,
  })
}
// 查询代理商
export function agentSelectOrderListPage(data) {
  return request({
    url: "/agentManagement/agentSelectOrderListPage",
    method: "post",
    data: data,
  })
}
// 代理商余额明细
export function selectAgentWithdrawalRecordListPage(data) {
  return request({
    url: "/agentWithdrawalRecord/selectAgentWithdrawalRecordListPage",
    method: "post",
    data: data,
  })
}
// 提现设置
export function updateWithdrawalConfig(data) {
  return request({
    url: "/withdrawal/config/updateWithdrawalConfig",
    method: "post",
    data: data,
  })
}
// 查询提现设置
export function selectWithdrawalConfig(data) {
  return request({
    url: "/withdrawal/config/selectWithdrawalConfig",
    method: "post",
    data: data,
  })
}
// 查询提现记录
export function selectWithdrawalRecordDetailsListPage(data) {
  return request({
    url: "/withdrawalRecord/selectWithdrawalRecordDetailsListPage",
    method: "post",
    data: data,
  })
}
// 查询提现记录
export function selectAgentCommissionConfig(data) {
  return request({
    url: "/agentCommission/selectAgentCommissionConfig",
    method: "post",
    data: data,
  })
}
// 查询提现记录
export function agentUpdateCommissionConfig(data) {
  return request({
    url: "/agentCommission/agentUpdateCommissionConfig",
    method: "post",
    data: data,
  })
}



// 代理商佣金查询
export function selectOrderCommissionListPage(data) {
  return request({
    url: "/agentCommission/selectOrderCommissionListPage",
    method: "post",
    data: data,
  })
}
// 计算汇率
export function computeRate(data) {
  return request({
    url: "/withdrawalApplication/computeRate?withdrawalAmount=" + data,
    method: "get",
  })
}
// 查询余额
export function selectWithdrawalRecord() {
  return request({
    url: "/withdrawalRecord/selectWithdrawalRecord",
    method: "get",
  })
}

// 获取API
export function getAgentApiVO() {
  return request({
    url: "/agentExtendUrl/getAgentApiVO",
    method: "get",
  })
}
// 获取推广链接信息
export function getAgentExtendUrlVO() {
  return request({
    url: "/agentExtendUrl/getAgentExtendUrlVO",
    method: "get",
  })
}
// 获取账号信息
export function getAgentInfoVO() {
  return request({
    url: "/agentExtendUrl/getAgentInfoVO",
    method: "get",
  })
}
// 更新回调url信息
export function updateCallbackUrl(data,chah) {
  return request({
    url: "/agentExtendUrl/updateCallbackUrl?agentCode="+ data +"&callbackUrl="+chah,
    method: "get",
  })
}
// 更新用户手机号
export function updateAgentPhone(data,chah) {
  return request({
    url: "/agentExtendUrl/updateAgentPhone?smsCode="+ data +"&phone="+chah,
    method: "get",
  })
}