package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改下级提卡费请求体
 */
@Data
@ApiModel("代理商修改下级提卡费")
public class AgentProductUpdateCardFeeBO {

    /**
     * 代理产品ID
     */
    @ApiModelProperty("代理产品ID")
    private Integer agentProductId;

    /**
     * 对下级的提卡费(元)
     */
    @ApiModelProperty("对下级的提卡费(元)")
    private Integer downstreamCardFee;
}
