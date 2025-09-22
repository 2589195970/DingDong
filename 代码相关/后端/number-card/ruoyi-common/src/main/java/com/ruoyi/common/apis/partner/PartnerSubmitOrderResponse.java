package com.ruoyi.common.apis.partner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 陈思伟
 */
@Data
@ApiModel("合作方提交订单返回")
public class PartnerSubmitOrderResponse {

    /**
     * 平台订单号
     */
    @ApiModelProperty(value = "平台订单号")
    private Long orderId;

    /**
     * 额外信息
     */
    @ApiModelProperty(value = "额外信息")
    private String extend1;
}
