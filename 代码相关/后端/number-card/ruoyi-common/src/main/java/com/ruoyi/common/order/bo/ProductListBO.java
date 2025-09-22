package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 产品 H5 页面信息
 */
@Data
public class ProductListBO {

    /**
     * 代理商编码
     */
    @ApiModelProperty("产品CODE")
    private String agentCode;


    /**
     * 运营商类型
     *MOBILE(0, "中国移动"),
     * TELECOM(1, "中国电信"),
     * UNICOM(2, "中国联通"),
     * CMG(3, "中国广电"),
     */
    @ApiModelProperty("运营商类型 0 中国移动 1 中国电信 2 中国联通 3中国广电")
    private Integer operatorType;



}
