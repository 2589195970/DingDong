package com.ruoyi.common.order.vo;

import com.ruoyi.common.order.entity.AgentStatistics;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Data
@ApiModel(value = "子代理统计返回")
public class AgentStatisticsVO {

    /**
     * 二级代理数量(代理的代理总数)
     */
    @ApiModelProperty("代理商数量")
    private Integer agentAccountTotal = 0;

    /**
     * 二级代理数量(代理的代理总数)
     */
    @ApiModelProperty("二级代理数量(代理的代理总数)")
    private Integer secondaryAgentTotal = 0;

    /**
     * 代理商统计信息列表
     */
    @ApiModelProperty("代理商统计信息列表")
    List<AgentStatistics> agentStatisticsList = new ArrayList<>();
}
