package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 撤销提卡费特例请求
 */
@Data
@ApiModel("撤销提卡费特例请求")
public class AgentProductCardFeeOverrideCancelBO {

    /**
     * 特例ID
     */
    @ApiModelProperty(value = "特例ID", required = true)
    private Long overrideId;
}
