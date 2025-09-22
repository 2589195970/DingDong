package com.ruoyi.common.utils;

import com.aliyun.openservices.ons.api.Message;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 处理并记录日志文件
 * 
 * @author ruoyi
 */
@Slf4j
public class LogUtils
{
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }

    public static void printMq(Message message) {
        try {
            log.info("Receive mq, id:{}, topic:{}, tag:{}, body:{}", message.getMsgID(), message.getTopic(), message.getTag(), new String(message.getBody(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("print mq fail");
        }
    }
}
