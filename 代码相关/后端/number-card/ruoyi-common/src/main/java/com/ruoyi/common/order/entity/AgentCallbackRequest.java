package com.ruoyi.common.order.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商回调
 */
@Data
public class AgentCallbackRequest {

    /**
     * 代理商编码
     */
    private String partnerCode;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 合作方单号
     */
    private String orderNo;

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
     * 是否充值 0 未充值 1已充值
     */
    private Integer isRecharged;

    /**
     * 充值金额
     */
    private String rechargeAmount;

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
    private Long deliveryTime;

    /**
     * 激活时间
     */
    private Long activeTime;

    /**
     * 充值时间
     */
    private Long rechargeTime;


    /**
     * 签名
     */
    private String sign;
}
