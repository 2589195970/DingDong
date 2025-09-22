package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工单列表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 16:39
 */
@TableName(value = "t_work_order", autoResultMap = true)
@Data
public class WorkOrder {

    /**
     * 工单ID
     */
    @TableId(type = IdType.AUTO)
    private Integer workOrderId;

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
     * 0 流量卡 1 技术对接 10其他
     */
    @ApiModelProperty(value = "工单类型 0 流量卡 1 技术对接 10其他")
    private Integer workOrderType;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String workOrderTitle;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String workOrderContent;

    /**
     * 工单附件图片url
     */
    @ApiModelProperty(value = "工单附件图片url")
    private String workOrderUrl;

    /**
     * 工单状态 0 待管理回复 1待代理商回复 2已完结
     */
    @ApiModelProperty(value = "工单状态 0 待管理回复 1待代理商回复 2已完结")
    private Integer workOrderStatus;

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
