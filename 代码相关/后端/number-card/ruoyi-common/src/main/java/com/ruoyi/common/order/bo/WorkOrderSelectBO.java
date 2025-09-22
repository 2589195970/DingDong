package com.ruoyi.common.order.bo;

import com.ruoyi.common.order.entity.WorkOrder;
import com.ruoyi.common.order.entity.WorkOrderRecover;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 工单记录查询
 */
@Data
public class WorkOrderSelectBO extends BaseBO{

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码")
    private String agentCode;

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
     * 工单状态 0 待管理回复 1待代理商回复 2已完结
     */
    @ApiModelProperty(value = "工单状态 0 待管理回复 1待代理商回复 2已完结")
    private Integer workOrderStatus;


    /**
     * 工单回复列表
     */
    /*@ApiModelProperty(value = "工单回复列表")
    List<WorkOrderRecover> workOrderRecovers;*/
}
