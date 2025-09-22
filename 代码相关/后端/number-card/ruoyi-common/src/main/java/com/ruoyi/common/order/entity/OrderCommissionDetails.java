package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单佣金详情表 统计计算佣金
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/2/22 16:39
 */
@TableName(value = "t_order_commission_details", autoResultMap = true)
@Data
public class OrderCommissionDetails {

    /**
     * 订单佣金详情ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("订单佣金详情ID")
    private Integer orderCommissionDetailsId;

    /**
     * 佣金记录ID
     */
    @ApiModelProperty("佣金记录ID")
    private Integer orderCommissionId;

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private Long orderId;

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
     * 产品佣金
     */
    @ApiModelProperty(value = "产品佣金")
    private Integer productCommission;

    /**
     * 分销佣金
     */
    @ApiModelProperty(value = "分销佣金")
    private Integer distributionProductCommission;

    /**
     * 收入佣金(产品佣金-分销佣金)
     */
    @ApiModelProperty(value = "收入佣金(产品佣金-分销佣金)")
    private Integer revenueProductCommission;

    /**
     * 订单来源下游代理商CODE
     */
    @ApiModelProperty(value = "订单来源下游代理商CODE")
    private String agentSourceCode;

    /**
     * 订单来源下游代理商名称
     */
    @ApiModelProperty(value = "订单来源下游代理商名称")
    private String agentSourceName;

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
