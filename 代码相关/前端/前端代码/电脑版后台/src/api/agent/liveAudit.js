import request from "@/utils/request";

// 分页查询直播审核列表
export function selectLiveAuditListPage(data) {
  return request({
    url: "/agentLiveAudit/selectLiveAuditListPage",
    method: "post",
    data: data,
  });
}

// 新增直播审核记录
export function addLiveAudit(data) {
  return request({
    url: "/agentLiveAudit/addLiveAudit",
    method: "post",
    data: data,
  });
}

// 查询登录用户直播审核记录
export function selectLiveAudit() {
  return request({
    url: "/agentLiveAudit/selectLiveAudit",
    method: "post",
  });
}

// 更新直播审核记录
export function updateLiveAudit(data) {
  return request({
    url: "/agentLiveAudit/updateLiveAudit",
    method: "post",
    data: data,
  });
}

// 更新直播审核状态
export function updateLiveAuditStatus(data) {
  return request({
    url: "/agentLiveAudit/updateLiveAuditStatus",
    method: "post",
    data: data,
  });
}

// 删除直播审核记录
export function deleteLiveAudit(liveAuditId) {
  return request({
    url: "/agentLiveAudit/deleteLiveAudit?liveAuditId=" + liveAuditId,
    method: "get",
  });
}

// 获取直播配置说明
export function getLiveConfig() {
  return request({
    url: "/agentLiveAudit/getLiveConfig",
    method: "post",
  });
}

// 更新直播配置说明
export function updateLiveConfig(data) {
  return request({
    url: "/agentLiveAudit/updateLiveConfig",
    method: "post",
    data: data,
  });
}