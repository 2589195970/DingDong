package com.ruoyi.web.controller.console.agent;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.WithdrawalRecordDetailsSelectBO;
import com.ruoyi.common.order.entity.WithdrawalRecord;
import com.ruoyi.common.order.entity.WithdrawalRecordDetails;
import com.ruoyi.console.service.WithdrawalRecordDetailsService;
import com.ruoyi.console.service.WithdrawalRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * 代理商提现记录
 */
@RestController
@RequestMapping("/agentWithdrawalRecord")
@Slf4j
@Api(tags = "代理商佣金相关接口")
public class AgentWithdrawalRecordController {

    public static final String TAG = "AgentWithdrawalRecordController";

    @Resource
    WithdrawalRecordService withdrawalRecordService;

    @Resource
    WithdrawalRecordDetailsService withdrawalRecordDetailsService;


    /**
     * 代理商查询提现记录
     * @return
     */
    @GetMapping("/selectAgentWithdrawalRecord")
    @ApiOperation("查询提现记录")
    public ResponseEntity<WithdrawalRecord> selectAgentWithdrawalRecord() {
        try {
            return ResponseEntity.success(withdrawalRecordService.selectWithdrawalRecord(null, getLoginUser()));
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }


    /**
     * 代理商余额明细列表查询
     *
     * @return
     */
    @PostMapping("/selectAgentWithdrawalRecordListPage")
    @ApiOperation("代理商余额明细列表查询")
    public ResponseEntity<PageResult<WithdrawalRecordDetails>> agentSelectOrderListPage(@RequestBody WithdrawalRecordDetailsSelectBO withdrawalRecordDetailsSelectBO) {
        try {
            LoginUser loginUser = getLoginUser();
            withdrawalRecordDetailsSelectBO.setSysUserId(loginUser.getUserId());
            return ResponseEntity.success(withdrawalRecordDetailsService.selectWithdrawalRecordDetailsListPage(withdrawalRecordDetailsSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }


}
