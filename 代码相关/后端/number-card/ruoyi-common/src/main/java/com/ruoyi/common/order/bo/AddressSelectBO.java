package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 地市省份查询
 */
@Data
public class AddressSelectBO extends BaseBO{

    /**
     * 省编码
     */
    @ApiModelProperty("省编码")
    private String provinceCode;


}
