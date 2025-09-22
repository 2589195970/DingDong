package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 新增工单
 */
@Data
public class WorkOrderAddBO {

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

}
