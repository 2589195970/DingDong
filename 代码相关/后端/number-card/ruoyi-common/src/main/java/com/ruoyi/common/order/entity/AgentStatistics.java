package com.ruoyi.common.order.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 代理商统计
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 16:39
 */
@Data
public class AgentStatistics {

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码")
    private String agentCode;

    /**
     * 代理商名称
     */
    @ApiModelProperty(value = "代理商名称")
    private String agentName;


    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    /**
     * 二级代理数量
     */
    @ApiModelProperty("二级代理数量")
    Integer secondaryAgentNumber = 0;

    /**
     * 二级代理统计信息列表
     */
    @ApiModelProperty("二级代理统计信息列表")
    List<AgentStatistics> secondaryAgentStatisticsList;

}
