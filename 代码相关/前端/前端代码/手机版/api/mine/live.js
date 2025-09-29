import request from "@/utils/request";

// 获取直播配置
export function getLiveConfig() {
  return request({
    url: "/liveConnect/getLiveConfig",
    method: "get",
  });
}

// 保存直播配置
export function saveLiveConfig(data) {
  return request({
    url: "/liveConnect/saveLiveConfig",
    method: "post",
    data: data,
  });
}

// 测试直播连接
export function testLiveConnection(data) {
  return request({
    url: "/liveConnect/testConnection",
    method: "post",
    data: data,
  });
}

// 获取支持的直播平台列表
export function getLivePlatforms() {
  return request({
    url: "/liveConnect/getPlatforms",
    method: "get",
  });
}

// 获取平台配置模板
export function getPlatformConfigTemplate(platformId) {
  return request({
    url: `/liveConnect/getPlatformConfigTemplate/${platformId}`,
    method: "get",
  });
}

// 启用/禁用直播对接
export function toggleLiveConnect(enabled) {
  return request({
    url: `/liveConnect/toggleConnect/${enabled}`,
    method: "post",
  });
}

// 获取直播状态
export function getLiveStatus() {
  return request({
    url: "/liveConnect/getLiveStatus",
    method: "get",
  });
}

// 同步直播间信息
export function syncLiveRoomInfo(data) {
  return request({
    url: "/liveConnect/syncLiveRoomInfo",
    method: "post",
    data: data,
  });
}

// 推送商品到直播间
export function pushProductToLive(data) {
  return request({
    url: "/liveConnect/pushProductToLive",
    method: "post",
    data: data,
  });
}

// 获取直播统计数据
export function getLiveStats(data) {
  return request({
    url: "/liveConnect/getLiveStats",
    method: "post",
    data: data,
  });
}

// 获取直播订单列表
export function getLiveOrders(data) {
  return request({
    url: "/liveConnect/getLiveOrders",
    method: "post",
    data: data,
  });
}

// 设置直播间商品
export function setLiveProducts(data) {
  return request({
    url: "/liveConnect/setLiveProducts",
    method: "post",
    data: data,
  });
}

// 获取直播间观众统计
export function getLiveAudienceStats(roomId) {
  return request({
    url: `/liveConnect/getLiveAudienceStats/${roomId}`,
    method: "get",
  });
}

// 发送直播间消息
export function sendLiveMessage(data) {
  return request({
    url: "/liveConnect/sendLiveMessage",
    method: "post",
    data: data,
  });
}

// 获取直播回放地址
export function getLiveReplayUrl(roomId) {
  return request({
    url: `/liveConnect/getLiveReplayUrl/${roomId}`,
    method: "get",
  });
}