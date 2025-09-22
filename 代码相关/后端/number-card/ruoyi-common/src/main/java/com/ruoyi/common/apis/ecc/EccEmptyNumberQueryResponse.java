package com.ruoyi.common.apis.ecc;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 额查查空号检测返回值
 * @author 陈思伟
 */
@Data
@ApiModel("额查查空号检测返回值")
public class EccEmptyNumberQueryResponse {


    /**
     * 1为实号；2为空号；3为停机；4为流量卡；5为沉默号
     */
    private String typecode;

    /**
     * 类型描述"沉默号"
     */
    private String desc;

    /**
     * 手机号
     */
    private String number;

    /**
     * 城市号码
     */
    private String area_num;

    /**
     * 运营商名称CMCC:中国移动；CUCC：中国联通；CTCC：中国电信；
     */
    private String isp;

    /**
     * 省份
     */
    private String prov;


    /**
     * 城市
     */
    private String city;

    /**
     * 邮编
     */
    private String post_code;

    /**
     * 电话区号
     */
    private String area_code;

    /**
     * 身份证省份前6位号码
     */
    private String card_prov_code;

    /**
     * 身份证市前6位号码
     */
    private String card_city_code;


    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 城市拼音
     */
    private String city_code;

    /**
     * 城市缩写
     */
    private String city_short_code;


    /**
     * 是否是携号 1携号转网，0未转网
     */
    private String mnpstatus;

    /**
     * 运营商名称
     */
    private String isp_name;



}
