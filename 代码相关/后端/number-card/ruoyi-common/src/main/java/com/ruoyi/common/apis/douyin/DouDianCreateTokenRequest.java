package com.ruoyi.common.apis.douyin;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店请求token参数类
 * @date 2025/7/18 13:49
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class DouDianCreateTokenRequest extends DouDianBaseRequest {

    /**
     * 授权码,工具型应用传对应code值，自用型应用传""
     * 非必传
     */
    private String code;

    /**
     *授权类型,工具型应用:authorization_code 自用型应用:authorization_self
     * 必传
     */
    private String grantType;

    /**
     *判断测试店铺标识 ，非必传，若新增测试店铺传1，若不是则不必传
     * 非必传
     */
    private String testShop;

    /**
     *店铺ID
     * 非必传
     */
    private String shopId;

}
