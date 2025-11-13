package com.ruoyi.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.order.service.order.OrderService;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.reuqest.OrderSubmitRequest;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.order.bo.OrderUpdateBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 查询订单信息（用于照片修改）
     *
     * @return
     */
    @GetMapping("/getOrderInfo")
    @ApiOperation("查询订单信息")
    public ResponseEntity getOrderInfo(@RequestParam(required = false, value = "orderId") Long orderId) {
        try {
            Order order = orderService.selectOrderById(orderId);
            if (order == null) {
                return ResponseEntity.error("订单不存在", null);
            }

            // 转换为前端需要的格式
            Map<String, Object> orderInfo = new HashMap<>();
            orderInfo.put("orderId", order.getOrderId());
            orderInfo.put("cardName", order.getCardName());
            orderInfo.put("cardPhone", order.getCardPhone());
            orderInfo.put("cardId", order.getCardId());
            orderInfo.put("cardAddress", order.getCardAddress());
            orderInfo.put("provinceCode", order.getProvinceCode());
            orderInfo.put("provinceName", order.getProvinceName());
            orderInfo.put("cityCode", order.getCityCode());
            orderInfo.put("cityName", order.getCityName());
            orderInfo.put("countyCode", order.getCountyCode());
            orderInfo.put("countyName", order.getCountyName());

            // 照片URL
            orderInfo.put("idCardFrontUrl", order.getIdCardFrontUrl());
            orderInfo.put("idCardBackUrl", order.getIdCardBackUrl());
            orderInfo.put("personPhotoUrl", order.getPersonPhotoUrl());
            orderInfo.put("customPhotoUrl", order.getCustomPhotoUrl());

            return ResponseEntity.success(orderInfo);
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getOrderInfo", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getOrderInfo", e.getMessage());
            return ResponseEntity.error("查询失败,请稍候重试:" + e.getMessage(), null);
        }
    }

    /**
     * 更新订单信息（照片修改专用）
     *
     * @return
     */
    @PostMapping("/updateOrderInfo")
    @ApiOperation("更新订单信息")
    public ResponseEntity updateOrderInfo(@RequestBody OrderUpdateBO updateBO) {
        try {
            orderService.updateOrderInfo(updateBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateOrderInfo", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateOrderInfo", e.getMessage());
            return ResponseEntity.error("更新失败,请稍候重试:" + e.getMessage(), null);
        }
    }

    /**
     * 已提交订单更新照片信息（同步上游）
     */
    @PostMapping("/updateSubmittedOrderPhotos")
    @ApiOperation("已提交订单更新照片")
    public ResponseEntity updateSubmittedOrderPhotos(@RequestBody OrderUpdateBO updateBO) {
        try {
            orderService.updateSubmittedOrderPhotos(updateBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateSubmittedOrderPhotos", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateSubmittedOrderPhotos", e.getMessage());
            return ResponseEntity.error("同步上游失败:" + e.getMessage(), null);
        }
    }

    /**
     * 照片审核通过后提交订单
     */
    @PostMapping("/submitAfterPhotoAudit")
    @ApiOperation("照片审核通过后提交订单")
    public ResponseEntity submitAfterPhotoAudit(@RequestBody Map<String, Long> params) {
        try {
            Long orderId = params == null ? null : params.get("orderId");
            orderService.submitAfterPhotoAudit(orderId);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "submitAfterPhotoAudit", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "submitAfterPhotoAudit", e.getMessage());
            return ResponseEntity.error("提交订单失败:" + e.getMessage(), null);
        }
    }

}
