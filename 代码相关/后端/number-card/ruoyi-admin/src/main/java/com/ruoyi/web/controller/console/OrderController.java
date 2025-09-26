package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgainOrderBO;
import com.ruoyi.common.order.bo.OrderSelectBO;
import com.ruoyi.common.order.bo.UpdateOrderStatusBO;
import com.ruoyi.common.order.bo.UploadOrderListExcelBO;
import com.ruoyi.common.order.vo.OrderSelectVO;
import com.ruoyi.console.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
@Api(tags = "订单处理相关接口")
public class OrderController {
    public static final String TAG = "OrderController";

    @Resource
    OrderService orderService;

    /**
     * 分页查询订单
     *
     * @return
     */
    @PostMapping("/selectOrderListPage")
    @ApiOperation("分页查询订单")
    public ResponseEntity<PageResult<OrderSelectVO>> orderSubmitInfo(@RequestBody OrderSelectBO orderSelectBO) {
        try {
            return ResponseEntity.success(orderService.selectOrderListPage(orderSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectOrderListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectOrderListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 导出订单信息
     *
     * @return
     */
    @PostMapping("/exportOrderList")
    @ApiOperation("导出订单信息")
    public void exportOrderList(@RequestBody OrderSelectBO orderSelectBO,HttpServletResponse response) {
        try {
            orderService.exportOrderList(orderSelectBO,response);
        } catch (BizException e) {
            log.info("{}方法异常:{}", "exportOrderList", e.getMessage());
        } catch (Exception e) {
            log.info("{}方法异常:{}", "exportOrderList", e.getMessage());
        }
    }

    /**
     * 重推订单
     *
     * @return
     */
    @PostMapping("/againOrderSubmit")
    @ApiOperation("重推订单")
    public ResponseEntity againOrderSubmit(@RequestBody AgainOrderBO againOrderBO) {
        try {
            orderService.againOrderSubmit(againOrderBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "againOrderSubmit", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "againOrderSubmit", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 查询订单日志信息
     *
     * @return
     */
    @GetMapping("/selectOrderLogList")
    @ApiOperation("查询订单日志信息")
    public ResponseEntity selectOrderLogList(@RequestParam(required = false, value = "orderId") String orderId) {
        try {
            return ResponseEntity.success(orderService.selectOrderLogList(orderId));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectOrderLogList", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectOrderLogList", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 更新订单状态
     *
     * @return
     */
    @PostMapping("/updateOrderStatus")
    @ApiOperation("更新订单状态")
    public ResponseEntity updateOrderStatus(@RequestBody UpdateOrderStatusBO updateOrderStatusBO) {
        try {
            orderService.updateOrderStatus(updateOrderStatusBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateOrderStatus", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateOrderStatus", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 导入订单
     *
     * @return
     */
    @PostMapping("/uploadOrderListExcel")
    @ApiOperation("导入订单")
    public ResponseEntity uploadOrderListExcel(UploadOrderListExcelBO uploadOrderListExcelBO) {
        try {
            orderService.uploadOrderListExcel(uploadOrderListExcelBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "uploadOrderListExcel", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "uploadOrderListExcel", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 订单余额查询
     *
     * @return
     */
    @GetMapping("/selectOrderBalance")
    @ApiOperation("订单余额查询")
    public ResponseEntity selectOrderBalance(@RequestParam(required = false, value = "orderId") String orderId) {
        try {
            return ResponseEntity.success(orderService.selectOrderBalance(orderId));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectOrderBalance", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectOrderBalance", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 获取订单统计数据
     *
     * @return
     */
    @GetMapping("/getOrderStatistics")
    @ApiOperation("获取订单统计数据")
    public ResponseEntity<Map<String, Object>> getOrderStatistics() {
        try {
            return ResponseEntity.success(orderService.getOrderStatistics());
        } catch (BizException e) {
            log.info("{} getOrderStatistics方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{} getOrderStatistics方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("获取订单统计数据出错,请稍候重试",null);
        }
    }

    /**
     * 获取今日代理订单排名
     *
     * @return
     */
    @GetMapping("/getTodayAgentOrderRanking")
    @ApiOperation("获取今日代理订单排名")
    public ResponseEntity<Map<String, Object>> getTodayAgentOrderRanking() {
        try {
            return ResponseEntity.success(orderService.getTodayAgentOrderRanking());
        } catch (BizException e) {
            log.info("{} getTodayAgentOrderRanking方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{} getTodayAgentOrderRanking方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("获取今日代理订单排名出错,请稍候重试",null);
        }
    }

    /**
     * 获取订单趋势数据（30天）
     *
     * @return
     */
    @GetMapping("/getOrderTrend")
    @ApiOperation("获取订单趋势数据（30天）")
    public ResponseEntity<List<Map<String, Object>>> getOrderTrend() {
        try {
            return ResponseEntity.success(orderService.getOrderTrend());
        } catch (BizException e) {
            log.info("{} getOrderTrend方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{} getOrderTrend方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("获取订单趋势数据出错,请稍候重试",null);
        }
    }

}
