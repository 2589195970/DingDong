package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.OrderCommissionSelectBO;
import com.ruoyi.common.order.bo.OrderCommissionUpdateBO;
import com.ruoyi.common.order.bo.ProductSelectBO;
import com.ruoyi.common.order.bo.WithdrawalRecordDetailsSelectBO;
import com.ruoyi.common.order.entity.OrderCommissionDetails;
import com.ruoyi.common.order.entity.WithdrawalRecord;
import com.ruoyi.common.order.entity.WithdrawalRecordDetails;
import com.ruoyi.common.order.vo.OrderCommissionSelectVO;
import com.ruoyi.common.order.vo.ProductSelectVO;
import com.ruoyi.common.order.vo.RevenueVO;
import com.ruoyi.console.service.OrderCommissionService;
import com.ruoyi.console.service.WithdrawalRecordDetailsService;
import com.ruoyi.console.service.WithdrawalRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;


@RestController
@RequestMapping("/withdrawalRecord")
@Slf4j
@Api(tags = "提现记录相关接口")
public class WithdrawalRecordController {
    public static final String TAG = "WithdrawalRecordController";

    @Resource
    WithdrawalRecordService withdrawalRecordService;

    @Resource
    WithdrawalRecordDetailsService withdrawalRecordDetailsService;

    /**
     * 查询提现记录
     * @return
     */
    @GetMapping("/selectWithdrawalRecord")
    @ApiOperation("查询提现记录 不传参数默认返回 当前登录账号提现记录")
    public ResponseEntity<WithdrawalRecord> selectWithdrawalRecord(@RequestParam(required = false, value = "agentCode") String agentCode) {
        try {
            return ResponseEntity.success(withdrawalRecordService.selectWithdrawalRecord(agentCode, getLoginUser()));
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectWithdrawalRecord", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 分页查询提现记录详情列表
     *
     * @return
     */
    @PostMapping("/selectWithdrawalRecordDetailsListPage")
    @ApiOperation("分页查询提现记录详情列表")
    public ResponseEntity<PageResult<WithdrawalRecordDetails>> selectWithdrawalRecordDetailsListPage(@RequestBody WithdrawalRecordDetailsSelectBO withdrawalRecordDetailsSelectBO) {
        try {
            return ResponseEntity.success(withdrawalRecordDetailsService.selectWithdrawalRecordDetailsListPage(withdrawalRecordDetailsSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectWithdrawalRecordDetailsListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectWithdrawalRecordDetailsListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 删除提现记录详情
     * @return
     */
    @GetMapping("/deleteWithdrawalRecordDetails")
    @ApiOperation("删除提现记录详情")
    public ResponseEntity deleteWithdrawalRecordDetails(@RequestParam(required = true, value = "withdrawalRecordDetailsId") Integer withdrawalRecordDetailsId) {
        try {
            withdrawalRecordDetailsService.deleteWithdrawalRecordDetails(withdrawalRecordDetailsId);
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteWithdrawalRecordDetails", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 根据登陆账户 查询收益记录
     * @return
     */
    @GetMapping("/selectRevenue")
    @ApiOperation("根据登陆用户查询收益记录")
    public ResponseEntity<RevenueVO> selectRevenue() {
        try {
            return ResponseEntity.success(withdrawalRecordDetailsService.selectRevenue(getLoginUser()));
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectWithdrawalRecord", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

}
