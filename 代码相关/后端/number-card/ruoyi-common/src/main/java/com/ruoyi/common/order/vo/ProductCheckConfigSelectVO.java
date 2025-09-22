package com.ruoyi.common.order.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 产品校验配置查询返回类
 */
@Data
@ApiModel(value = "产品校验配置查询返回类")
public class ProductCheckConfigSelectVO {

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
