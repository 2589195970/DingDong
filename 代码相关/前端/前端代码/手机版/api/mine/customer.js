import request from "@/utils/request";

// 获取客服信息
export function getCustomerService() {
  return request({
    url: "/customerService/getCustomerServiceInfo",
    method: "get",
  });
}

// 提交客户反馈
export function submitCustomerFeedback(data) {
  return request({
    url: "/customerService/submitFeedback",
    method: "post",
    data: data,
  });
}

// 获取客服工作状态
export function getCustomerServiceStatus() {
  return request({
    url: "/customerService/getServiceStatus",
    method: "get",
  });
}

// 发起在线客服会话
export function startOnlineChat(data) {
  return request({
    url: "/customerService/startOnlineChat",
    method: "post",
    data: data,
  });
}

// 获取常见问题列表
export function getFAQList(data) {
  return request({
    url: "/customerService/getFAQList",
    method: "post",
    data: data,
  });
}

// 搜索常见问题
export function searchFAQ(keyword) {
  return request({
    url: `/customerService/searchFAQ?keyword=${keyword}`,
    method: "get",
  });
}

// 获取反馈类型列表
export function getFeedbackTypes() {
  return request({
    url: "/customerService/getFeedbackTypes",
    method: "get",
  });
}

// 获取我的反馈记录
export function getMyFeedbackList(data) {
  return request({
    url: "/customerService/getMyFeedbackList",
    method: "post",
    data: data,
  });
}