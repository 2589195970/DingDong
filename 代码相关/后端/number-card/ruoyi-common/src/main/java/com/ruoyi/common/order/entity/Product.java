package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 产品表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/15 16:39
 */
@TableName(value = "t_product", autoResultMap = true)
@Data
public class Product {

    /**
     * 产品ID
     */
    @TableId(type = IdType.AUTO)
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
     * 运营商类型
     */
    @ApiModelProperty("运营商类型")
    private Integer operatorType;

    /**
     * 产品特征
     */
    @ApiModelProperty("产品特征")
    private String productCharacteristics;

    /**
     * 产品类型
     */
    @ApiModelProperty("产品类型")
    private Integer productType;

    /**
     * 产品备注
     */
    @ApiModelProperty("产品备注")
    private String productRemark;

    /**
     * 产品通用流量
     */
    @ApiModelProperty("产品通用流量")
    private String productTyll;

    /**
     * 产品定向流量
     */
    @ApiModelProperty("产品定向流量")
    private String productDxll;

    /**
     * 产品通话分钟
     */
    @ApiModelProperty("产品通话分钟")
    private String productThfz;

    /**
     * 产品优惠月租
     */
    @ApiModelProperty("产品优惠月租")
    private String productYhyz;

    /**
     * 产品原始月租
     */
    @ApiModelProperty("产品原始月租")
    private String productYsyz;

    /**
     * 产品首充说明
     */
    @ApiModelProperty("产品首充说明")
    private String productScsm;

    /**
     * 产品归属地区
     */
    @ApiModelProperty("产品归属地区")
    private String productGsdq;

    /**
     * 产品发货方式
     */
    @ApiModelProperty("产品发货方式")
    private String productFafs;

    /**
     * 产品合约期限
     */
    @ApiModelProperty("产品合约期限")
    private String productHyqx;

    /**
     * 产品套餐介绍
     */
    @ApiModelProperty("产品套餐介绍")
    private String productTcjs;

    /**
     * 产品推广要求
     */
    @ApiModelProperty("产品推广要求")
    private String productDemand;

    /**
     * 开启产品分单 0 关闭 1开启 开启后上游API和产品从配置表中读取
     */
    @ApiModelProperty("开启产品分单 0 关闭 1开启 开启后上游API和产品从配置表中读取")
    private Integer isDispatchUpstreamApi;

    /**
     * 是否复制产品 0  非复制 1复制
     */
    @ApiModelProperty("是否复制产品 0  非复制 1复制")
    private Integer isCopy;

    /**
     * 是否开放全部代理商 0 否 1是
     */
    @ApiModelProperty("是否开放全部代理商 0 否 1是")
    private Integer isAllAgent;

    /**
     * '上游API id'
     */
    @ApiModelProperty("上游API id")
    private Integer upstreamApiId;

    /**
     * '上游API CODE'
     */
    @ApiModelProperty("上游API CODE")
    private String upstreamApiCode;

    /**
     * 上游API 名称
     */
    @ApiModelProperty("上游API 名称")
    private String upstreamApiName;

    /**
     * '上游产品 id'
     */
    @ApiModelProperty("上游产品 id")
    private Integer upstreamProductId;

    /**
     * 上游产品CODE
     */
    @ApiModelProperty("上游产品CODE")
    private String upstreamProductCode;

    /**
     * 上游产品名称
     */
    @ApiModelProperty("上游产品名称")
    private String upstreamProductName;

    /**
     * 产品最小年龄(周岁)
     */
    @ApiModelProperty("产品最小年龄(周岁)")
    private Integer productAgeMin;

    /**
     * 产品最大年龄(周岁)
     */
    @ApiModelProperty("产品最大年龄(周岁)")
    private Integer productAgeMax;

    /**
     * 产品模板类型
     */
    @ApiModelProperty("产品模板类型")
    private Integer productTemplateType;

    /**
     * 产品主图URL
     */
    @ApiModelProperty("产品主图URL")
    private String productMasterMap;

    /**
     * 产品详情图url
     */
    @ApiModelProperty("产品详情图url")
    private String productDetailMap;

    /**
     * 产品海报图url
     */
    @ApiModelProperty("产品海报图url")
    private String productPlacardMap;

    /**
     * 产品二维码推广图url
     */
    @ApiModelProperty("产品二维码推广图url")
    private String productQrcodeMap;


    /**
     * 产品模板其他参数 json格式
     */
    @ApiModelProperty("产品模板其他参数")
    private String productTemplateJson;


    /**
     * 代理商产品状态 0 下架 1上架
     */
    @ApiModelProperty("代理商产品状态 0 下架 1上架")
    private Integer productStatus;

    /**
     * 产品佣金
     */
    @ApiModelProperty("产品佣金")
    private Integer productCommission;

    /**
     * 产品展示排序
     */
    @ApiModelProperty("产品展示排序")
    private Integer productSort;

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

    /**
     * 余额配置
     */
    @ApiModelProperty("余额配置")
    private Integer balanceConfig;


}
