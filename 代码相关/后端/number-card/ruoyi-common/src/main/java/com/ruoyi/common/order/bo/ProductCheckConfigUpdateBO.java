package com.ruoyi.common.order.bo;

import com.ruoyi.common.order.entity.AddressCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 产品校验配置
 */
@Data
public class ProductCheckConfigUpdateBO {

    /**
     * 产品校验配置表ID
     */
    @NotNull(message = "产品校验配置表ID不能为空")
    @ApiModelProperty("产品校验配置表ID")
    private String productCheckConfigId;

    /**
     * 地域 省列表 存放省信息
     */
    @ApiModelProperty("地域 省列表 存放省信息")
    private List<AddressCode> territoryProvinceList;

    /**
     * 地域 省列表 存放市信息
     */
    @ApiModelProperty("地域 省列表 存放市信息")
    private List<AddressCode> territoryCityList;

    /**
     * 最大年龄
     */
    @ApiModelProperty("最大年龄")
    private Integer ageMax;

    /**
     * 最小年龄
     */
    @ApiModelProperty("最小年龄")
    private Integer ageMin;


}
