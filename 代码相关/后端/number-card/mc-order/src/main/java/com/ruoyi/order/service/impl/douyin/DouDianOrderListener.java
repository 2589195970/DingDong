package com.ruoyi.order.service.impl.douyin;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.ruoyi.common.apis.douyin.ShopCommonMessage;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.message.DouYinMqMessage;
import com.ruoyi.common.utils.LogUtils;
import com.ruoyi.common.utils.RocketMqUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店MQ监听
 * @date 2025/7/20 23:02
 */
@Component
@Slf4j
public class DouDianOrderListener implements MessageListener {



    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        LogUtils.printMq(message);
        //消息反序列化
        DouYinMqMessage commonMessage = RocketMqUtils.fromMessage(message, DouYinMqMessage.class);
        if (null == commonMessage) {
            log.error("消息反序列化后为空");
            return Action.CommitMessage;
        }
        try {
            return Action.CommitMessage;
        } /*catch (BizException be) {
            log.info("抖音订单处理异常:{},topic:{},MsgID:{}", be.getMessage(),message.getTopic(),message.getMsgID());
            return Action.CommitMessage;
        }*/ catch (Exception e) {
            log.info("抖音订单处理异常:{},topic:{},MsgID:{}", e.getMessage(),message.getTopic(),message.getMsgID());
            return Action.ReconsumeLater;
        }
    }


    /**
     * 抖店处理逻辑
     * @param commonMessage
     * @throws BizException
     */
    private void douDianProcess(ShopCommonMessage commonMessage) throws BizException {
        try {
            //todo 此处根据不同消息状态 处理不同业务请求


        } catch (Exception e) {
            throw new BizException("抖店：{}", e.getMessage());
        }

    }













}
