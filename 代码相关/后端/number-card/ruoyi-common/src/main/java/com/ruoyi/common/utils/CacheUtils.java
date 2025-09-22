package com.ruoyi.common.utils;


/**
 * @ClassName CacheUtils
 * @Author Cony
 * @Date 2024/12/27 15:07
 **/
public class CacheUtils {

    public static final String KEY_SEPARATOR = ":";

    public static String generalKey(String key, Object... params) {
        StringBuilder stringBuilder = new StringBuilder(key);
        for (Object obj : params) {
            if (obj != null) {
                stringBuilder.append(KEY_SEPARATOR);
                stringBuilder.append((obj.toString() + "").toUpperCase());
            }
        }
        return stringBuilder.toString();
    }

}
