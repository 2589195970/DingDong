/*
package com.ruoyi.console.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.dto.SmsDTO;
import com.ruoyi.common.order.entity.ToolConfig;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.console.service.SmsService;
import com.ruoyi.console.service.ToolConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.aliyun.teaopenapi.models.Config;

*/
/**
 * @Description
 * @Author 陈思伟
 * @Date 2023/8/24 17:13
 *//*

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Resource(name = "configStringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    ToolConfigService toolConfigService;


    */
/**
     * 短信模板code
     *//*

    public static final String SMS_TEMPLATE_CODE = "SMS_463130810";


    */
/**
     * 发送短信
     *
     * @param smsDTO
     *//*

    @Override
    public void sendSms(SmsDTO smsDTO) throws BizException {
        String timeKey = CacheUtils.generalKey(CacheKeyConstants.SMS_SEND_TIME, smsDTO.getPhoneNumber(), smsDTO.getSmsTemplateType());
        if (StringUtils.hasLength(stringRedisTemplate.opsForValue().get(timeKey))) {
            throw new BizException("已发送验证码,请稍后再试");
        }
        try {
            ToolConfig toolConfig =toolConfigService.getToolConfig(BaseConstant.ONE_INT);
            Client client = createClient(toolConfig.getAccessKey(),toolConfig.getSecretKey());
            SendSmsRequest sendSmsRequest = new SendSmsRequest();
            sendSmsRequest.setPhoneNumbers(smsDTO.getPhoneNumber());
            sendSmsRequest.setSignName(toolConfig.getSignName());
            sendSmsRequest.setTemplateCode(SMS_TEMPLATE_CODE);
            RuntimeOptions runtime = new RuntimeOptions();
            //此处发送验证码 并保存到redis中
            int code = new Random().nextInt(9000) + 1000;
            Map<String, String> map = new HashMap<>();
            map.put("code", String.valueOf(code));
            sendSmsRequest.setTemplateParam(JSONObject.toJSONString(map));
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            if (sendSmsResponse == null || !sendSmsResponse.getBody().code.equals("OK")) {
                String err = sendSmsResponse != null && sendSmsResponse.getBody() != null ? sendSmsResponse.getBody().message : null;
                throw new BizException("短信发送失败:{}", StringUtils.hasLength(err) ? err : "请检查号码格式");
            }
            log.info("号码{}发送短信验证码成功,验证码:{},类型:{}", smsDTO.getPhoneNumber(), code, smsDTO.getSmsTemplateType());
            //保存验证码到redis中
            String cacheKey = CacheUtils.generalKey(CacheKeyConstants.SMS_COED, smsDTO.getPhoneNumber(), smsDTO.getSmsTemplateType());
            stringRedisTemplate.opsForValue().set(cacheKey, String.valueOf(code), BaseConstant.TEN_INT, TimeUnit.MINUTES);
            //限制一个手机号1分钟内不能重复发送
            stringRedisTemplate.opsForValue().set(timeKey, String.valueOf(code), BaseConstant.ONE_INT, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.info("发送验证码异常:{},{}", JSONObject.toJSONString(smsDTO), e.getMessage());
            throw new BizException("发送验证码失败,请联系管理员");
        }
    }

    */
/**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     *//*

    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret);
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }


    */
/**
     * 校验验证码是否一致
     *
     * @param smsDTO
     *//*

    @Override
    public boolean checkSms(SmsDTO smsDTO) {
        //默认0000通过认证 避免有时候联系不上人
        if (StringUtils.hasLength(smsDTO.getSmsCode()) && smsDTO.getSmsCode().equals("0000")) {
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
*/
