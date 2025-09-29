import request from "@/utils/request";

// 获取邀请信息
export function getInviteInfo() {
  return request({
    url: "/invite/getInviteInfo",
    method: "get",
  });
}

// 生成邀请码
export function generateInviteCode() {
  return request({
    url: "/invite/generateInviteCode",
    method: "post",
  });
}

// 刷新邀请码
export function refreshInviteCode() {
  return request({
    url: "/invite/refreshInviteCode",
    method: "post",
  });
}

// 获取邀请统计数据
export function getInviteStats() {
  return request({
    url: "/invite/getInviteStats",
    method: "get",
  });
}

// 获取邀请记录列表
export function getInviteRecords(data) {
  return request({
    url: "/invite/getInviteRecords",
    method: "post",
    data: data,
  });
}

// 获取推广规则
export function getInviteRules() {
  return request({
    url: "/invite/getInviteRules",
    method: "get",
  });
}

// 生成推广海报
export function generateInvitePoster(data) {
  return request({
    url: "/invite/generateInvitePoster",
    method: "post",
    data: data,
  });
}

// 获取海报模板列表
export function getPosterTemplates() {
  return request({
    url: "/invite/getPosterTemplates",
    method: "get",
  });
}

// 记录分享行为
export function recordShareAction(data) {
  return request({
    url: "/invite/recordShareAction",
    method: "post",
    data: data,
  });
}

// 获取邀请二维码
export function getInviteQRCode(data) {
  return request({
    url: "/invite/getInviteQRCode",
    method: "post",
    data: data,
    responseType: 'blob'
  });
}

// 验证邀请码
export function validateInviteCode(code) {
  return request({
    url: `/invite/validateInviteCode/${code}`,
    method: "get",
  });
}

// 获取邀请奖励记录
export function getInviteRewards(data) {
  return request({
    url: "/invite/getInviteRewards",
    method: "post",
    data: data,
  });
}

// 获取推广数据趋势
export function getInviteTrend(data) {
  return request({
    url: "/invite/getInviteTrend",
    method: "post",
    data: data,
  });
}

// 设置邀请配置
export function setInviteConfig(data) {
  return request({
    url: "/invite/setInviteConfig",
    method: "post",
    data: data,
  });
}