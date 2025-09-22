package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 提现记录新增
 */
@Data
public class WithdrawalApplicationUpdateBO {

    /**
     * 提现记录详情表ID
     */
    @ApiModelProperty("提现申请表ID")
    private Integer withdrawalApplicationId;

    /**
     * 提现状态 0 申请中 1 打款中 2 提现成功 3 提现失败
     */
    @ApiModelProperty(value = "提现状态 0 申请中 1 打款中 2 提现成功 3 提现失败")
    private Integer withdrawalStatus;

    /**
     * 备注
     */
    @ApiModelProperty(value = "交易流水号")
    private String serialNumber;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
