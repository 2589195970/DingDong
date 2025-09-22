package com.ruoyi.web.controller.console;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.order.dto.SmsDTO;
import com.ruoyi.console.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("/sms")
@Slf4j
@Api(tags = "短信发送接口")
public class SmsController {
    public static final String TAG = "SmsController";

    @Resource
    SmsService smsService;

    /**
     * 短信发送
     *
     * @return
     */
    /*@PostMapping("/sendSms")
    @ApiOperation("短信发送")
    public ResponseEntity sendSms(@RequestBody SmsDTO smsDTO) {
        try {
            smsService.sendSms(smsDTO);
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", "sendSms", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }*/

    /**
     * 短信发送
     *
     * @return
     */
    @GetMapping("/sendSms")
    @ApiOperation("短信发送")
    public ResponseEntity sendSms(@RequestParam(required = false, value = "data") String data) {
        try {
            smsService.sendSms(JSONObject.parseObject(decryptedBase64(BaseConstant.AES_KEY,data),SmsDTO.class));
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", "sendSms", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     *
     * @return
     */
    public String encryptedBase64(String aesKey,String content){
        // 创建AES对象，使用ECB模式和PKCS5Padding填充
        AES aes = new AES("ECB", "PKCS5Padding", aesKey.getBytes());
        // 加密操作
        byte[] encryptedBytes = aes.encrypt(content);
        // 将加密后的字节数组转换为Base64字符串
        String encryptedBase64 = Base64.encode(encryptedBytes);
        System.out.println("加密后的明文: " + encryptedBase64);
        return encryptedBase64;
        /*// 解密操作
        byte[] decryptedBytes = aes.decrypt(Base64.decode(encryptedBase64));
        String decryptedText = new String(decryptedBytes);
        System.out.println("解密后的明文: " + decryptedText);*/
    }

    /**
     *
     * @return
     */
    public String decryptedBase64(String aesKey,String encryptedBase64){
        // 创建AES对象，使用ECB模式和PKCS5Padding填充
        AES aes = new AES("ECB", "PKCS5Padding", aesKey.getBytes());
        // 解密操作
        byte[] decryptedBytes = aes.decrypt(Base64.decode(encryptedBase64));
        String decryptedText = new String(decryptedBytes);
        System.out.println("解密后的明文: " + decryptedText);
        return decryptedText;
    }

}
