import request from "@/utils/request";

// 获取代理商日榜
export function getDailyRanking(params) {
  return request({
    url: "/console/agent/ranking/daily",
    method: "get",
    params: params
  });
}

// 获取代理商月榜
export function getMonthlyRanking(params) {
  return request({
    url: "/console/agent/ranking/monthly",
    method: "get",
    params: params
  });
}

// 自定义时间范围排行榜
export function getCustomRanking(data) {
  return request({
    url: "/console/agent/ranking/custom",
    method: "post",
    data: data
  });
}

// 获取排行榜类型说明
export function getRankingTypes() {
  return request({
    url: "/console/agent/ranking/types",
    method: "get"
  });
}

// 格式化统计数值显示
export function formatStatValue(item) {
  if (item.statDesc === '佣金(分)') {
    return (item.statValue / 100).toFixed(2) + '元';
  }
  return item.statValue + item.statDesc.replace(/[()]/g, '');
}

// 获取排行榜数据的通用错误处理
export async function handleRankingRequest(requestFn) {
  try {
    const response = await requestFn();

    if (response.code === 200) {
      return { success: true, data: response.data };
    } else {
      return { success: false, message: response.msg };
    }
  } catch (error) {
    console.error('排行榜请求失败:', error);

    if (error.response) {
      switch (error.response.status) {
        case 401:
          return { success: false, message: '未登录，请重新登录' };
        case 403:
          return { success: false, message: '权限不足，请联系管理员' };
        case 400:
          return { success: false, message: '参数错误：' + error.response.data.msg };
        default:
          return { success: false, message: '服务器错误，请稍后重试' };
      }
    }

    return { success: false, message: '网络错误，请检查网络连接' };
  }
}