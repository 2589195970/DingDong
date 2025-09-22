package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 产品状态更新
 */
@Data
public class ProductUpdateStatusBO {

    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private Integer productId;

    /**
     * 产品展示排序
     */
    @ApiModelProperty("产品展示排序")
    private Integer productSort;

    /**
     * 产品状态 0 下架 1上架
     */
    @ApiModelProperty("产品状态 0 下架 1上架")
    private Integer productStatus;

    /**
     * 产品佣金
     */
    @ApiModelProperty("产品佣金")
    private Integer productCommission;

}
