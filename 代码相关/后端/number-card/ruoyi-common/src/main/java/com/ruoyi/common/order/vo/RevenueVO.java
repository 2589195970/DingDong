package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收益记录
 */
@Data
@ApiModel(value = "收益记录")
public class RevenueVO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;


    /**
     * 余额(可提现金额) 单位分
     */
    @ApiModelProperty("余额(可提现金额) 单位分")
    private Integer balance;

    /**
     * 已提现金额 单位分
     */
    @ApiModelProperty("已提现金额 单位分")
    private Integer depositAmount;

    /**
     * 总收益 单位分
     */
    @ApiModelProperty("总收益 单位分")
    private Integer withdrawalAmount;



}
