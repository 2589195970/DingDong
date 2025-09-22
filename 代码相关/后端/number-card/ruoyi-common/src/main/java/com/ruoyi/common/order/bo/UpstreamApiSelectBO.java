package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上游接口API查询
 */
@Data
public class UpstreamApiSelectBO extends BaseBO{

    /**
     * 上游API id
     */
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

}
