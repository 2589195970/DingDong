package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 提现记录新增
 */
@Data
public class WithdrawalApplicationAddBO {


    /**
     * 申请用户账号ID
     */
    @NotNull(message = "申请用户不能为空")
    @ApiModelProperty(value = "申请用户账号ID")
    private Long applyUserId;


    /**
     * 提现金额 单位元
     */
    @NotNull(message = "提现金额不能为空")
    @ApiModelProperty(value = "提现金额 单位元")
    private Integer withdrawalAmount;


    /**
     * 提现类型 1 支付宝 2 银行卡
     */
    @NotNull(message = "提现类型不能为空")
    @ApiModelProperty(value = "提现类型 1 支付宝 2 银行卡")
    private Integer withdrawalType;

    /**
     * 支付宝账号
     */
    @ApiModelProperty(value = "支付宝账号")
    private String zfbAccount;

    /**
     * 支付宝账号真实姓名
     */
    @ApiModelProperty(value = "支付宝账号真实姓名")
    private String zfbAccountName;

    /**
     * 银行卡号
     */
    @ApiModelProperty(value = "银行卡号")
    private String bankNumber;

    /**
     * 银行开户行名称
     */
    @ApiModelProperty(value = "银行开户行名称")
    private String bankName;

    /**
     * 银行卡账号真实姓名
     */
    @ApiModelProperty(value = "银行卡账号真实姓名")
    private String bankNumberName;

    /**
     * 银行卡预留手机号
     */
    @ApiModelProperty(value = "银行卡预留手机号")
    private String bankNumberPhone;


}
