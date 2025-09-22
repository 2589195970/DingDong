package com.ruoyi.common.order.reuqest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;


/**
 * API提交参数
 * 做一层拆分 避免临时参数添加到BaseSubmitOrderRequest中
 * @Description
 * @Author 陈思伟
 * @Date 2025-01-08 15:23
 */
@Data
@ToString(callSuper = true)
@ApiModel(description = "API接口提交参数")
public class APISubmitInfoRequest extends BaseSubmitOrderRequest {


}
