package com.ruoyi.order.controller;

import com.ruoyi.common.apis.partner.PartnerSubmitOrderRequest;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.order.service.partner.PartnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 订单处理相关接口
 *
 * @Description
 */
@RestController
@RequestMapping("/partner")
@Slf4j
@Api(tags = "合作方进单API")
public class PartnerController {
    public static final String TAG = "PartnerController";

    @Resource
    private PartnerService partnerService;

    /**
     * 合作方提交订单信息接口
     *
     * @return
     */
    @PostMapping("/submitInfo")
    @ApiOperation("提交订单信息")
    public ResponseEntity orderSubmitInfo(@RequestBody PartnerSubmitOrderRequest partnerSubmitOrderRequest) {
        try {
            return ResponseEntity.success(partnerService.submitOrder(partnerSubmitOrderRequest));
        } catch (BizException e) {
            log.info("{} partner submitInfo方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage());
        } catch (Exception e) {
            log.info("{} partner submitInfo方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("提交订单信息出错,请稍候重试:{}", e.getMessage());
        }
    }


}
