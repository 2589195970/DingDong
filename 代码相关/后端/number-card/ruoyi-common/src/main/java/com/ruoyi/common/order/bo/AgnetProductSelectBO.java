package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商产品查询
 */
@Data
public class AgnetProductSelectBO extends BaseBO{


    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 运营商类型
     */
    @ApiModelProperty("运营商类型")
    private Integer operatorType;


    /**
     * 产品状态 0 下架 1上架
     */
    @ApiModelProperty("产品状态 0 下架 1上架")
    private Integer productStatus;

}
