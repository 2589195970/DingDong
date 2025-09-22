package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商更新启用状态
 */
@Data
public class AgentUpdateEnabledBO extends BaseBO{

    /**
     * 代理商账号ID
     */
    @ApiModelProperty("代理商账号ID")
    private Integer agentAccountId;

    /**
     *是否启用  0 启用 1禁用
     */
    @ApiModelProperty("0 启用 1禁用")
    private Integer isEnabled;



}
