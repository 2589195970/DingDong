package com.ruoyi.order.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店接口
 * @date 2025/7/20 16:44
 */
@RestController
@RequestMapping("/douyin")
@Slf4j
@Api(tags = "抖音接口")
public class DouYinController {
    public static final String TAG = "DouYinController";

    /*@Resource
    private DoudianService doudianService;*/


    /**
     * 抖店订阅消息接受
     *
     * @param
     * @return
     */
    /*@PostMapping("/receiveMessage")
    @ApiOperation("订阅消息接受")
    public String receiveMessage(@RequestHeader(required = true, value = "event-sign") String eventSign, @RequestHeader(required = true, value = "app-id") String appId, @RequestBody(required = true) String body) {
        try {
            return doudianService.receiveMessage(eventSign, appId, body);
        } catch (BizException e1) {
            log.error(e1.getMessage());
            return "";
        } catch (Exception e) {
            log.error(TAG, e);
            return "";
        }
    }*/

    /**
     * 获取或删除抖店缓存token
     *
     * @param type
     * @param shopId
     * @return
     */
    /*@GetMapping("/getOrDeleteToken")
    @ApiOperation("获取或删除抖店token")
    public String getOrDeleteToken(@RequestParam(required = false, value = "type") String type, @RequestParam(required = false, value = "shopId") String shopId) {
        try {
            return doudianService.getOrDeleteToken(type, shopId);
        } catch (BizException e1) {
            log.error(e1.getMessage());
            return "";
        } catch (Exception e) {
            log.error(TAG, e);
            return "";
        }
    }*/



}
