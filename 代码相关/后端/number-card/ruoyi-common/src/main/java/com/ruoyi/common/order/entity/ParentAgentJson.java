package com.ruoyi.common.order.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商父编码列表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 16:39
 */
@Data
public class ParentAgentJson {

    /**
     * sysUser表ID
     */
    @ApiModelProperty(value = "sysUser表ID")
    private Long sysUserId;

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码")
    private String agentCode;

    /**
     * 代理商名称
     */
    @ApiModelProperty(value = "代理商名称")
    private String agentName;

    /**
     * 等级
     */
    @ApiModelProperty(value = "等级0-5级")
    private Integer level;

}
