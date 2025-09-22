package com.ruoyi.common.apis.douyin;

import lombok.Data;

/**
 * 陈思伟
 *
 * @author
 * @version 1.0
 * @description: 抖店云解密
 * @date 2025/7/18 12:47
 */
@Data
public class DouDianCloudDecryptRequest {

    /**
     * 待解密数据
     */
    DouDianBatchDecryptRequest douDianBatchDecryptRequest;

    /**
     * token
     */
    String accessToken;

    /**
     * appKey
     */
    String appKey;

    /**
     * 秘钥
     */
    String appSecret;


}
