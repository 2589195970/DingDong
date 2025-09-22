package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商app订单统计
 */
@Data
@ApiModel(value = "代理商app订单统计")
public class AgentOrderAPPStatisticsVO {

    /**
     * 昨日订单数
     */
    @ApiModelProperty("昨日订单数")
    private Integer yesterdayOrderNumber;


    /**
     * 今日订单数
     */
    @ApiModelProperty("今日订单数")
    private Integer todayOrderNumber;


    /**
     * 订单总数
     */
    @ApiModelProperty("订单总数")
    private Integer totalOrderNumber;

}
