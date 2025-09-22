package com.ruoyi.common.apis.douyin;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @Description 抖店请求公共类
 * @Author 陈思伟
 * @Date 2025-07-25 12:27
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("抖店请求公共类")
public class DouDianBaseRequest {
    /**
     * 调用的API接口名称
     * 示例：order.searchList
     * 必传
     */
    private String method;

    /**
     * 应用创建完成后被分配的key
     * 必传
     */
    private String appKey;

    /**
     * 需要先通过/token/create接口获取
     * 格式 edae7c30-8386-443b-88a1-031111596fdd
     * 必传（/token/create接口除外）
     */
    private String accessToken;

    /**
     * 标准json类型，里面是业务参数按照参数名的
     * 字符串大小排序具体参数值，参考每个接口的参数表
     */
    private String paramJson;

    /**
     * 时间戳，格式为yyyy-MM-dd HH:mm:ss，时区为GMT+8，例如：2016-01-01 12:00:00
     */
    private String timestamp;

    /**
     * API协议版本，当前版本为2
     */
    private String v;

    /**
     * 签名
     */
    private String sign;

    /**
     * 签名算法
     * 类型推荐使用hmac-sha256
     * 后续会下线md5（不传则默认为"md5"）1. md5:表示用md5算法加密字符串 2. hmac-sha256:表示用hmac_sha_256算法加密字符串
     */
    private String signMethod;
}
