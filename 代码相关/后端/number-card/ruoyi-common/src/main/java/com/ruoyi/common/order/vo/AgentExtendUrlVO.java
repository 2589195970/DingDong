package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商推广链接URL
 */
@Data
@ApiModel(value = "代理商推广链接URL")
public class AgentExtendUrlVO {


    /**
     * 店铺url
     */
    @ApiModelProperty(value = "店铺url")
    private String shopUrl;

    /**
     * 店铺聚合页图Url
     */
    @ApiModelProperty(value = "店铺聚合页图Url")
    private String shopQrcodeMap;


    /**
     * 移动端url
     */
    @ApiModelProperty(value = "移动端url")
    private String mobileUrl;

    /**
     * 推广url
     */
    @ApiModelProperty(value = "推广url")
    private String extendUrl;


    /**
     * 推广海报图1
     */
    @ApiModelProperty(value = "推广海报图1")
    private String registerQrcodeMap1;

    /**
     * 推广海报图2
     */
    @ApiModelProperty(value = "推广海报图2")
    private String registerQrcodeMap2;

    /**
     * 推广海报图3
     */
    @ApiModelProperty(value = "推广海报图3")
    private String registerQrcodeMap3;

}
