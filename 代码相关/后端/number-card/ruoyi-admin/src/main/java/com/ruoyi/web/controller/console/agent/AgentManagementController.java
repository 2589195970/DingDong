package com.ruoyi.web.controller.console.agent;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.WithdrawalRecordDetails;
import com.ruoyi.common.order.vo.AgentAccountListVO;
import com.ruoyi.console.service.AgentManagementService;
import com.ruoyi.console.service.WithdrawalRecordDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 代理商管理 admin相关接口
 */
@RestController
@RequestMapping("/agentManagement")
@Slf4j
@Api(tags = "代代理商管理(admin)相关接口")
public class AgentManagementController {

    public static final String TAG = "AgentManagementController";

    @Resource
    AgentManagementService agentManagementService;

    @Resource
    WithdrawalRecordDetailsService withdrawalRecordDetailsService;


    /**
     * 代理商列表查询
     *
     * @return
     */
    @PostMapping("/agentSelectOrderListPage")
    @ApiOperation("代理商分页查询订单")
    public ResponseEntity<PageResult<AgentAccountListVO>> selectAgentAccountListPage(@RequestBody AgentAccountSelectBO agentAccountSelectBO) {
        try {
            return ResponseEntity.success(agentManagementService.selectAgentAccountListPage(agentAccountSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 更新代理生效状态
     *
     * @return
     */
    @PostMapping("/updateAgentStatus")
    @ApiOperation("更新代理生效状态")
    public ResponseEntity updateAgentStatus(@RequestBody AgentUpdateEnabledBO agentUpdateEnabledBO) {
        try {
            agentManagementService.updateAgentStatus(agentUpdateEnabledBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 更新代理商订单加密状态
     *
     * @return
     */
    @PostMapping("/updateAgentEncryptStatus")
    @ApiOperation("更新代理商订单加密状态")
    public ResponseEntity updateAgentEncryptStatus(@RequestBody AgentUpdateEncryptBO agentUpdateEncryptBO) {
        try {
            agentManagementService.updateAgentEncryptStatus(agentUpdateEncryptBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 调整余额
     *
     * @return
     */
    @PostMapping("/updateBalance")
    @ApiOperation("调整余额")
    public ResponseEntity updateBalance(@RequestBody AgentUpdateBalanceBO agentUpdateEnabledBO) {
        try {
            agentManagementService.updateBalance(agentUpdateEnabledBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 删除代理商
     *
     * @return
     */
    @GetMapping("/deleteAgentAccount")
    @ApiOperation("删除代理商")
    public ResponseEntity deleteAgentAccount(@RequestParam(required = false, value = "agentAccountId") Integer agentAccountId) {
        try {
            agentManagementService.deleteAgentAccount(agentAccountId);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "deleteProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 代理商余额明细
     *
     * @return
     */
    @PostMapping("/selectWithdrawalRecordDetailsListPage")
    @ApiOperation("代理商余额查询明细列表")
    public ResponseEntity<PageResult<WithdrawalRecordDetails>> agentSelectOrderListPage(@RequestBody WithdrawalRecordDetailsSelectBO withdrawalRecordDetailsSelectBO) {
        try {
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
