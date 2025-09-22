package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商产品表
 * 记录各个代理 产品可见及佣金
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/15 16:39
 */
@TableName(value = "t_product_check_config", autoResultMap = true)
@Data
public class ProductCheckConfig {

    /**
     * 产品校验配置表ID
     */
    @TableId(type = IdType.AUTO)
    private Integer productCheckConfigId;

    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private Integer productId;

    /**
     * 产品CODE 自动生成的编码
     */
    @ApiModelProperty("产品CODE")
    private String productCode;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 校验配置类型  0 年龄限制 1 地域限制
     */
    @ApiModelProperty("校验配置类型  0 年龄限制 1 地域限制")
    private Integer checkConfigType;

    /**
     * 策略 0 禁止 1允许
     */
    @ApiModelProperty("策略 0 禁止 1允许")
    private Integer tactics;

    /**
     * 地域限制类型 0省级 1市级
     */
    @ApiModelProperty("地域限制类型 0省级 1市级")
    private Integer territoryCheckType;

    /**
     * 地域 省列表 存放省信息
     */
    @ApiModelProperty("地域 省列表 存放省信息")
    private String territoryProvinceJson;

    /**
     * 地域 省列表 存放市信息
     */
    @ApiModelProperty("地域 省列表 存放市信息")
    private String territoryCityJson;

    /**
     * 最大年龄
     */
    @ApiModelProperty("最大年龄")
    private Integer ageMax;

    /**
     * 最小年龄
     */
    @ApiModelProperty("最小年龄")
    private Integer ageMin;

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
