package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上游API类型返回
 */
@Data
@ApiModel(value = "上游API类型返回")
public class UpstreamApiTypeVO {

    /**
     * api 类型
     */
    @ApiModelProperty("api 类型")
    private String upstreamApiType;

    /**
     * api 名称
     */
    @ApiModelProperty("api 名称")
    private String upstreamApiName;

    /**
     * api参数简介
     */
    @ApiModelProperty("api参数简介")
    private String apiIntro;

    /**
     * 产品配置参数简介
     */
    @ApiModelProperty("产品配置参数简介")
    private String productIntro;
}
