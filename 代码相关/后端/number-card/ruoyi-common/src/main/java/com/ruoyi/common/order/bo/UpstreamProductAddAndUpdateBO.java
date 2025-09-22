package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上游接口新增更新
 */
@Data
public class UpstreamProductAddAndUpdateBO {


    @ApiModelProperty("上游产品ID")
    private Integer upstreamProductId;

    /**
     * 上游产品 名称
     */
    @ApiModelProperty("上游产品 名称")
    private String upstreamProductName;

    /**
     * 上游API id
     */
    @ApiModelProperty("上游API id")
    private Integer upstreamApiId;

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
