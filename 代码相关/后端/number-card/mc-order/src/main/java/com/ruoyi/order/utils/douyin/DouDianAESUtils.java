package com.ruoyi.order.utils.douyin;


import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class DouDianAESUtils {

    /**
     * 编码格式
     */
    private final static String CHARSET = "UTF-8";

    /**
     * 加密算法
     */
    public final static String KEY_ALGORITHM = "AES";



    private final static String PASSWORD = "F04D000D014D9333446DE894539A3C84";


    /**
     * 福建移动存量偏移量
     */
    private static final String iv = "NIfb&95GUY86Gfgh";


    public static String encrypt(String content)
    {
        try
        {
            SecretKeySpec key = new SecretKeySpec(parseHexStr2Byte(PASSWORD), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);// 创建密码器
            byte[] byteContent = content.getBytes(CHARSET);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return parseByte2HexStr(result); // 加密
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @return
     */
    public static String decrypt(String content)
    {
        try
        {
            SecretKeySpec key = new SecretKeySpec(parseHexStr2Byte(PASSWORD), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(parseHexStr2Byte(content));
            return new String(result); // 加密
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密字符串
     *
     * @param content  需要被加密的字符串
     * @param password 加密需要的密码
     * @return 密文
     */
    public static String encryptStr(String content, String password) throws Exception {
        byte[] encrypt = encrypt(content, password);
        return parseByte2HexStr(encrypt);
    }

    /**
     * AES加密字符串
     *
     * @param content  需要被加密的字符串
     * @param password 加密需要的密码
     * @return 密文
     */
    public static byte[] encrypt(String content, String password) throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
        kgen.init(128, random);// 利用用户密码作为随机数初始化出
        // 128位的key生产者
        // 加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
        SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
        byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
        // null。
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        byte[] byteContent = content.getBytes("UTF-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
        byte[] result = cipher.doFinal(byteContent);// 加密
        return result;
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content  AES加密过过的内容
     * @param password 加密时的密码
     * @return 明文
     */
    public static String decryptStr(String content, String password) throws Exception {
        byte[] twoStrResult = parseHexStr2Byte(content);
        byte[] decrypt = decrypt(twoStrResult, password);
        return new String(decrypt, "UTF-8");
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content  AES加密过过的内容
     * @param password 加密时的密码
     * @return 明文
     */
    public static byte[] decrypt(byte[] content, String password) throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
        kgen.init(128, random);
        SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
        byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
        byte[] result = cipher.doFinal(content);
        return result; // 明文
    }


    /**
     * AES算法加密
     *
     * @Param:text原文
     * @Param:key密钥
     */
    public static String AESEncrypt(String text, String key) throws Exception {
        //创建AES加密算法实例(根据传入指定的秘钥进行加密)
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        //初始化为加密模式 并将密钥注入到算法中
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        // 将传入的文本加密
        byte[] encrypted = cipher.doFinal(text.getBytes());
        //生成密文
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String AESEncryptHexStr(String text, String key) throws Exception {
        //创建AES加密算法实例(根据传入指定的秘钥进行加密)
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        //初始化为加密模式 并将密钥注入到算法中
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        // 将传入的文本加密
        byte[] encrypted = cipher.doFinal(text.getBytes());
        //生成密文
        return parseByte2HexStr(encrypted);
    }


    /**
     * AES算法解密
     *
     * @Param:base64Encrypted密文
     * @Param:key密钥
     */
    public static String AESDecrypt(String base64Encrypted, String key) throws Exception {
        // 创建AES解密算法实例
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        // 初始化为解密模式，并将密钥注入到算法中
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] encrypted = Base64.getDecoder().decode(base64Encrypted);
        // 解密
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, "UTF-8");
    }

    public static byte[] decode(String base64EncodedString) {
        return Base64.getDecoder().decode(base64EncodedString);
    }
    public static String decryptAES(String data, String key) {
        try {
            byte[] encrypted1 = decode(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    public static String getKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String s = byteToHexString(b);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex = Integer.toHexString(bytes[i]);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }


    public static String aesHexDecrypt(String hexCiphertext, String key) throws Exception {
        // HEX解码
        byte[] encryptedBytes = Hex.decodeHex(hexCiphertext.toCharArray());

        // 处理密钥（MySQL AES_ENCRYPT兼容方式）
        byte[] keyBytes = key.getBytes("UTF-8");
        keyBytes = Arrays.copyOf(keyBytes, 16); // 强制128位

        // 初始化解密器（CBC模式+零向量IV）
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        // 执行解密
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, "UTF-8");
    }


}
