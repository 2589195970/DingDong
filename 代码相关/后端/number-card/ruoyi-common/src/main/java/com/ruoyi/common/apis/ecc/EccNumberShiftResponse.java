package com.ruoyi.common.apis.ecc;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 额查查携号转网返回值
 * @author 陈思伟
 */
@Data
@ApiModel("额查查 携号转网返回值")
public class EccNumberShiftResponse {


    /**
     * 数据中的原始运营商
     */
    private String ori_carrier;

    /**
     * 数据中的新运营商
     */
    private String new_carrier;

    /**
     * 数据中的城市
     */
    private String city;

    /**
     * 数据中的省份
     */
    private String prov;

    /**
     * 是否携号转网，0：否；1:是
     */
    private Integer change;


}
