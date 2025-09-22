package com.ruoyi.order.controller;

import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.OrderListBO;
import com.ruoyi.common.order.bo.ProductH5BO;
import com.ruoyi.common.order.bo.ProductListBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.vo.OrderListVO;
import com.ruoyi.common.order.vo.ProductH5VO;
import com.ruoyi.common.order.vo.ProductListVO;
import com.ruoyi.order.service.AgentService;
import com.ruoyi.order.service.ProductService;
import com.ruoyi.order.service.order.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 产品获取
 *
 * @Description
 */
@RestController
@RequestMapping("/product")
@Slf4j
@Api(tags = "order服务产品相关接口")
public class ProductController {
    public static final String TAG = "ProductController";

    @Resource
    private ProductService productService;

    @Resource
    OrderService orderService;

    @Resource
    AgentService agentService;

    /**
     * 获取产品 h5相关信息
     *
     * @return
     */
    @PostMapping("/h5Info")
    @ApiOperation("获取产品 h5相关信息")
    public ResponseEntity<ProductH5VO> orderSubmitInfo(@RequestBody ProductH5BO productH5BO) {
        try {
            return ResponseEntity.success(productService.getProductH5(productH5BO));
        } catch (BizException e) {
            log.info("{} h5Info方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{} h5Info方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("提交订单信息出错,请稍候重试",null);
        }
    }


    /**
     * 根据代理商code 获取产品列表
     * @return
     */
    @PostMapping("/getAgentProductList")
    @ApiOperation("根据代理商code 获取产品列表")
    public ResponseEntity<List<ProductListVO>> getAgentProductList(@RequestBody ProductListBO productListBO) {
        try {
            return ResponseEntity.success(productService.getAgentProductList(productListBO));
        } catch (BizException e) {
            log.info("{} getAgentProductList方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{} getAgentProductList方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("提交订单信息出错,请稍候重试",null);
        }
    }

    /**
     * 订单列表查询
     * @return
     */
    @PostMapping("/selectOrderList")
    @ApiOperation("订单列表查询")
    public ResponseEntity<List<OrderListVO>> selectOrderList(@RequestBody OrderListBO orderListBO) {
        try {
            return ResponseEntity.success(orderService.selectOrderList(orderListBO));
        } catch (BizException e) {
            log.info("{} selectOrderList方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{} selectOrderList方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("提交订单信息出错,请稍候重试",null);
        }
    }

    /**
     * 根据代理商code 获取店铺聚合页图片
     * @return
     */
    @GetMapping("/getShopQrcodeMap")
    @ApiOperation("根据代理商code获取店铺聚合页图片")
    public ResponseEntity<String> getShopQrcodeMap(@RequestParam(required = false, value = "agentCode") String agentCode) {
        try {
            AgentAccount agentAccount = agentService.getAgentAccountByCode(agentCode);
            return ResponseEntity.success(agentAccount.getShopQrcodeMap());
        } catch (BizException e) {
            log.info("{} getAgentProductList方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{} getAgentProductList方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("提交订单信息出错,请稍候重试",null);
        }
    }
}
