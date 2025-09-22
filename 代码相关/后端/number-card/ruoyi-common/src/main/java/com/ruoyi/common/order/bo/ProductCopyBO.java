package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 产品复制
 */
@Data
public class ProductCopyBO {

    /**
     * 产品ID
     */
    @NotBlank(message = "产品ID不能为空")
    @ApiModelProperty("产品ID")
    private Integer productId;


}
