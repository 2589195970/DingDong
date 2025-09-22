package com.ruoyi.common.order.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 地址信息
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 16:39
 */
@Data
public class AddressCode {

    /**
     * 省编码
     */
    @ApiModelProperty(value = "省编码")
    private String provinceCode;

    /**
     * 省名称
     */
    @ApiModelProperty(value = "省名称")
    private String provinceName;

    /**
     * 市编码
     */
    @ApiModelProperty(value = "市编码")
    private String cityCode;

    /**
     * 市名称
     */
    @ApiModelProperty(value = "市名称")
    private String cityName;

    /**
     * 县/区编码
     */
    @ApiModelProperty(value = "县/区编码")
    private String countyCode;

    /**
     * 县/区名称
     */
    @ApiModelProperty(value = "县/区名称")
    private String countyName;


}
