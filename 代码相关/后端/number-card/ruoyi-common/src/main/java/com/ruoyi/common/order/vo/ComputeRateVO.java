package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提现手续费计算返回
 */
@Data
@ApiModel(value = "提现手续费计算返回")
public class ComputeRateVO {

    /**
     * 提现金额 单位分
     */
    @ApiModelProperty(value = "提现金额 单位分")
    private Integer withdrawalAmount;

    /**
     * 实际金额 单位分
     */
    @ApiModelProperty(value = "实际金额 单位分")
    private Integer receivedAmount;

    /**
     * 提现费率
     */
    @ApiModelProperty(value = "提现费率")
    private Integer withdrawalRate;

    /**
     * 提现手续费 单位分
     */
    @ApiModelProperty(value = "提现手续费 单位分")
    private Integer withdrawalRateAmount;
}
