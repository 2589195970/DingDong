package com.ruoyi.common.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上游产品表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/18 16:39
 */
@TableName(value = "t_upstream_product", autoResultMap = true)
@Data
public class UpstreamProduct {

    /**
     * 上游产品ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("上游产品ID")
    private Integer upstreamProductId;

    /**
     * 上游产品 名称
     */
    @ApiModelProperty("上游产品 名称")
    private String upstreamProductName;

    /**
     * 上游产品 CODE
     */
    @ApiModelProperty("上游产品CODE")
    private String upstreamProductCode;


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


    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Long updateTime;
}
