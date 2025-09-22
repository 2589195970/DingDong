package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 余额查询
 */
@Data
public class OrderNumberStatusLogSelectBO extends BaseBO{

    /**
     * 检测号码
     */
    @ApiModelProperty(value = "检测号码")
    private String phone;

    /**
     * 请求类型
     * 请求类型 0 炫咖移动余额查询 1炫咖联通余额查询 2炫咖电信余额查询 3炫咖携号转网 4 炫咖号码查询 5 炫咖空号检测 6 额查查余额查询 7 额查查携号查询 8 额查查空号查询
     */
    @ApiModelProperty(value = "请求类型")
    private Integer type;


    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Long starTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Long endTime;

}
