package com.ruoyi.console.service.photo.util;

import cn.hutool.crypto.SecureUtil;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 感叹号签名工具类
 *
 * @author Claude
 * @date 2025/01/17
 */
@Slf4j
public class GthSignatureUtil {

    /**
     * 生成订单提交签名
     *
     * @param shareId 发展人ID
     * @param sku     商品SKU
     * @param sourceId 订单ID
     * @param apiToken API令牌
     * @return MD5签名
     */
    public static String generateSubmitSign(String shareId, String sku, String sourceId, String apiToken) {
        if (StringUtils.isEmpty(shareId) || StringUtils.isEmpty(sku) ||
            StringUtils.isEmpty(sourceId) || StringUtils.isEmpty(apiToken)) {
            throw new IllegalArgumentException("签名参数不能为空");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("share_id=").append(shareId)
                    .append("&sku=").append(sku)
                    .append("&source_id=").append(sourceId)
                    .append(apiToken);

        String signString = stringBuilder.toString();
        log.debug("生成签名原文: {}", signString);

        return SecureUtil.md5(signString).toLowerCase();
    }

    /**
     * 生成照片加密秘钥请求签名
     *
     * @param orderId 订单ID
     * @param shareId 发展人ID
     * @param apiToken API令牌
     * @return MD5签名
     */
    public static String generatePhotoSecretSign(Long orderId, String shareId, String apiToken) {
        if (orderId == null || StringUtils.isEmpty(shareId) || StringUtils.isEmpty(apiToken)) {
            throw new IllegalArgumentException("签名参数不能为空");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("order_id=").append(orderId)
                    .append("&share_id=").append(shareId)
                    .append(apiToken);

        String signString = stringBuilder.toString();
        log.debug("生成照片加密秘钥签名原文: {}", signString);

        return SecureUtil.md5(signString).toLowerCase();
    }

    /**
     * 验证签名格式
     *
     * @param sign 签名
     * @return 是否为有效格式
     */
    public static boolean isValidSign(String sign) {
        if (StringUtils.isEmpty(sign)) {
            return false;
        }

        // MD5签名应该是32位小写字母和数字
        return sign.matches("^[a-z0-9]{32}$");
    }
}