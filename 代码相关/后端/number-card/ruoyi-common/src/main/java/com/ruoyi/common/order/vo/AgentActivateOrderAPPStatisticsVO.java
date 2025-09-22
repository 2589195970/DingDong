package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商app激活订单
 */
@Data
@ApiModel(value = "代理商app激活订单")
public class AgentActivateOrderAPPStatisticsVO {

    /**
     * 激活订单数
     */
    @ApiModelProperty("激活订单数")
    private Integer activatedOrderNumber;


    /**
     * 结算订单数
     */
    @ApiModelProperty("结算订单数")
    private Integer settledOrderNumber;


    /**
     * 订单总数
     */
    @ApiModelProperty("订单总数")
    private Integer totalOrderNumber;

}
