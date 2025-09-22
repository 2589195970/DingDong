import request from "@/utils/request";
import { Message,Loading} from 'element-ui'
import { blobValidate } from "@/utils/ruoyi";
let downloadLoadingInstance;
// 分页查询产品
export function selectProductListPage(data) {
  return request({
    url: "/product/selectProductListPage",
    method: "post",
    data: data,
  });
}
// 分页查询订单
export function selectOrderListPage(data) {
  return request({
    url: "/order/selectOrderListPage",
    method: "post",
    data: data,
  });
}
// 新增产品
export function addProduct(data) {
  return request({
    url: "/product/addProduct",
    method: "post",
    data: data,
  });
}
// 下架产品
export function updateProductStatus(data) {
  return request({
    url: "/product/updateProductStatus",
    method: "post",
    data: data,
  });
}
// 修改佣金
export function updateProductCommission(data) {
  return request({
    url: "/product/updateProductCommission",
    method: "post",
    data: data,
  });
}
// 产品排序
export function updateProductSort(data) {
  return request({
    url: "/product/updateProductSort",
    method: "post",
    data: data,
  });
}
// 复制产品
export function copyProduct(data) {
  return request({
    url: "/product/copyProduct",
    method: "post",
    data: data,
  });
}
// 删除产品
export function deleteProduct(data) {
  return request({
    url: "/product/deleteProduct?productId=" + data,
    method: "get",
  });
}
// 分页查询上游接口列表
export function selectUpstreamApiListPage(data) {
  return request({
    url: "/upstream/api/selectUpstreamApiListPage",
    method: "post",
    data: data,
  });
}

// 分页查询上游接口列表
export function selectUpstreamApiTypeList() {
  return request({
    url: "/upstream/api/selectUpstreamApiTypeList",
    method: "get",
  });
}
// 新增上游产品
export function addUpstreamProduct(data) {
  return request({
    url: "/upstream/product/addUpstreamProduct",
    method: "post",
    data: data,
  });
}
// 新增上游产品
export function addUpstreamApi(data) {
  return request({
    url: "/upstream/api/addUpstreamApi",
    method: "post",
    data: data,
  });
}
// 更新上游产品
export function updateUpstreamApi(data) {
  return request({
    url: "/upstream/api/updateUpstreamApi",
    method: "post",
    data: data,
  });
}

// 分页查询上游接口列表
export function deleteUpstreamProduct(data) {
  return request({
    url: "/upstream/product/deleteUpstreamProduct?upstreamProductId=" + data,
    method: "get",
  });
}
// 分页查询上游接口列表
export function deleteUpstreamApi(data) {
  return request({
    url: "/upstream/api/deleteUpstreamApi?upstreamApiId=" + data,
    method: "get",
  });
}

// 重提订单
export function againOrderSubmit(data) {
  return request({
    url: "/order/againOrderSubmit",
    method: "post",
    data: data,
  });
}
// 查询日志
export function selectOrderLogList(data) {
  return request({
    url: "/order/selectOrderLogList?orderId=" + data,
    method: "get",
  });
}
// 更新订单状态
export function updateOrderStatus(data) {
  return request({
    url: "/order/updateOrderStatus",
    method: "post",
    data: data,
  });
}
// 验证码
export function sendSms(data) {
  return request({
    url: "/sms/sendSms",
    method: "post",
    data: data,
  });
}
// 分页查询上游产品列表
export function selectUpstreamProductListPage(data) {
  return request({
    url: "/upstream/product/selectUpstreamProductListPage",
    method: "post",
    data: data,
  });
}
// 编辑产品
export function updateProduct(data) {
  return request({
    url: "/product/updateProduct",
    method: "post",
    data: data,
  });
}
// 查询实名列表
export function selectNameAuditListPage(data) {
  return request({
    url: "/agentNameAudit/selectNameAuditListPage",
    method: "post",
    data: data,
  });
}
// 查询用户列表
export function selectChildAgentList(data) {
  return request({
    url: "/agentAccount/selectChildAgentList",
    method: "post",
    data: data,
  });
}
// 查询用户列表
export function agentSelectOrderListPage(data) {
  return request({
    url: "/agentManagement/agentSelectOrderListPage",
    method: "post",
    data: data,
  });
}
// 删除代理商
export function deleteAgentAccount(data) {
  return request({
    url: "/agentManagement/deleteAgentAccount?agentAccountId=" + data,
    method: "get",
  });
}
// 调整余额
export function updateBalance(data) {
  return request({
    url: "/agentManagement/updateBalance",
    method: "post",
    data: data,
  });
}
// 代理商禁用
export function updateAgentStatus(data) {
  return request({
    url: "/agentManagement/updateAgentStatus",
    method: "post",
    data: data,
  });
}
// 代理商余额明细
export function selectWithdrawalRecordDetailsListPage(data) {
  return request({
    url: "/agentManagement/selectWithdrawalRecordDetailsListPage",
    method: "post",
    data: data,
  });
}


