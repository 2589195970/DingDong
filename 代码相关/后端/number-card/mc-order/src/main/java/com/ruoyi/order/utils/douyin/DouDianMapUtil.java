package com.ruoyi.order.utils.douyin;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈思伟
 */
@Slf4j
public class DouDianMapUtil {
    private static final String TAG = "MapUtil";

    public static String getString(Map map, String key) throws Exception {
        if (!map.containsKey(key)) {
            throw new Exception(key + " 不存在");
        }
        return map.get(key).toString();
    }

    public static String getString(Map map, String key, String defaultVal)  {
        if (!map.containsKey(key)) {
            return defaultVal;
        }
        return map.get(key).toString();
    }

    public static Boolean getBoolean(Map map, String key, Boolean defaultVal)  {
        if (!map.containsKey(key)) {
            return defaultVal;
        }
        try{
            return Boolean.parseBoolean(map.get(key).toString());
        }catch (Exception e){
            log.error(TAG,e);
        }
        return defaultVal;
    }

    public static Integer getInteger(Map map, String key, Integer defaultVal)  {
        if (!map.containsKey(key)) {
            return defaultVal;
        }
        try{
            return Integer.parseInt(map.get(key).toString());
        }catch (Exception e){
            log.error(TAG,e);
        }
        return defaultVal;
    }


    public static Map<String, String> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();

        // 获取obj的Class对象
        Class<?> clazz = obj.getClass();

        // 获取类的所有字段
        Field[] fields = clazz.getDeclaredFields();

        // 遍历字段数组
        for (Field field : fields) {
            // 设置字段可访问（如果为private等修饰符）
            field.setAccessible(true);
            // 将字段名作为键，字段值作为值，存入map中
            map.put(field.getName(), String.valueOf(field.get(obj)));
        }

        return map;
    }

}
