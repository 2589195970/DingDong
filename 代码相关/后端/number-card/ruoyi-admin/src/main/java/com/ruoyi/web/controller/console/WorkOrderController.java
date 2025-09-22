package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.ProductCheckConfig;
import com.ruoyi.common.order.entity.WithdrawalRecord;
import com.ruoyi.common.order.entity.WithdrawalRecordDetails;
import com.ruoyi.common.order.entity.WorkOrderRecover;
import com.ruoyi.common.order.vo.WorkOrderSelectVO;
import com.ruoyi.console.service.WithdrawalRecordDetailsService;
import com.ruoyi.console.service.WithdrawalRecordService;
import com.ruoyi.console.service.WorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;


@RestController
@RequestMapping("/workOrder")
@Slf4j
@Api(tags = "工单相关接口")
public class WorkOrderController {
    public static final String TAG = "WorkOrderController";

    @Resource
    WorkOrderService workOrderService;


    /**
     * 工单列表查询
     *
     * @return
     */
    @PostMapping("/selectWorkOrderListPage")
    @ApiOperation("工单列表查询")
    public ResponseEntity<PageResult<WorkOrderSelectVO>> selectWorkOrderListPage(@RequestBody WorkOrderSelectBO workOrderSelectBO) {
        try {
            return ResponseEntity.success(workOrderService.selectWorkOrderListPage(workOrderSelectBO,getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectWorkOrderListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectWorkOrderListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 新增工单记录
     *
     * @return
     */
    @PostMapping("/addWorkOrder")
    @ApiOperation("新增工单记录")
    public ResponseEntity addWorkOrder(@RequestBody WorkOrderAddBO workOrderAddBO) {
        try {
            workOrderService.addWorkOrder(workOrderAddBO,getLoginUser());
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "addWorkOrder", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "addWorkOrder", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 新增工单回复
     *
     * @return
     */
    @PostMapping("/addWorkOrderRecover")
    @ApiOperation("新增工单回复")
    public ResponseEntity addWorkOrderRecover(@RequestBody WorkOrderRecover workOrderRecover) {
        try {
            workOrderService.addWorkOrderRecover(workOrderRecover,getLoginUser());
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "addWorkOrderRecover", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "addWorkOrderRecover", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }



    /**
     * 删除工单
     *
     * @return
     */
    @GetMapping("/deleteWorkOrderRecover")
    @ApiOperation("删除工单")
    public ResponseEntity deleteWorkOrderRecover(@RequestParam(required = false, value = "workOrderId") Integer workOrderId) {
        try {
            workOrderService.deleteWorkOrderRecover(workOrderId);
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteWorkOrderRecover", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }


}
