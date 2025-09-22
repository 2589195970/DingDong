package com.ruoyi.common.order.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 获取号池列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("获取号池列表单个号码")
public class NumberDTO {

    /**
     * 号码id
     */
    @ApiModelProperty(value = "号码ID")
    private String numberId;

    /**
     * 号池id
     */
    @ApiModelProperty(value = "号池id")
    private String poolId;

    /**
     * 号码
     */
    @ApiModelProperty(value = "号码")
    private String number;

    /**
     * 额外参数
     */
    @ApiModelProperty(value = "额外参数")
    private String extendJson;
}
