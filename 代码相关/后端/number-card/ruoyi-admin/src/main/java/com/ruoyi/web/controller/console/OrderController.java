package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.vo.OrderSelectVO;
import com.ruoyi.console.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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

    @Value("${submit.h5}")
    private String h5BaseUrl;

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
    public void exportOrderList(@RequestBody OrderSelectBO orderSelectBO, HttpServletResponse response) {
        try {
            orderService.exportOrderList(orderSelectBO, response);
        } catch (BizException e) {
            log.error("{}方法异常:{}", "exportOrderList", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            try {
                response.getWriter().write("{\"code\":500,\"msg\":\"" + e.getMessage() + "\"}");
            } catch (Exception ex) {
                log.error("写入错误响应失败:{}", ex.getMessage());
            }
        } catch (Exception e) {
            log.error("{}方法异常:{}", "exportOrderList", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            try {
                response.getWriter().write("{\"code\":500,\"msg\":\"导出失败:" + e.getMessage() + "\"}");
            } catch (Exception ex) {
                log.error("写入错误响应失败:{}", ex.getMessage());
            }
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
            return ResponseEntity.success("重推请求已发送，请几分钟后刷新订单列表查看结果");
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

    /**
     * 上传订单照片
     *
     * @return
     */
    @PostMapping("/uploadOrderPhotos")
    @ApiOperation("上传订单照片")
    public ResponseEntity uploadOrderPhotos(@RequestBody PhotoUploadBO photoUploadBO) {
        try {
            orderService.uploadOrderPhotos(photoUploadBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "uploadOrderPhotos", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "uploadOrderPhotos", e.getMessage());
            return ResponseEntity.error("上传照片失败,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 代理商提交照片审核
     *
     * @return
     */
    @PostMapping("/submitPhotoForAudit")
    @ApiOperation("代理商提交照片审核")
    public ResponseEntity submitPhotoForAudit(@RequestBody PhotoUploadBO photoUploadBO) {
        try {
            orderService.submitPhotoForAudit(photoUploadBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "submitPhotoForAudit", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "submitPhotoForAudit", e.getMessage());
            return ResponseEntity.error("提交审核失败,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 管理员审核照片
     *
     * @return
     */
    @PostMapping("/auditOrderPhotos")
    @ApiOperation("管理员审核照片")
    public ResponseEntity auditOrderPhotos(@RequestBody PhotoAuditBO photoAuditBO) {
        try {
            orderService.auditOrderPhotos(photoAuditBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "auditOrderPhotos", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "auditOrderPhotos", e.getMessage());
            return ResponseEntity.error("审核操作失败,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 查询订单照片状态
     *
     * @return
     */
    @GetMapping("/getOrderPhotoStatus")
    @ApiOperation("查询订单照片状态")
    public ResponseEntity getOrderPhotoStatus(@RequestParam(required = false, value = "orderId") Long orderId) {
        try {
            return ResponseEntity.success(orderService.getOrderPhotoStatus(orderId));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getOrderPhotoStatus", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getOrderPhotoStatus", e.getMessage());
            return ResponseEntity.error("查询照片状态失败,请稍候重试:"+e.getMessage(), null);
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
     * 获取H5页面基础URL配置
     *
     * @return
     */
    @GetMapping("/getH5Config")
    @ApiOperation("获取H5页面基础URL配置")
    public ResponseEntity<Map<String, Object>> getH5Config() {
        try {
            Map<String, Object> config = new HashMap<>();
            config.put("h5BaseUrl", h5BaseUrl);
            return ResponseEntity.success(config);
        } catch (Exception e) {
            log.error("{} getH5Config方法异常:{}", TAG, e.getMessage(), e);
            return ResponseEntity.error("获取H5配置失败,请稍候重试", null);
        }
    }

    /**
     * 分页查询待审核照片订单
     * 查询条件：产品 photo_required = 1 且 sfxysh = 1，订单 photo_status = 3
     *
     * @return
     */
    @PostMapping("/selectPhotoAuditOrders")
    @ApiOperation("分页查询待审核照片订单")
    public ResponseEntity<PageResult<OrderSelectVO>> selectPhotoAuditOrders(@RequestBody OrderSelectBO orderSelectBO) {
        try {
            return ResponseEntity.success(orderService.selectPhotoAuditOrders(orderSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectPhotoAuditOrders", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectPhotoAuditOrders", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

}
