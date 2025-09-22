package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商提现统计
 */
@Data
@ApiModel(value = "代理商提现统计")
public class AgentWithdrawalAPPStatisticsVO {

    /**
     * 今日提现金额
     */
    @ApiModelProperty("今日提现金额 单位分")
    private Integer todayWithdrawalAmount;


    /**
     * 昨日提现金额
     */
    @ApiModelProperty("昨日提现金额 单位分")
    private Integer yesterdayWithdrawalAmount;


    /**
     * 订单总数
     */
    @ApiModelProperty("金额总数")
    private Integer totalWithdrawalAmount;


}
