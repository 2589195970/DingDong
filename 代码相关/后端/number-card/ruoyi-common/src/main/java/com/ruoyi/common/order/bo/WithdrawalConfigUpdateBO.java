package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 提现配置表修改
 */
@Data
public class WithdrawalConfigUpdateBO {

    /**
     * 提现配置ID
     */
    @ApiModelProperty("提现配置ID")
    private Integer withdrawConfigId;

    /**
     * 最小提现金额
     */
    @ApiModelProperty(value = "最小提现金额")
    private Integer withdrawMinAmount;

    /**
     * 提现费率
     */
    @ApiModelProperty(value = "提现费率")
    private Integer withdrawRate;



}
