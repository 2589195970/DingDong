package com.ruoyi.console.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 上游照片上传配置
 *
 * @author Claude
 * @date 2025/01/17
 */
@Data
@Component
@ConfigurationProperties(prefix = "upstream.photo")
public class UpstreamPhotoConfig {

    /**
     * 是否启用照片上传功能
     */
    private boolean enabled = true;

    /**
     * 全局超时时间（毫秒）
     */
    private int timeout = 30000;

    /**
     * 重试次数
     */
    private int retryCount = 3;

    /**
     * 供应商配置
     */
    private Map<String, ProviderConfig> providers;

    /**
     * 供应商配置
     */
    @Data
    public static class ProviderConfig {
        /**
         * 供应商名称
         */
        private String name;

        /**
         * 基础URL
         */
        private String baseUrl;

        /**
         * API密钥/Token
         */
        private String apiKey;

        /**
         * AES加密密钥
         */
        private String aesKey;

        /**
         * 发展人ID
         */
        private String shareId;

        /**
         * 超时时间（毫秒）
         */
        private int timeout = 30000;

        /**
         * 是否启用
         */
        private boolean enabled = true;

        /**
         * 商品SKU
         */
        private String sku;

        /**
         * 来源SKU
         */
        private String sourceSku;
    }

    /**
     * 获取供应商配置
     */
    public ProviderConfig getProviderConfig(String providerCode) {
        if (providers == null) {
            return null;
        }
        return providers.get(providerCode);
    }

    /**
     * 检查供应商是否启用
     */
    public boolean isProviderEnabled(String providerCode) {
        if (!enabled) {
            return false;
        }
        ProviderConfig config = getProviderConfig(providerCode);
        return config != null && config.isEnabled();
    }
}