//新增实名审核记录
export function addNameAudit(data) {
  return request({
    url: "/agentNameAudit/addNameAudit",
    method: "post",
    data: data,
  });
}
//更新实名审核记录
export function updateNameAudit(data) {
  return request({
    url: "/agentNameAudit/updateNameAudit",
    method: "post",
    data: data,
  });
}
//登陆实名审核记录
export function selectNameAudit(data) {
  return request({
    url: "/agentNameAudit/selectNameAudit",
    method: "post",
    data: data,
  });
}
// 删除实名审核记录
export function nameAuditId(data) {
  return request({
    url: "/agentNameAudit​/nameAuditId?nameAuditId=" + data,
    method: "get",
  });
}

//更新实名审核状态
export function updateNameAuditStatus(data) {
  return request({
    url: "/agentNameAudit/updateNameAuditStatus",
    method: "post",
    data: data,
  });
}



//更新工具配置
export function updateToolConfig(data) {
  return request({
    url: "/toolConfig/updateToolConfig",
    method: "post",
    data: data,
  });
}
//更新工具配置
export function updateRegisterQrcodeMap(data) {
  return request({
    url: "/toolConfig/updateRegisterQrcodeMap",
    method: "post",
    data: data,
  });
}
// 查询工具配置
export function getToolConfig(data) {
  return request({
    url: "/toolConfig/getToolConfig?toolConfigType="+data,
    method: "get",
  });
}



//新增产品校验配置
export function addProductCheckConfig(data) {
  return request({
    url: "/productCheckConfig/addProductCheckConfig",
    method: "post",
    data: data,
  });
}

//分页查询产品校验配置列表
export function selectProductCheckConfigListPage(data) {
  return request({
    url: "/productCheckConfig/selectProductCheckConfigListPage",
    method: "post",
    data: data,
  });
}
//修改产品校验配置
export function updateProductCheckConfig(data) {
  return request({
    url: "/productCheckConfig/updateProductCheckConfig",
    method: "post",
    data: data,
  });
}
// 删除产品校验配置
export function deleteProductCheckConfig(data) {
  return request({
    url: "/productCheckConfig/deleteProductCheckConfig?productCheckConfigId="+data,
    method: "get",
  });
}
//省市区
export function selectAddressList(data) {
  return request({
    url: "/address/selectAddressList",
    method: "post",
    data: data,
  });
}

//更新上游产品
export function updateUpstreamProduct(data) {
  return request({
    url: "/upstream/product/updateUpstreamProduct",
    method: "post",
    data: data,
  });
}
//导入订单
export function uploadOrderListExcel(data) {
  return request({
    url: "/order/uploadOrderListExcel",
    method: "post",
    data: data,
  });
}
//订单导出
export function exportOrderList(data) {
  return request({
    url: "/order/exportOrderList",
    method: "post",
    data: data,
  });
}
//加密修改
export function updateAgentEncryptStatus(data) {
  return request({
    url: "/agentManagement/updateAgentEncryptStatus",
    method: "post",
    data: data,
  });
}
// 重置密码
export function resetPwdDefault(data) {
  return request({
    url: "/system/user/resetPwdDefault?userName="+data,
    method: "get",
  });
}
// api说明
export function selectUpstreamExplain(data,data1) {
  return request({
    url: "/upstream/api/selectUpstreamExplain?upstreamApiType="+ data +"&explainType="+ data1,
    method: "get",
  });
}

//免密登录
export function loginFreePassword(data) {
  return request({
    url: "/loginFreePassword",
    method: "post",
    data: data,
  });
}

export function selectOrderBalance(query) {
  return request({
    url: '/order/selectOrderBalance',
    method: 'get',
    params: query
  })
}

// 渠道结算下载模板
export function exportSettlement(data,filename,urla) {
  downloadLoadingInstance = Loading.service({ text: "正在下载数据，请稍候", spinner: "el-icon-loading", background: "rgba(0, 0, 0, 0.7)", })
  return request({
    url: urla,
    // headers: { 'Content-Type': 'application/json' },
    responseType: 'blob',
    method: 'post',
    data: data
  }).then(async (data) => {
    const isBlob = blobValidate(data);
    if (isBlob) {
      const blob = new Blob([data])
      saveAs(blob,filename)
    } else {
      const resText = await data.text();
      const rspObj = JSON.parse(resText);
      // const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
      Message.error(rspObj.msg);
    }
    downloadLoadingInstance.close();
  }).catch((r) => {
    console.error(r)
    Message.error('下载文件出现错误，请联系管理员！')
    downloadLoadingInstance.close();
  })
}