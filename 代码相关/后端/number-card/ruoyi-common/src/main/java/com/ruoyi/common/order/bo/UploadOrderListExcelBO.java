package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 订单导入
 */
@Data
public class UploadOrderListExcelBO {

    /**
     * 产品编码
     */
    @ApiModelProperty("产品编码")
    private String productCode;

    /**
     * 下游代理商code
     */
    @ApiModelProperty("下游代理商code")
    private String downstreamCode;

    /**
     * 导入文件
     */
    @ApiModelProperty("导入文件")
    private MultipartFile file;

}
