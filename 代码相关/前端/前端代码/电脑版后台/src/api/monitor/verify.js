import request from "@/utils/request";
import { Message,Loading} from 'element-ui'
import { blobValidate } from "@/utils/ruoyi";
let downloadLoadingInstance;
// 分页查询产品
export function selectNumberStatusLogListPage(data) {
  return request({
    url: "/numberStatus/selectNumberStatusLogListPage",
    method: "post",
    data: data,
  });
}
export function getPhoneByType(query) {
    return request({
      url: '/numberStatus/getPhoneByType',
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