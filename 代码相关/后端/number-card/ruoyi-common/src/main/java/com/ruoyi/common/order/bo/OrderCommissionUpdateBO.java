package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

/**
 * 订单查询
 */
@Data
public class OrderCommissionUpdateBO extends BaseBO {

    /**
     * 订单佣金ID
     */
    @NotNull(message = "订单佣金ID")
    @ApiModelProperty("订单佣金ID")
    private String orderCommissionId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 订单佣金状态 0 未到结算状态 1 待结算 2 部分结算 3已结算 4无法结算
     */
    @ApiModelProperty("订单佣金状态 0 未到结算状态 1 待结算 2 部分结算 3已结算 4无法结算")
    private Integer orderCommissionStatus;

    /**
     * 订单佣金说明
     */
    @ApiModelProperty("订单佣金说明")
    private String orderCommissionMessage;

}
