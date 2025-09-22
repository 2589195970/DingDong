import request from "@/utils/request";
// 分页查询产品
export function agentSelectProductListPage(data) {
  return request({
    url: "/agentProduct/agentSelectProductListPage",
    method: "post",
    data: data,
  });
}
// 查询订单
export function agentSelectOrderListPage(data) {
  return request({
    url: "agentOrder/agentSelectOrderListPage",
    method: "post",
    data: data,
  });
}
//更新指定代理
export function updateAgentProduct(data) {
  return request({
    url: "/agentProduct/updateAgentProduct",
    method: "post",
    data: data,
  });
}

//查询佣金订单
export function selectOrderCommissionListPage(data) {
  return request({
    url: "/orderCommission/selectOrderCommissionListPage",
    method: "post",
    data: data,
  });
}
//查询佣金结算明细
export function selectOrderCommissionDetailsList(data) {
  return request({
    url: "/orderCommission/selectOrderCommissionDetailsList?orderCommissionId="+data,
    method: "get",
  });
}

//更新结算状态
export function updateOrderCommissionStatus(data) {
  return request({
    url: "/orderCommission/updateOrderCommissionStatus",
    method: "post",
    data: data,
  });
}
// 删除结算记录
export function deleteOrderCommission(data) {
  return request({
    url: "/orderCommission/deleteOrderCommission",
    method: "post",
    data: data,
  });
}



// 修改佣金
export function updateProductCommission(data) {
  return request({
    url: "/agentProduct/updateProductCommission",
    method: "post",
    data: data,
  });
}