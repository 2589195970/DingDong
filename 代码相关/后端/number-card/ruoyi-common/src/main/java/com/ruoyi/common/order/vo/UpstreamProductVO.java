package com.ruoyi.common.order.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 上游产品表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/18 16:39
 */
@Data
public class UpstreamProductVO {

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


    /**
     * 关联产品列表(产品库中使用该上游产品的商品)
     */
    @ApiModelProperty("关联产品列表")
    private List<ProductBindVO> productList;

    /**
     * 是否需要上传照片 0 否 1 是
     */
    @ApiModelProperty("是否需要上传照片 0 否 1 是")
    private Integer photoRequired;

    /**
     * 是否需要审核 0 否 1 是
     */
    @ApiModelProperty("是否需要审核 0 否 1 是")
    private Integer sfxysh;

    /**
     * 是否参与佣金返现 0 否 1 是
     */
    @ApiModelProperty("是否参与佣金返现 0 否 1 是")
    private Integer sfyjfx;

    /**
     * 照片上传配置 JSON格式
     */
    @ApiModelProperty("照片上传配置 JSON格式")
    private String photoConfig;

    @Data
    public static class ProductBindVO {
        /**
         * 产品ID
         */
        @ApiModelProperty("产品ID")
        private Integer productId;

        /**
         * 产品编码
         */
        @ApiModelProperty("产品编码")
        private String productCode;

        /**
         * 产品名称
         */
        @ApiModelProperty("产品名称")
        private String productName;

        /**
         * 产品状态 0-下架 1-上架
         */
        @ApiModelProperty("产品状态 0-下架 1-上架")
        private Integer productStatus;

        /**
         * 产品状态文案
         */
        @ApiModelProperty("产品状态文案")
        private String productStatusName;

        /**
         * 上架时间
         */
        @ApiModelProperty("上架时间")
        private Long shelfTime;
    }
}
