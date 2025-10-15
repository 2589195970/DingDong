package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 照片审核业务对象
 *
 * @author 陈思伟
 * @date 2024/12/12
 */
@ApiModel("照片审核业务对象")
@Data
public class PhotoAuditBO {

    @ApiModelProperty("订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @ApiModelProperty("审核操作 1-审核通过 2-审核拒绝")
    @NotNull(message = "审核操作不能为空")
    private Integer auditAction;

    @ApiModelProperty("审核备注")
    @NotBlank(message = "审核备注不能为空")
    private String auditRemark;
}