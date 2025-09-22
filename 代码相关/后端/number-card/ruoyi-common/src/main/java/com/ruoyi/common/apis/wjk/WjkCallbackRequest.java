package com.ruoyi.common.apis.wjk;

import com.ruoyi.common.order.reuqest.BaseNotifyRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("挖金客回调信息")
public class WjkCallbackRequest extends BaseNotifyRequest {

    /**
     * 合作方ID
     */
    private Integer partnerId;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 状态
     */
    private String status;
    /**
     * 物流单号
     */
    private String trackingNum;
    /**
     * 物流信息
     */
    private String express;
    /**
     * 预占号码
     */
    private String number;
    /**
     * 充值金额
     */
    private String amount;
    /**
     * 消息
     */
    private String message;
    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 发货时间
     */
    private Long shipTime;

    /**
     * 激活时间
     */
    private Long activeTime;

    /**
     * 合作方单号
     */
    private String orderNo;

    /**
     * 签名
     */
    private String sign;
}
