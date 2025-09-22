package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseBO {

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页大小")
    private Integer pageSize =20;

}
