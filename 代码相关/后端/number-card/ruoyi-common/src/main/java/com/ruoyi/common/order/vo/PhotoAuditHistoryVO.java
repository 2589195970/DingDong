package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 照片审核历史记录视图对象
 *
 * @author 陈思伟
 * @date 2024/12/12
 */
@ApiModel("照片审核历史记录视图对象")
@Data
public class PhotoAuditHistoryVO {

    @ApiModelProperty("记录ID")
    private Long id;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("操作类型 1-上传照片 2-代理商提交 3-管理员审核通过 4-管理员审核拒绝")
    private Integer operationType;

    @ApiModelProperty("操作类型名称")
    private String operationTypeName;

    @ApiModelProperty("操作员ID")
    private Long operatorId;

    @ApiModelProperty("操作员姓名")
    private String operatorName;

    @ApiModelProperty("操作时间")
    private Long operationTime;

    @ApiModelProperty("操作备注")
    private String remark;

    @ApiModelProperty("操作前状态")
    private Integer beforeStatus;

    @ApiModelProperty("操作前状态名称")
    private String beforeStatusName;

    @ApiModelProperty("操作后状态")
    private Integer afterStatus;

    @ApiModelProperty("操作后状态名称")
    private String afterStatusName;
}