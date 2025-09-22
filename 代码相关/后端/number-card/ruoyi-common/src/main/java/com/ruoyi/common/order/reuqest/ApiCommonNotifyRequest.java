package com.ruoyi.common.order.reuqest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description 回调接口传入数据
 */
@Data
@Accessors(chain = true)
@ApiModel("通用回调信息")
public class ApiCommonNotifyRequest extends BaseNotifyRequest {
    /**
     * 平台订单号
     */
    @ApiModelProperty("平台订单号")
    private String orderId;

    /**
     * 运营商订单号
     */
    @ApiModelProperty("运营商订单号")
    private String carrierOrderId;

    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private String orderStatus;

    /**
     * 订单信息
     */
    @ApiModelProperty("订单信息")
    private String message;

    /**
     * 预占号码
     */
    @ApiModelProperty("预占号码")
    private String accNumber;

    /**
     * 运单号
     */
    @ApiModelProperty("运单号")
    private String trackingNumber;

    /**
     * 快递公司
     */
    @ApiModelProperty("快递公司")
    private String logisticsName;

    @ApiModelProperty("否充值 0 未充值 1已充值是否充值 0 未充值 1已充值")
    private String isRecharged;

    /**
     * 充值信息
     */
    @ApiModelProperty("充值信息")
    private String rechargeAmount;

    /**
     * 激活时间
     */
    @ApiModelProperty("激活时间")
    private Long activeTime;
}
