package com.ruoyi.common.apis.xuanka;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 炫咖携号转网返回值
 * @author 陈思伟
 */
@Data
@ApiModel("炫咖携号转网返回值")
public class XkNumberShiftResponse {


    /**
     * 查询号码
     */
    private String number;

    /**
     * 归属地省
     */
    private String province;

    /**
     * 归属地市
     */
    private String city;

    /**
     * 原运营商代码
     */
    private String pri_isp;

    /**
     * 原运营商名称
     */
    private String pri_isp_name;

    /**
     * 现运营商名称
     */
    private String new_isp;

    /**
     * 现运营商名称
     */
    private String new_isp_name;

    /**
     * 是否携号转网，0：否；1:是
     */
    private Integer change;


}
