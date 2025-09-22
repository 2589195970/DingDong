package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单查询
 */
@Data
public class OrderCommissionSelectBO extends BaseBO{

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private String orderId;

    /**
     * 下游订单号
     */
    @ApiModelProperty("下游订单号")
    private String orderDownstreamId;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String cardName;

    /**
     * 用户下单手机号
     */
    @ApiModelProperty("用户下单手机号")
    private String cardPhone;

    /**
     * 用户身份证
     */
    @ApiModelProperty("用户身份证")
    private String cardId;

    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private Integer orderStatus;

    /**
     * 是否充值 0 未充值 1已充值是否充值 0 未充值 1已充值
     */
    @ApiModelProperty("是否充值 0 未充值 1已充值")
    private Integer isRecharged;

    /**
     * 下游代理商code
     */
    @ApiModelProperty("下游代理商code")
    private String downstreamAgentCode;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Long starTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Long endTime;

    /**
     * 订单佣金状态 0 未到结算状态 1 待结算 2 部分结算 3已结算 4无法结算
     */
    @ApiModelProperty("订单佣金状态 0 未到结算状态 1 待结算 2 部分结算 3已结算 4无法结算")
    private Integer orderCommissionStatus;

}
