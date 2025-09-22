package com.ruoyi.common.utils;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;

import java.nio.charset.StandardCharsets;

/**
 * @author Dots
 */
public class RocketMqUtils {

    public static Message createMessage(String topic, String tag, Object obj) {
        String json = JSON.toJSONString(obj);
        byte[] body = json.getBytes(StandardCharsets.UTF_8);
        return new Message(topic, tag, body);
    }

    public static <T> T fromMessage(Message message, Class<T> cls) {
        String jsonStr = new String(message.getBody(), StandardCharsets.UTF_8);
        return JSON.parseObject(jsonStr, cls);
    }
}
