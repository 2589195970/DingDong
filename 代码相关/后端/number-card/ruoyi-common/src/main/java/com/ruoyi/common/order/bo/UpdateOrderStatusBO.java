package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单状态变更
 */
@Data
public class UpdateOrderStatusBO extends BaseBO{

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private String orderId;

    /**
     * 快递公司
     */
    @ApiModelProperty("快递公司")
    private String express;


    /**
     * 下快递单号
     */
    @ApiModelProperty("下快递单号")
    private String trackingNumber;


    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private Integer orderStatus;

    /**
     * 订单说明信息
     */
    @ApiModelProperty("订单说明信息")
    private String orderMessage;


    @ApiModelProperty("否充值 0 未充值 1已充值是否充值 0 未充值 1已充值")
    private String isRecharged;

    /**
     * 充值信息
     */
    @ApiModelProperty("充值信息")
    private String rechargeAmount;

}
