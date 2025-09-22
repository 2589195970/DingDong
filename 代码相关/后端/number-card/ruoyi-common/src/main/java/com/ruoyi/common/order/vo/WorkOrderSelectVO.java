package com.ruoyi.common.order.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.order.entity.WorkOrderRecover;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 工单查询返回值
 */
@Data
@ApiModel(value = "工单查询返回值")
public class WorkOrderSelectVO {

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
     * 工单回复列表
     */
    @ApiModelProperty(value = "工单回复列表")
    List<WorkOrderRecover> workOrderRecovers;

}
