package com.ruoyi.common.constant;


import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/17 17:36
 */
public class NumberStatusConstant {

    private NumberStatusConstant() {
    }

    /**
     * 成功
     */
    public static final int RESPONSE_SUCCESS = 200;

    public static final String KRY = "GOojiBkmKD29kO1OQPDFlucLar";

    /**
     * 移动余额查询
     */
    public static final String MOBILE_BALANCE_URL  = "https://api.taolale.com/api/Inquiry_Phone_Charges/get_yd";


    /**
     * 联通余额查询
     */
    public static final String UNICOM_BALANCE_URL  = "https://api.taolale.com/api/Inquiry_Phone_Charges/get_lt";


    /**
     * 电信余额查询
     */
    public static final String TELECOM_BALANCE_URL  = "https://api.taolale.com/api/Inquiry_Phone_Charges/get_dx";


    /**
     * 携号转网查询
     */
    public static final String NUMBER_SHIFT_URL  = "https://api.taolale.com/api/number_portability/get";

    /**
     * 号码查询查询 详版
     */
    public static final String NUMBER_QUERY_URL  = "https://api.taolale.com/api/Number_Inquiry/detail";

    /**
     * 空号检测
     */
    public static final String EMPTY_NUMBER_QUERY_URL  = "https://api.taolale.com/api/Empty_Number/senior";


    /**
     * appid：104638823389547578213
     * appkey：20537c1466b8037cabf561ac92bb99e7132
     */
    public static final String ECC_APP_ID = "104638823389547578213";


    public static final String ECC_APP_KEY = "20537c1466b8037cabf561ac92bb99e7132";


    /**
     * 额查查 空号检测
     */
    public static final String ECC_EMPTY_NUMBER_QUERY_URL  = "http://api.qiaosuan.net/api/queryKH";

    /**
     * 额查查 携号转网查询
     */
    public static final String ECC_NUMBER_SHIFT_URL  = "http://api.qiaosuan.net/api/queryXH";

    /**
     * 额查查 余额查询
     */
    public static final String ECC_BALANCE_URL  = "http://api.qiaosuan.net/api/queryHF";

    /**
     * 额查查 号码归属地查询
     */
    public static final String ECC_GSD_URL  = "http://api.qiaosuan.net/api/queryGSD";


}
