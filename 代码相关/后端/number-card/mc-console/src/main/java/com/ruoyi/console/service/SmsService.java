package com.ruoyi.console.service;


import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.dto.SmsDTO;

/**
 * @author 陈思伟
 * @version 1.0
 * @description: TODO
 * @date 2022/8/15 17:21
 */
public interface SmsService {

    /**
     * 发送验证码
     *
     * @param smsDTO
     */
    void sendSms(SmsDTO smsDTO) throws BizException;


    /**
     * 校验短信验证码
     *
     * @param smsDTO
     */
    boolean checkSms(SmsDTO smsDTO);




}
