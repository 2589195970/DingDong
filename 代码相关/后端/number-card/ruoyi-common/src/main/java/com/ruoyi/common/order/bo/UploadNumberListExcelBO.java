package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 订单导入
 */
@Data
public class UploadNumberListExcelBO {

    /**
     * 号码查询类型
     */
    @ApiModelProperty("号码查询类型")
    private Integer type;


    /**
     * 导入文件
     */
    @ApiModelProperty("导入文件")
    private MultipartFile file;

}
