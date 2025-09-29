package com.ruoyi.order.controller;

import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.OrderListBO;
import com.ruoyi.common.order.bo.PageViewBO;
import com.ruoyi.common.order.bo.ProductH5BO;
import com.ruoyi.common.order.bo.ProductListBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.vo.OrderListVO;
import com.ruoyi.common.order.vo.ProductH5VO;
import com.ruoyi.common.order.vo.ProductListVO;
import com.ruoyi.order.service.PageViewCollectService;
import com.ruoyi.order.service.AgentService;
import com.ruoyi.order.service.ProductService;
import com.ruoyi.order.service.order.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

    @Resource
    PageViewCollectService pageViewCollectService;


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

    /**
     * 记录页面访问
     * @param pageViewBO 访问参数
     * @param request HTTP请求对象
     * @return 记录结果
     */
    @PostMapping("/recordPageView")
    @ApiOperation("记录页面访问")
    public ResponseEntity<String> recordPageView(@Valid @RequestBody PageViewBO pageViewBO, HttpServletRequest request) {
        try {
            String ipAddress = getClientIpAddress(request);
            boolean success = pageViewCollectService.collectPageView(pageViewBO, ipAddress);
            if (success) {
                return ResponseEntity.success("访问记录成功");
            } else {
                return ResponseEntity.error("访问记录失败", null);
            }
        } catch (Exception e) {
            log.error("{} recordPageView方法异常:{}", TAG, e.getMessage(), e);
            return ResponseEntity.error("记录页面访问出错,请稍候重试", null);
        }
    }

    /**
     * 获取基础页面访问统计（供前端展示）
     * @param agentCode 代理商编码
     * @return 基础统计数据
     */
    @GetMapping("/getBasicStats")
    @ApiOperation("获取基础页面访问统计")
    public ResponseEntity<Map<String, Object>> getBasicStats(
            @RequestParam(required = false, value = "agentCode") String agentCode) {
        try {
            Map<String, Object> stats = pageViewCollectService.getBasicStats(agentCode);
            return ResponseEntity.success(stats);
        } catch (Exception e) {
            log.error("{} getBasicStats方法异常:{}", TAG, e.getMessage(), e);
            return ResponseEntity.error("获取基础统计出错,请稍候重试", null);
        }
    }

    /**
     * 获取客户端真实IP地址
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR",
                "X-Real-IP"
        };

        for (String headerName : headerNames) {
            String ip = request.getHeader(headerName);
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多级代理的情况，第一个IP为客户端真实IP
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                return ip;
            }
        }

        // 如果所有代理头都没有，使用request.getRemoteAddr()
        String ip = request.getRemoteAddr();

        // 处理IPv6本地地址
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }
}
