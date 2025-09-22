package com.ruoyi.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.order.service.order.OrderService;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.reuqest.OrderSubmitRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单处理相关接口
 *
 * @Description
 */
@RestController
@RequestMapping("/order")
@Slf4j
@Api(tags = "订单处理相关接口")
public class OrderController {
    public static final String TAG = "OrderController";

    @Resource
    private OrderService orderService;

    /**
     * 提交订单信息接口
     *
     * @return
     */
    @PostMapping("/submitInfo")
    @ApiOperation("提交订单信息")
    public ResponseEntity orderSubmitInfo(@RequestBody OrderSubmitRequest orderSubmitRequest) {
        try {
            return ResponseEntity.success(orderService.submitOrder(orderSubmitRequest));
        } catch (BizException e) {
            log.info("{}方法异常:{},报文:{}", "submitInfo", e.getMessage(), JSONObject.toJSONString(orderSubmitRequest));
            return ResponseEntity.error(e.getMessage());
        } catch (Exception e) {
            log.info("{}方法异常:{},报文:{}", "submitInfo", e.getMessage(),JSONObject.toJSONString(orderSubmitRequest));
            return ResponseEntity.error("提交订单信息出错,请稍候重试", e.getMessage());
        }
    }


}
