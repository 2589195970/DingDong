package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 直属下级提卡费信息
 */
@Data
@ApiModel("直属下级提卡费信息")
public class AgentProductCardFeeChildVO {

    @ApiModelProperty("代理产品ID")
    private Integer agentProductId;

    @ApiModelProperty("下级代理编码")
    private String agentCode;

    @ApiModelProperty("下级代理名称")
    private String agentName;

    @ApiModelProperty("当前成本(元)")
    private Integer incomingCardFee;

    @ApiModelProperty("对下售价(元)")
    private Integer downstreamCardFee;

    @ApiModelProperty("提卡费差价(元)")
    private Integer cardFeeProfit;

    @ApiModelProperty("是否存在特例 0-否 1-是")
    private Integer hasOverride;
}
