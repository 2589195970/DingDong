package com.ruoyi.console.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.constant.SmsConstant;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.order.dto.SendSmsRequest;
import com.ruoyi.common.order.dto.SmsDTO;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.console.service.SmsService;
import com.ruoyi.console.service.ToolConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2023/8/24 17:13
 */

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Resource(name = "configStringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    ToolConfigService toolConfigService;

    @Resource
    HttpClient httpClient;

    /**
     * 发送短信
     * @param smsDTO
     * @throws BizException
     */
    @Override
    public void sendSms(SmsDTO smsDTO) throws BizException {
        String regex = "(\\+\\d+)?1\\d{10}$";
        if (!StringUtils.hasLength(smsDTO.getPhoneNumber())||!Pattern.matches(regex, smsDTO.getPhoneNumber())) {
            throw new BizException("手机号格式异常");
        }
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        if(checkSendSmsNumber(smsDTO.getPhoneNumber(),10)){
            throw new BizException("您当日短信发送已达上限");
        }
        //此处发送验证码 并保存到redis中
        int code = new Random().nextInt(9000) + 1000;
        String content = "【上信云】您申请的验证码:"+code+".有效期5分钟,为了您的安全请勿向他人泄露验证码";
        sendSmsRequest.setUserid(SmsConstant.SMS_USER_ID);
        sendSmsRequest.setAccount(SmsConstant.SMS_ACCOUNT);
        sendSmsRequest.setPassword(SmsConstant.SMS_PASSWORD);
        sendSmsRequest.setMobile(smsDTO.getPhoneNumber());
        sendSmsRequest.setContent(content);
        sendSmsRequest.setAction("send");
        sendSmsRequest.setCheckcontent("0");
        sendSmsRequest.setSendTime("");
        String msg;
        try {
           msg = httpClient.postFormForString(SmsConstant.SMS_UEL,BeanUtil.beanToMap(sendSmsRequest),null);
        }catch (Exception e){
            log.info("短信发送异常:{},{}",smsDTO.getPhoneNumber(),e.getMessage());
            throw new BizException("短信发送异常:{},{}",smsDTO.getPhoneNumber(),e.getMessage());
        }
        log.info("发送短信验证返回:{},{}",smsDTO.getPhoneNumber(), JSONObject.toJSONString(msg));
        //保存验证码到redis中
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.SMS_COED, smsDTO.getPhoneNumber(), smsDTO.getSmsTemplateType());
        stringRedisTemplate.opsForValue().set(cacheKey, String.valueOf(code), BaseConstant.TEN_INT, TimeUnit.MINUTES);
        saveSendSmsNumber(smsDTO.getPhoneNumber());
    }


    /**
     * 保存短信提交次数
     *
     * @throws Exception
     */
    protected void saveSendSmsNumber(String phone) {
        if (StrUtil.isBlankIfStr(phone)) {
            return;
        }
        String key = CacheUtils.generalKey(CacheKeyConstants.SEND_SMS_NUMBER,phone);
        long count = stringRedisTemplate.opsForValue().increment(key);
        if (count == 1) {
            stringRedisTemplate.expire(key, 24, TimeUnit.HOURS);
        }
    }

    /**
     * 检查失败提交次数
     *
     * @param
     * @param
     * @throws Exception
     */
    protected boolean checkSendSmsNumber(String phone,Integer number) {
        String key = CacheUtils.generalKey(CacheKeyConstants.SEND_SMS_NUMBER, phone);
        long count = 0;
        try {
            count = Long.parseLong(stringRedisTemplate.opsForValue().get(key));
        } catch (Exception e) {
        }
        if (count >= number) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkSms(SmsDTO smsDTO) {
        //默认0000通过认证 避免有时候联系不上人
        if (StringUtils.hasLength(smsDTO.getSmsCode()) && smsDTO.getSmsCode().equals(SmsConstant.SMS_FIXED_CODE)) {
            return true;
        }
        //校验验证码是否一致
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.SMS_COED, smsDTO.getPhoneNumber(), smsDTO.getSmsTemplateType());
        String code = stringRedisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.hasLength(smsDTO.getSmsCode()) && smsDTO.getSmsCode().equals(code)) {
            return true;
        }
        return false;
    }
}

