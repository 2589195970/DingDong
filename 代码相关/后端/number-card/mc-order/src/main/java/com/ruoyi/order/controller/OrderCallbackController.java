package com.ruoyi.order.controller;

import com.ruoyi.common.apis.gth.GthCallbackRequest;
import com.ruoyi.common.apis.wjk.WjkCallbackRequest;
import com.ruoyi.common.apis.yqe.YqeCallbackRequest;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.reuqest.ApiCommonNotifyRequest;
import com.ruoyi.order.service.common.CommonService;
import com.ruoyi.order.service.gth.GthService;
import com.ruoyi.order.service.wjk.WjkService;
import com.ruoyi.order.service.yqe.YqeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单回调处理
 *
 * @Description
 */
@RestController
@RequestMapping("/callback")
@Slf4j
@Api(tags = "订单回调处理")
public class OrderCallbackController {
    public static final String TAG = "OrderCallbackController";

    @Resource
    private WjkService wjkService;

    @Resource
    private GthService gthService;

    @Resource
    private YqeService yqeService;

    @Resource
    CommonService commonService;


    /**
     * 通用回调处理
     *
     * @return
     */
    @PostMapping("/commonCallback")
    @ApiOperation("通用回调处理")
    public ResponseEntity commonCallback(@RequestBody ApiCommonNotifyRequest request) {
        try {
            commonService.callback(request);
            return ResponseEntity.success();
        } catch (BizException e) {
            e.printStackTrace();
            log.info("{} commonCallback方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage());
        } catch (Exception e) {
            log.info("{} commonCallback方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("回调接收出错,请稍候重试:{}", e.getMessage());
        }
    }


    /**
     * 挖金客回调信息处理
     *
     * @return
     */
    @PostMapping("/wjkCallback")
    @ApiOperation("挖金客回调信息处理")
    public ResponseEntity wjkCallback(@RequestBody WjkCallbackRequest request) {
        try {
            wjkService.callback(request);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "wjkCallback", e.getMessage());
            return ResponseEntity.error(e.getMessage());
        } catch (Exception e) {
            log.info("{}方法异常:{}", "wjkCallback", e.getMessage());
            return ResponseEntity.error("回调接收出错,请稍候重试:"+e.getMessage(), e.getMessage());
        }
    }

    /**
     * 感叹号回调信息处理
     *
     * @return
     */
    @PostMapping("/gthCallback")
    @ApiOperation("感叹号回调信息处理")
    public String gthCallback(@ModelAttribute GthCallbackRequest request) {
        try {
            gthService.callback(request);
            return "SUCCESS";
        } catch (BizException e) {
            log.info("{}方法异常:{}", "gthCallback", e.getMessage());
            return "SUCCESS";
        } catch (Exception e) {
            log.info("{}方法异常:{}", "gthCallback", e.getMessage());
            return "SUCCESS";
        }
    }
    /**
     * 172回调信息处理
     *
     * @return
     */
    @PostMapping("/yqeCallback")
    @ApiOperation("172回调信息处理")
    public ResponseEntity yqeCallback(@ModelAttribute YqeCallbackRequest request) {
        try {
            yqeService.callback(request);
            return ResponseEntity.success(0,"上传成功");
        } catch (BizException e) {
            log.info("{}方法异常:{}", "yqeCallback", e.getMessage());
            return ResponseEntity.success(0,"上传成功");
        } catch (Exception e) {
            log.info("{}方法异常:{}", "yqeCallback", e.getMessage());
            return ResponseEntity.success(0,"上传成功");
        }
    }




}
