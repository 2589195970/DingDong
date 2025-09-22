package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 上游产品查询
 */
@Data
public class UpstreamProductSelectBO extends BaseBO{

    /**
     * 上游API id
     */
    @ApiModelProperty("上游API id")
    @NotNull(message = "上游API ID不能为空")
    private Integer upstreamApiId;


    /**
     * 上游产品 名称
     */
    @ApiModelProperty("上游产品 名称")
    private String upstreamProductName;

    /**
     * 上游产品 CODE
     */
    @ApiModelProperty("上游产品 CODE")
    private String upstreamProductCode;


}
