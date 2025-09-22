package com.ruoyi.common.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上游API 参数说明表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/18 16:39
 */
@TableName(value = "t_upstream_explain", autoResultMap = true)
@Data
public class UpstreamExplain {

    /**
     * 上游API id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("上游API 参数说明id")
    private Integer upstreamExplainId;

    /**
     * 上游API 名称
     */
    @ApiModelProperty("上游API 名称")
    private String upstreamApiName;


    /**
     * 上游API 类型
     */
    @ApiModelProperty("上游API类型")
    private String upstreamApiType;

    /**
     * 说明类型 0 API说明 1产品说明
     */
    @ApiModelProperty("说明类型 0 API说明 1产品说明")
    private Integer explainType;

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
