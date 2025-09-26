package com.ruoyi.common.constant;

import com.ruoyi.common.order.entity.UpstreamProduct;

/**
 * redis缓存key常量
 */
public class CacheKeyConstants {

    /**
     * 合作方进单序列号
     */
    public static final String PARTNER_REQUEST_SERIAL = "PARTNER_REQUEST_SERIAL";


    /**
     * 缓存时间
     */
    public static final Integer CATCH_TIME = 10;


    /**
     * 代理商账号缓存
     */
    public static final String AGENT_ACCOUNT_USER_ID = "AGENT_ACCOUNT_USER_ID";

    /**
     * 代理商账号缓存
     */
    public static final String AGENT_ACCOUNT_CODE = "AGENT_ACCOUNT_CODE";

    /**
     * 代理商子代理列表缓存
     */
    public static final String AGENT_ACCOUNT_CHILD_LIST = "AGENT_ACCOUNT_CHILD_LIST";


    /**
     * 地址缓存key缓存
     */
    public static final String ADDRESS_JSON = "ADDRESS_JSON";

    /**
     * 上游API缓存
     */
    public static final String UPSTREAM_PRODUCT_API = "UPSTREAM_PRODUCT_API";

    /**
     * 上游API产品缓存
     */
    public static final String UPSTREAM_PRODUCT_API_PRODUCT = "UPSTREAM_PRODUCT_API_PRODUCT";

    /**
     * 产品缓存
     */
    public static final String PRODUCT_API = "PRODUCT_API";

    /**
     * 产品缓存 包含已下架产品
     */
    public static final String PRODUCT_API_NOT_STATUS = "PRODUCT_API_NOT_STATUS";

    /**
     * 代理产品缓存
     */
    public static final String AGENT_PRODUCT_API = "AGENT_PRODUCT_API";


    /**
     * 代理产品佣金计算缓存
     */
    public static final String AGENT_PRODUCT_COMMISSION = "AGENT_PRODUCT_COMMISSION";

    /**
     * 代理商账号缓存
     */
    public static final String AGENT_ACCOUNT = "AGENT_ACCOUNT";

    /**
     * 产品H5缓存 信息
     */
    public static final String PRODUCT_H5_API = "PRODUCT_H5_API";

    /**
     * 短信发送间隔
     */
    public static final String SMS_SEND_TIME = "SMS_SEND_TIME";

    /**
     * 工具配置
     */
    public static final String TOOL_CONFIG = "TOOL_CONFIG";

    /**
     * 短信验证码
     */
    public static final String SMS_COED = "SMS_COED";

    /**
     * 产品校验缓存
     */
    public static final String PRODUCT_CHECK_CONFIG = "PRODUCT_CHECK_CONFIG";


    /**
     * 代理商产品列表
     */
    public static final String AGENT_PRODUCT_lIST_API = "AGENT_PRODUCT_lIST_API";



    /**
     * 号码发送短信次数
     */
    public static final String SEND_SMS_NUMBER = "SEND_SMS_NUMBER";

    /**
     * 产品分类统计缓存
     */
    public static final String PRODUCT_CATEGORY_COUNT_API = "PRODUCT_CATEGORY_COUNT_API";
}
