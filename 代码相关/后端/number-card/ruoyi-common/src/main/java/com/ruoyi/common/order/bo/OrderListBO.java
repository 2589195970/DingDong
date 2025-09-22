package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单列表查询参数
 */
@Data
public class OrderListBO {

    /**
     * 用户下单手机号
     */
    @ApiModelProperty("用户下单手机号")
    private String cardPhone;


    /**
     */
    @ApiModelProperty("用户身份证")
    private String cardId;


}
