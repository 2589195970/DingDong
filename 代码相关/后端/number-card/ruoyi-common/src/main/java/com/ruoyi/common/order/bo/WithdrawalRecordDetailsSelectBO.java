package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提现记录详情查询
 */
@Data
public class WithdrawalRecordDetailsSelectBO extends BaseBO{

    /**
     * 提现记录表
     */
    @ApiModelProperty("提现记录表")
    private Integer withdrawalRecordId;

    /**
     * 登录账户ID
     */
    @ApiModelProperty(value = "登录账户ID")
    private Long sysUserId;

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码")
    private String agentCode;

    /**
     *  金额类型 '0 存入 1 提现'
     */
    @ApiModelProperty(value = "'0 存入 1 提现'")
    private Integer amountType;


    /**
     * 提现类型 0 佣金结算 1代理商提现 2 添加余额
     */
    @ApiModelProperty(value = "提现类型 0 佣金结算 1代理商提现 2 管理员操作")
    private Integer withdrawalType;

    /**
     * 来源单号
     */
    @ApiModelProperty("来源单号")
    private String sourceNumber;

}
