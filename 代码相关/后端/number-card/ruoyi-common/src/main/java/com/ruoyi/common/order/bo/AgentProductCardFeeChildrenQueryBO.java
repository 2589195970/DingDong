package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询直属下级提卡费请求
 */
@Data
@ApiModel("查询直属下级提卡费请求")
public class AgentProductCardFeeChildrenQueryBO {

    /**
     * 父级代理产品ID
     */
    @ApiModelProperty(value = "父级代理产品ID", required = true)
    private Integer agentProductId;
}
