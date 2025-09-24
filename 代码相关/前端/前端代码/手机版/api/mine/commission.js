import request from "@/utils/request";

// 分页查询代理商佣金明细列表
export function selectAgentWithdrawalRecordListPage(data) {
  return request({
    url: "/agentWithdrawalRecord/selectAgentWithdrawalRecordListPage",
    method: "post",
    data: data,
  });
}

// 获取佣金统计数据
export function getCommissionStats(data) {
  return request({
    url: "/agentWithdrawalRecord/getCommissionStats",
    method: "post",
    data: data,
  });
}

// 获取佣金概览信息
export function getCommissionOverview() {
  return request({
    url: "/agentWithdrawalRecord/getCommissionOverview",
    method: "get",
  });
}

// 导出佣金明细
export function exportCommissionDetail(data) {
  return request({
    url: "/agentWithdrawalRecord/exportCommissionDetail",
    method: "post",
    data: data,
    responseType: 'blob'
  });
}

// 获取佣金明细详情
export function getCommissionDetail(id) {
  return request({
    url: `/agentWithdrawalRecord/getCommissionDetail/${id}`,
    method: "get",
  });
}