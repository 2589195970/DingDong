package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商API信息
 */
@Data
@ApiModel(value = "代理商API信息")
public class AgentApiVO {


    /**
     * api url
     */
    @ApiModelProperty(value = "api url")
    private String apiUrl;


    /**
     * 回调地址
     */
    @ApiModelProperty(value = "回调地址")
    private String callbackUrl;

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码")
    private String agentCode;


    /**
     * 代理商密钥
     */
    @ApiModelProperty(value = "代理商密钥")
    private String securityKey;

}
