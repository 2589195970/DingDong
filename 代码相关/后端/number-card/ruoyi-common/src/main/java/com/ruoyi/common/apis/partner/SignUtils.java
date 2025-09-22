package com.ruoyi.common.apis.partner;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import java.lang.reflect.Field;
import java.net.URLEncoder;
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


    /**
     * certName=吴延华,certNum=371523199206055312,province=广东,city=深圳市,secret= zUOF20hUm4@p4yJT
     * 加密前：
     * appKey=dsfadsafassfadsafds&certName=%E5%90%B4%E5%BB%B6%E5%8D%8E&certNum=371523199206055312&city=%E6%B7%B1%E5%9C%B3%E5%B8%82&province=%E5%B9%BF%E4%B8%9C&secret=zUOF20hUm4%40p4yJT
     * 加密后的SIGN参数：
     * sign= 919B8860338B19B19B2EC70DFC0FB30B259039AC40658D724405F149E1F9643E
     *
     * @param args
     */
    public static void main(String[] args) {
        try {

            Map<String, String> map = new HashMap<>();
            map.put("certName","吴延华");
            map.put("certNum","371523199206055312");
            map.put("province","广东");
            map.put("city","深圳市");
            map.put("secret","zUOF20hUm4@p4yJT");
            map.put("appKey","dsfadsafassfadsafds");
            TreeMap<String, Object> treeMap = (TreeMap<String, Object>) BeanUtil.beanToMap(map, new TreeMap<String, Object>(), false, true);
            //拼接字符串
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
                if(StringUtils.hasLength(String.valueOf(entry.getValue()))){
                    stringBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(String.valueOf(entry.getValue()))).append("&");
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            log.info("签名生成:{}",stringBuilder.toString());
            String sign = DigestUtil.sha256Hex(stringBuilder.toString());
            log.info("签名:{}",sign);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
