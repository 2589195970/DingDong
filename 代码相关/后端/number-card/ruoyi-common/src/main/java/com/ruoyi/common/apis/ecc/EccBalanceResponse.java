package com.ruoyi.common.apis.ecc;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 额查查 余额查询返回值
 * @author 陈思伟
 */
@Data
@ApiModel("额查查 余额查询返回值")
public class EccBalanceResponse {


    /**
     * 号码
     */
    private String mobile;

    /**
     * 查询的余额
     */
    private String curFee;

    /**
     * 原始运营商
     */
    private String ori_carrier;

    /**
     * 新运营商
     */
    private String new_carrier;

    /**
     * 烟台
     */
    private String city;

    /**
     * 山东
     */
    private String prov;


}
