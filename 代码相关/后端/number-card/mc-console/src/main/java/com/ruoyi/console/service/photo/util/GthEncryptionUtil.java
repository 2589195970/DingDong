package com.ruoyi.console.service.photo.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.symmetric.AES;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 感叹号加密工具类
 *
 * @author Claude
 * @date 2025/01/17
 */
@Slf4j
public class GthEncryptionUtil {

    /**
     * AES加密
     *
     * @param content 明文内容
     * @param aesKey  AES密钥
     * @return Base64编码的密文
     */
    public static String aesEncrypt(String content, String aesKey) {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(aesKey)) {
            throw new IllegalArgumentException("加密内容和密钥不能为空");
        }

        try {
            // 创建AES对象，使用ECB模式和PKCS5Padding填充
            AES aes = new AES("ECB", "PKCS5Padding", aesKey.getBytes());

            // 加密操作
            byte[] encryptedBytes = aes.encrypt(content);

            // 将加密后的字节数组转换为Base64字符串
            return Base64.encode(encryptedBytes);

        } catch (Exception e) {
            log.error("AES加密失败: {}", e.getMessage(), e);
            throw new RuntimeException("加密失败: " + e.getMessage(), e);
        }
    }

    /**
     * AES解密
     *
     * @param encryptedContent Base64编码的密文
     * @param aesKey          AES密钥
     * @return 明文内容
     */
    public static String aesDecrypt(String encryptedContent, String aesKey) {
        if (StringUtils.isEmpty(encryptedContent) || StringUtils.isEmpty(aesKey)) {
            throw new IllegalArgumentException("解密内容和密钥不能为空");
        }

        try {
            // 创建AES对象，使用ECB模式和PKCS5Padding填充
            AES aes = new AES("ECB", "PKCS5Padding", aesKey.getBytes());

            // 解密操作
            byte[] decryptedBytes = aes.decrypt(Base64.decode(encryptedContent));

            return new String(decryptedBytes);

        } catch (Exception e) {
            log.error("AES解密失败: {}", e.getMessage(), e);
            throw new RuntimeException("解密失败: " + e.getMessage(), e);
        }
    }

    /**
     * 验证AES密钥格式
     *
     * @param aesKey AES密钥
     * @return 是否为有效格式
     */
    public static boolean isValidAesKey(String aesKey) {
        if (StringUtils.isEmpty(aesKey)) {
            return false;
        }

        // AES密钥可以是16、24或32字节
        int keyLength = aesKey.getBytes().length;
        return keyLength == 16 || keyLength == 24 || keyLength == 32;
    }
}