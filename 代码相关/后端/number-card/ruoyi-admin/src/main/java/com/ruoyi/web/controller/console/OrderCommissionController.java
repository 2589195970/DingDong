package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.OrderCommissionDetails;
import com.ruoyi.common.order.vo.OrderCommissionSelectVO;
import com.ruoyi.console.service.OrderCommissionImportService;
import com.ruoyi.console.service.OrderCommissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;



@RestController
@RequestMapping("/orderCommission")
@Slf4j
@Api(tags = "订单佣金相关接口")
public class OrderCommissionController {
    public static final String TAG = "OrderCommissionController";

    @Resource
    OrderCommissionService orderCommissionService;

    @Resource
    OrderCommissionImportService orderCommissionImportService;

    /**
     * 分页查询订单佣金列表
     *
     * @return
     */
    @PostMapping("/selectOrderCommissionListPage")
    @ApiOperation("分页查询订单佣金列表")
    public ResponseEntity<PageResult<OrderCommissionSelectVO>> selectOrderCommissionListPage(@RequestBody OrderCommissionSelectBO orderCommissionSelectBO) {
        try {
            return ResponseEntity.success(orderCommissionService.selectOrderCommissionListPage(orderCommissionSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectOrderCommissionListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectOrderCommissionListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 佣金结算数据导出
     *
     * @return
     */
    @PostMapping("/exportOrderCommissionList")
    @ApiOperation("佣金结算数据导出")
    public void exportOrderCommissionList(@RequestBody OrderCommissionSelectBO orderCommissionSelectBO, HttpServletResponse response) {
        try {
            orderCommissionService.exportOrderCommissionList(orderCommissionSelectBO,response);
        } catch (BizException e) {
            log.info("{}方法异常:{}", "exportOrderList", e.getMessage());
        } catch (Exception e) {
            log.info("{}方法异常:{}", "exportOrderList", e.getMessage());
        }
    }


    /**
     * 订单佣金明细列表查询
     *
     * @return
     */
    @GetMapping("/selectOrderCommissionDetailsList")
    @ApiOperation("订单佣金明细列表查询")
    public ResponseEntity<List<OrderCommissionDetails>> selectOrderCommissionDetailsList(@RequestParam(required = false, value = "orderCommissionId") Integer orderCommissionId) {
        try {
            return ResponseEntity.success(orderCommissionService.selectOrderCommissionDetailsList(orderCommissionId));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectOrderCommissionDetailsList", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectOrderCommissionDetailsList", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 更新订单结算状态
     *
     * @return
     */
    @PostMapping("/updateOrderCommissionStatus")
    @ApiOperation("更新订单结算状态")
    public ResponseEntity updateOrderCommissionStatus(@RequestBody OrderCommissionUpdateBO orderCommissionUpdateBO) {
        try {
            orderCommissionService.updateOrderCommissionStatus(orderCommissionUpdateBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateOrderCommissionStatus", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateOrderCommissionStatus", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 删除订单佣金记录
     *
     * @return
     */
    @PostMapping("/deleteOrderCommission")
    @ApiOperation("删除订单佣金记录")
    public ResponseEntity deleteOrderCommission(@RequestBody OrderCommissionUpdateBO orderCommissionUpdateBO) {
        try {
            orderCommissionService.deleteOrderCommission(orderCommissionUpdateBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "deleteOrderCommission", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteOrderCommission", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 导入佣金结算数据
     *
     * @return
     */
    @PostMapping("/uploadOrderCommissionExcel")
    @ApiOperation("导入佣金结算数据")
    public ResponseEntity uploadOrderCommissionExcel(UploadNumberListExcelBO uploadOrderListExcelBO) {
        try {
            orderCommissionImportService.uploadOrderCommissionExcel(uploadOrderListExcelBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "uploadOrderListExcel", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "uploadOrderListExcel", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }



}
