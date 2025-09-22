package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工具配置表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/2/22 16:39
 */
@TableName(value = "t_tool_config", autoResultMap = true)
@Data
public class ToolConfig {

    /**
     * 工具配置ID
     */
    @TableId(type = IdType.AUTO)
    private Integer toolConfigId;

    /**
     * 访问key
     */
    @ApiModelProperty("访问key")
    private String accessKey;

    /**
     * 秘钥key
     */
    @ApiModelProperty("秘钥key")
    private String secretKey;

    /**
     * 短信签名
     */
    @ApiModelProperty("短信签名")
    private String signName;

    /**
     * 工具配置类型 1 阿里云短信 2七牛云短信
     */
    @ApiModelProperty("工具配置类型 1 阿里云短信 2七牛云短信")
    private Integer toolConfigType;

    /**
     * 空间
     */
    @ApiModelProperty("空间")
    private String bucket;

    /**
     * 域名url
     */
    @ApiModelProperty("域名url")
    private String domainNameUrl;

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
