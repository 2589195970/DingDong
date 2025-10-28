package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提卡费特例返回对象
 */
@Data
public class AgentProductCardFeeOverrideVO {

    @ApiModelProperty("特例ID")
    private Long overrideId;

    @ApiModelProperty("商品编码")
    private String productCode;

    @ApiModelProperty("上级代理编码")
    private String parentAgentCode;

    @ApiModelProperty("下级代理编码")
    private String targetAgentCode;

    @ApiModelProperty("下级代理名称")
    private String targetAgentName;

    @ApiModelProperty("特例提卡费售价(元)")
    private Integer overrideFee;

    @ApiModelProperty("当前成本(元)")
    private Integer incomingCardFee;

    @ApiModelProperty("状态 1-生效 0-失效")
    private Integer status;

    @ApiModelProperty("生效时间(毫秒时间戳)")
    private Long effectiveTime;

    @ApiModelProperty("失效时间(毫秒时间戳, 可为空)")
    private Long expireTime;

    @ApiModelProperty("备注")
    private String memo;

    @ApiModelProperty("最后操作人")
    private String operator;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;
}
