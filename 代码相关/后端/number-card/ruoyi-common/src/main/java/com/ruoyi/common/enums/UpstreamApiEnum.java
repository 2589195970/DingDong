package com.ruoyi.common.enums;


import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.exception.base.BaseException;

/**
 * 上游接口状态相关枚举
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/11 18:47
 */
public enum UpstreamApiEnum {

   /* ME("fanfou", "饭否","fanfouServiceImpl","com.ruoyi.common.order.reuqest.BaseSubmitOrderRequest","参数配置说明：\n" +
            "参数1：合作方ID（挖金客提供）\n" +"参数2：秘钥（挖金客提供）\n" +"参数3：share_id （发展人ID）\n" +"参数4：aes_key （aes加密秘钥）","参数配置说明：\n" +
            "参数1：产品ID（挖金客提供）\n",0,""),*/

    WJK("wjk", "挖金客","wjkServiceImpl","com.ruoyi.common.order.reuqest.BaseSubmitOrderRequest","参数配置说明：\n" +
            "参数1：合作方ID（挖金客提供）\n" +"参数2：秘钥（挖金客提供）\n","参数配置说明：\n" +
            "参数1：产品ID（挖金客提供）\n",0,""),

    GTH("gth", "感叹号","gthServiceImpl","com.ruoyi.common.order.reuqest.BaseSubmitOrderRequest","参数配置说明：\n" +
            "参数1：发展人ID（感叹号提供）\n" +"参数2：ApiToken（感叹号提供）\n"+"参数3：AES加密秘钥（感叹号提供）\n","参数配置说明：\n" +
            "参数1：商品名称（感叹号提供）\n"+"参数2：商品编码（挖金客提供）\n",0,""),


    YQE("yqe", "172","yqeServiceImpl","com.ruoyi.common.order.reuqest.BaseSubmitOrderRequest","参数配置说明：\n" +
            "参数1：172号卡登录账号\n" +"参数2：secret秘钥（172提供）\n","参数配置说明：\n" +
            "参数1：产品ID（172号卡后台商品列表第一列获取）\n",0,""),
    ;


    UpstreamApiEnum(String upstreamApiType, String upstreamApiName, String interfaceClassName, String parameterClassName, String apiIntro, String productIntro,Integer isAsync,String callbackUrl) {
        this.upstreamApiType = upstreamApiType;
        this.upstreamApiName = upstreamApiName;
        this.interfaceClassName = interfaceClassName;
        this.parameterClassName = parameterClassName;
        this.apiIntro = apiIntro;
        this.productIntro = productIntro;
        this.isAsync = isAsync;
        this.callbackUrl = callbackUrl;
    }


    public static UpstreamApiEnum getUpstreamApiEnum(String upstreamApiType) throws BizException {
        for (UpstreamApiEnum value : UpstreamApiEnum.values()) {
            if (value.getUpstreamApiType().equals(upstreamApiType)) {
                return value;
            }
        }
       throw new BizException("接口API类型不存在");
    }

    /**
     * api 类型
     */
    private String upstreamApiType;

    /**
     * api 名称
     */
    private String upstreamApiName;

    /**
     * 接口类名称
     */
    private String interfaceClassName;

    /**
     * 参数类名称
     */
    private String parameterClassName;

    /**
     * api参数简介
     */
    private String apiIntro;

    /**
     * 产品配置参数简介
     */
    private String productIntro;

    /**
     * 是否异步 0 同步 1 异步
     */
    private Integer isAsync;

    /**
     * 回调地址
     */
    private String callbackUrl;


    public String getUpstreamApiType() {
        return upstreamApiType;
    }

    public String getUpstreamApiName() {
        return upstreamApiName;
    }

    public String getInterfaceClassName() {
        return interfaceClassName;
    }

    public String getParameterClassName() {
        return parameterClassName;
    }

    public String getApiIntro() {
        return apiIntro;
    }

    public String getProductIntro() {
        return productIntro;
    }

    public Integer getIsAsync() {
        return isAsync;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
}
