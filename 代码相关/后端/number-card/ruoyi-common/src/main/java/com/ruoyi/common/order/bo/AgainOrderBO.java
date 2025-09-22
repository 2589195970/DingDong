package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 重推订单
 */
@Data
public class AgainOrderBO extends BaseBO{

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private String orderId;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码", required = true)
    private String productCode;

    /**
     * 下游代理商code
     */
    @ApiModelProperty("下游代理商code")
    private String downstreamCode;

    /**
     * 身份证
     */
    @ApiModelProperty("身份证号码")
    private String cardId;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String cardName;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String cardPhone;

}
