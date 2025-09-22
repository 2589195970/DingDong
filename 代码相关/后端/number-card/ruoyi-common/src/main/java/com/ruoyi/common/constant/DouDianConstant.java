package com.ruoyi.common.constant;


/**
 * 抖店相关参数
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/7/18 17:36
 */
public class DouDianConstant {

    private DouDianConstant() {
    }


    /**
     *  抖店
     */
    public static final int DOU_DIAN = 0;

    /*
     * 回传状态：
     * 1、异步下单
     * 2、发货回传
     * 3、失败回传
     * 4、订单延时处理
     */
    public static final int ASYNC_ORDER = 1;

    public static final int SHIPMENT_REVIEW = 2;

    public static final int DEFEAT_REVIEW = 3;

    public static final int DELAY_ORDER = 4;

    /**
     * 抖店接口域名
     */
    public static final String DOUDIAN_URL = "https://openapi-fxg.jinritemai.com";

    /**
     * API协议版本，当前版本为2
     */
    public static final String APP_V = "2";

    /**
     * 签名加密方法
     */
    public static final String SIGN_METHOD = "hmac-sha256";
    /**
     * 抖店cache的cacheName
     */
    public static final String DOUDIAN_CACHE_NAME = "doudianCache";

    /**
     * 抖店订单防重cachekey
     */
    public static final String DOUDIAN_ORDER_REPEAT = "doudian_repeat";

    /**
     * 抖店token的cacheKey
     */
    public static final String DOUDIAN_TOKEN_KEY = "doudian_access_token";

    /**
     * 抖店token的订单cacheKey
     */
    public static final String DOUDIAN_ORDER_KEY = "doudian_order";

    /**
     * 抖店的shopid
     */
    public static final String DOUDIAN_SHOP_ID = "30741844";

    /**
     * 获取token的API名称
     */
    public static final String DOUDIAN_TOKEN_CREATE = "token.create";

    /**
     * 获取token的接口地址
     */
    public static final String DOUDIAN_TOKEN_CREATE_ADD = "/token/create";

    /**
     * 获取订单列表的API名称
     */
    public static final String DOUDIAN_ORDER_LIST = "order.searchList";

    /**
     * 获取订单列表的接口地址
     */
    public static final String DOUDIAN_ORDER_LIST_ADD = "/order/searchList";

    /**
     * 获取订单详情的API名称
     */
    public static final String DOUDIAN_ORDER_DETAIL = "order.orderDetail";

    /**
     * 获取订单详情的接口地址
     */
    public static final String DOUDIAN_ORDER_DETAIL_ADD = "/order/orderDetail";

    /**
     * 订单回调API名称
     */
    public static final String DOUDIAN_ORDER_REVIEW = "order.review";

    /**
     * 订单回调的接口地址
     */
    public static final String DOUDIAN_ORDER_REVIEW_ADD = "/order/review";

    /**
     * 发货回调API名称
     */
    public static final String DOUDIAN_ORDER_LOGISTICSADD = "order.logisticsAdd";

    /**
     * 发货回调的接口地址
     */
    public static final String DOUDIAN_ORDER_LOGISTICSADD_ADD = "/order/logisticsAdd";

    /**
     * 解密API名称
     */
    public static final String DOUDIAN_ORDER_BATCHDECRYPT = "order.batchDecrypt";

    /**
     * 解密接口地址
     */
    public static final String DOUDIAN_ORDER_BATCHDECRYPT_ADD = "/order/batchDecrypt";



    /**
     * AES加密秘钥
     */
    public static final String AES_KEY = "17d7029a02e1fbc2224cbcb157d7f338";






}
