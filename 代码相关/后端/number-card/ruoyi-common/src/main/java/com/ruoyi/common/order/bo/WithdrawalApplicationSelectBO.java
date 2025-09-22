package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单查询
 */
@Data
public class WithdrawalApplicationSelectBO extends BaseBO{

    /**
     * 提现单号(年月日+六位随机数)
     */
    @ApiModelProperty(value = "提现单号(年月日+六位随机数)")
    private String applicationNumber;

    /**
     * 申请用户账号ID
     */
    @ApiModelProperty(value = "申请用户账号ID")
    private Long applyUserId;

    /**
     * 申请代理商编码
     */
    @ApiModelProperty(value = "申请代理商编码")
    private String applyAgentCode;


    /**
     * 提现状态 0 申请中 1 打款中 2 提现成功 3 提现失败
     */
    @ApiModelProperty(value = "提现状态 0 申请中 1 打款中 2 提现成功 3 提现失败")
    private Integer withdrawalStatus;

    /**
     * 提现类型 0 微信 1 支付宝 2 已打款
     */
    @ApiModelProperty(value = "提现类型 0 微信 1 支付宝 2 已打款")
    private Integer withdrawalType;


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


}
