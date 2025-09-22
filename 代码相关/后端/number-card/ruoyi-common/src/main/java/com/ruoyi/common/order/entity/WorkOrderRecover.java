package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工单详情
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 16:39
 */
@TableName(value = "t_work_order_recover", autoResultMap = true)
@Data
public class WorkOrderRecover {

    /**
     * 工单ID
     */
    @TableId(type = IdType.AUTO)
    private Integer workOrderRecoverId;

    /**
     * 工单ID
     */
    @ApiModelProperty(value = "工单ID")
    private Integer workOrderId;

    /**
     * sysUser表ID
     */
    @ApiModelProperty(value = "sysUser表ID")
    private Long sysUserId;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String recoverContent;

    /**
     * 工单附件图片url
     */
    @ApiModelProperty(value = "工单附件图片url")
    private String recoverUrl;

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


}
