package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单日志表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 16:39
 */
@TableName(value = "t_order_log", autoResultMap = true)
@Data
public class OrderLog {

    /**
     * 提单日志ID
     */
    @TableId(type = IdType.AUTO)
    private Integer orderLogId;

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private String orderId;

    /**
     * 请求url
     */
    @ApiModelProperty(value = "请求url")
    private String requestUrl;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private String requestBody;


    /**
     * 请求返回
     */
    @ApiModelProperty(value = "请求返回")
    private String requestMsg;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码")
    private String productCode;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;




}
