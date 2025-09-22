package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商账号表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 16:39
 */
@TableName(value = "t_agent_account", autoResultMap = true)
@Data
public class AgentAccount {

    /**
     * 代理商账号表ID
     */
    @TableId(type = IdType.AUTO)
    private Integer agentAccountId;

    /**
     * sysUser表ID
     */
    @ApiModelProperty(value = "sysUser表ID")
    private Long sysUserId;

    /**
     * 代理商父编码
     */
    @ApiModelProperty(value = "代理商父编码")
    private String parentAgentCode;

    /**
     * 代理商父编码列表 json格式
     */
    @ApiModelProperty(value = "代理商父编码列表 json格式")
    private String parentAgentList;

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
     * 代理商密钥
     */
    @ApiModelProperty(value = "代理商密钥")
    private String securityKey;

    /**
     * 回调URL
     */
    @ApiModelProperty(value = "回调URL")
    private String callbackUrl;

    /**
     * 等级
     */
    @ApiModelProperty(value = "等级0-5级")
    private Integer level;

    /**
     * 是否实名 0否 1是
     */
    @ApiModelProperty(value = "是否实名 0否 1是")
    private Integer isRealName;


    /**
     * 身份证正面url
     */
    @ApiModelProperty(value = "身份证正面url")
    private String cardIdUrlFront;

    /**
     * 身份证反面url
     */
    @ApiModelProperty(value = "身份证反面url")
    private String cardIdUrlBack;

    /**
     * 店铺聚合页图
     */
    @ApiModelProperty(value = "店铺聚合页图")
    private String shopQrcodeMap;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 是否禁用 0启用 1禁用
     */
    @ApiModelProperty(value = "是否禁用 0启用 1禁用")
    private Integer isEnabled;


    /**
     *  0 订单加密 1 订单解密
     */
    @ApiModelProperty(value = "0 订单加密 1 订单解密")
    private Integer isEncrypt;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;

    /**
     * 推广海报图1
     */
    @ApiModelProperty(value = "推广海报图1")
    private String registerQrcodeMap1;

    /**
     * 推广海报图2
     */
    @ApiModelProperty(value = "推广海报图2")
    private String registerQrcodeMap2;

    /**
     * 推广海报图3
     */
    @ApiModelProperty(value = "推广海报图3")
    private String registerQrcodeMap3;


}
