package com.ruoyi.common.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上游API表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/18 16:39
 */
@TableName(value = "t_upstream_api", autoResultMap = true)
@Data
public class UpstreamApi {

    /**
     * 上游API id
     */
    @TableId(type = IdType.AUTO)
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
     * 接口类名称
     */
    @ApiModelProperty("接口类名称")
    private String interfaceClassName;

    /**
     * 参数类名称
     */
    @ApiModelProperty("参数类名称")
    private String parameterClassName;

    /**
     * 是否异步接口 0 同步接口 1异步接口
     */
    @ApiModelProperty("是否异步接口 0 同步接口 1异步接口")
    private Integer isAsync;

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
