package com.ruoyi.common.order.vo;

import com.ruoyi.common.order.bo.AddressCacheProvinceBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 省市区查询返回值
 */
@Data
@ApiModel(value = "省市区编码返回值")
public class AddressSelectVO {

    /**
     * 省市区json列表
     */
    @ApiModelProperty("省市区json列表")
    private List<AddressCacheProvinceBO> addressCacheProvinceBO;


}
