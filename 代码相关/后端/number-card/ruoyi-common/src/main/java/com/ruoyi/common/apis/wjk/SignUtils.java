package com.ruoyi.common.apis.wjk;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class SignUtils {
    public static String getSign(String appKey, Object request) throws Exception {
        return SecureUtil.md5(getSignStr(appKey, request));
    }

    public static String getSignStr(String appKey, Object request) throws Exception {
        if (request == null || StrUtil.isBlankIfStr(appKey)) {
            throw new Exception("签名参数错误");
        }
        Field[] fields = ReflectUtil.getFields(request.getClass());
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        Collections.sort(fieldNames);
        StrBuilder sb = StrBuilder.create();
        for (String fieldName : fieldNames) {
            if (fieldName.equals("sign")) {
                continue;
            }
            if (ReflectUtil.getFieldValue(request, fieldName) != null) {
                sb.append(fieldName).append(ReflectUtil.getFieldValue(request, fieldName));
            }
        }
        return sb.toString() + appKey;
    }

}
