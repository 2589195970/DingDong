package com.ruoyi.common.order.bo;

import com.ruoyi.common.order.entity.AddressCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 产品校验配置
 */
@Data
public class ProductCheckConfigAddBO {

    /**
     * 产品编码
     */
    @NotNull(message = "产品编码不能为空")
    @ApiModelProperty("产品编码")
    private String productCode;

    /**
     * 校验配置类型
     */
    @ApiModelProperty("0 年龄限制 1 地域限制")
    private Integer checkConfigType;

    /**
     * 地域限制类型 0省级 1市级
     */
    @ApiModelProperty("地域限制类型 0省级 1市级")
    private Integer territoryCheckType;

    /**
     * 策略 0 禁止 1允许
     */
    @ApiModelProperty("策略 0 禁止 1允许")
    private Integer tactics;

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
