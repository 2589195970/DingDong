package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 代理商排名返回结果
 *
 * @Description
 * @Author 系统
 * @Date 2025/01/27
 */
@Data
@ApiModel(value = "代理商排名返回结果")
public class AgentRankingVO {

    /**
     * 代理商编码
     */
    @ApiModelProperty("代理商编码")
    private String agentCode;

    /**
     * 代理商姓名
     */
    @ApiModelProperty("代理商姓名")
    private String agentName;

    /**
     * 订单总数
     */
    @ApiModelProperty("订单总数")
    private Long totalOrders;

    /**
     * 激活订单数
     */
    @ApiModelProperty("激活订单数")
    private Long activatedOrders;

    /**
     * 佣金总额（单位：分）
     */
    @ApiModelProperty("佣金总额（单位：分）")
    private BigDecimal totalCommission;

    /**
     * 下游代理数
     */
    @ApiModelProperty("下游代理数")
    private Long downstreamAgentCount;

    /**
     * 排名
     */
    @ApiModelProperty("排名")
    private Integer ranking;
}
