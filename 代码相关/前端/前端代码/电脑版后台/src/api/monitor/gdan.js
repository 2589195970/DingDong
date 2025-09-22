import request from "@/utils/request";
// 新增工单记录
export function addWorkOrder(data) {
    return request({
        url: "/workOrder/addWorkOrder",
        method: "post",
        data: data,
    });
}
//新增工单回复
export function addWorkOrderRecover(data) {
    return request({
        url: "/workOrder/addWorkOrderRecover",
        method: "post",
        data: data,
    });
}
//工单列表查询
export function selectWorkOrderListPage(data) {
    return request({
        url: "/workOrder/selectWorkOrderListPage",
        method: "post",
        data: data,
    });
}
// 删除工单
export function deleteWorkOrderRecover(data) {
    return request({
        url: "/workOrder/deleteWorkOrderRecover?workOrderId=" + data,
        method: "get",
    });
}
