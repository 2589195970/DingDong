package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.WithdrawalApplicationSelectBO;
import com.ruoyi.common.order.bo.WithdrawalApplicationUpdateBO;
import com.ruoyi.common.order.entity.WithdrawalApplication;
import com.ruoyi.common.order.entity.WithdrawalConfig;
import com.ruoyi.common.order.vo.ComputeRateVO;
import com.ruoyi.console.service.WithdrawalApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;



@RestController
@RequestMapping("/withdrawalApplication")
@Slf4j
@Api(tags = "提现申请相关接口")
public class WithdrawalApplicationController {
    public static final String TAG = "WithdrawalApplicationController";

    @Resource
    WithdrawalApplicationService withdrawalApplicationService;


    /**
     * 分页查询提现申请列表
     *
     * @return
     */
    @PostMapping("/selectWithdrawalApplicationListPage")
    @ApiOperation("分页查询提现申请列表")
    public ResponseEntity<PageResult<WithdrawalApplication>> selectWithdrawalApplicationListPage(@RequestBody WithdrawalApplicationSelectBO withdrawalApplicationSelectBO) {
        try {
            return ResponseEntity.success(withdrawalApplicationService.selectWithdrawalApplicationListPage(withdrawalApplicationSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectWithdrawalApplicationListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectWithdrawalApplicationListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 更新申请提现记录
     *
     * @return
     */
    @PostMapping("/updateWithdrawalApplication")
    @ApiOperation("更新申请提现记录")
    public ResponseEntity updateWithdrawalApplication(@RequestBody WithdrawalApplicationUpdateBO withdrawalApplicationUpdateBO) {
        try {
            withdrawalApplicationService.updateWithdrawalApplication(withdrawalApplicationUpdateBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateWithdrawalApplication", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateWithdrawalApplication", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 删除提现记录详情
     * @return
     */
    @GetMapping("/deleteWithdrawalApplication")
    @ApiOperation("删除提现申请")
    public ResponseEntity deleteWithdrawalRecordDetails(@RequestParam(required = true, value = "withdrawalApplicationId") Integer withdrawalApplicationId) {
        try {
            withdrawalApplicationService.deleteWithdrawalApplication(withdrawalApplicationId);
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteWithdrawalApplication", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 计算提现费率
     * @return
     */
    @GetMapping("/computeRate")
    @ApiOperation("计算提现费率")
    public ResponseEntity<ComputeRateVO> computeRate(@RequestParam(required = true, value = "withdrawalAmount") Integer withdrawalAmount) {
        try {
            return ResponseEntity.success(withdrawalApplicationService.computeRate(withdrawalAmount));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "computeRate", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        }catch (Exception e) {
            log.info("{}方法异常:{}", "computeRate", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


}
