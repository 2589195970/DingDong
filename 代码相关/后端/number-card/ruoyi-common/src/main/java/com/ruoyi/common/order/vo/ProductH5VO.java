package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 产品 H5 页面信息
 */
@Data
public class ProductH5VO {

    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private Integer productId;


    /**
     * 产品CODE 自动生成的编码
     */
    @ApiModelProperty("产品CODE")
    private String productCode;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 产品主图URL
     */
    @ApiModelProperty("产品主图URL")
    private String productMasterMap;

    /**
     * 产品详情图url
     */
    @ApiModelProperty("产品详情图url")
    private String productDetailMap;

    /**
     * 产品海报图url
     */
    @ApiModelProperty("产品海报图url")
    private String productPlacardMap;

    /**
     * 产品模板其他参数 json格式
     */
    @ApiModelProperty("产品模板其他参数json格式")
    private String productTemplateJson;


}
