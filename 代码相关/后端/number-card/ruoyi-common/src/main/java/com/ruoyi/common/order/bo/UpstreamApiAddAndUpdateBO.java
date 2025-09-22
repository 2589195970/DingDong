package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 上游接口新增更新
 */
@Data
public class UpstreamApiAddAndUpdateBO {


    @ApiModelProperty("上游API id")
    private Integer upstreamApiId;

    /**
     * 上游API 名称
     */
    @ApiModelProperty("上游API 名称")
    private String upstreamApiName;

    /**
     * 上游API CODE
     */
    @ApiModelProperty("上游API CODE")
    private String upstreamApiCode;

    /**
     * 上游API 类型
     */
    @ApiModelProperty("上游API类型")
    private String upstreamApiType;

    /**
     * 回调地址
     */
    @ApiModelProperty("回调地址")
    private String callbackUrl;

    /**
     * 参数1
     */
    @ApiModelProperty("参数1")
    private String argument1;

    /**
     * 参数2
     */
    @ApiModelProperty("参数2")
    private String argument2;

    /**
     * 参数3
     */
    @ApiModelProperty("参数3")
    private String argument3;

    /**
     * 参数4
     */
    @ApiModelProperty("参数4")
    private String argument4;

    /**
     * 参数5
     */
    @ApiModelProperty("参数5")
    private String argument5;

    /**
     * 参数6
     */
    @ApiModelProperty("参数6")
    private String argument6;

}
