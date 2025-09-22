package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商 调整余额
 */
@Data
public class AgentUpdateBalanceBO extends BaseBO{

    /**
     * 代理商账号ID
     */
    @ApiModelProperty("代理商账号ID")
    private Integer agentAccountId;

    /**
     * 更改余额 单位元
     */
    @ApiModelProperty("更改余额 单位元")
    private Integer balanceYun;

    /**
     * 类型 0 存入 1提取
     */
    @ApiModelProperty("类型 0 存入 1提取")
    private Integer type;



}
