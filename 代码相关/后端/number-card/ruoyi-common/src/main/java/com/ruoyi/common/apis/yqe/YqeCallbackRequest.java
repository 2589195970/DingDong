package com.ruoyi.common.apis.yqe;

import com.ruoyi.common.order.reuqest.BaseNotifyRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("172回调信息")
public class YqeCallbackRequest extends BaseNotifyRequest {

    /**
     * 推送请求Id,唯一，推送失败，会重复请求这条id的信息。
     */
    private String requidId;

    /**
     * 返回订单信息Json字符串格式
     */
    private String data;

}
