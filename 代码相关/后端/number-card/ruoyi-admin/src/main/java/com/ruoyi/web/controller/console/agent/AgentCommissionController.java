package com.ruoyi.web.controller.console.agent;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.CommissionConfig;
import com.ruoyi.common.order.entity.WithdrawalRecordDetails;
import com.ruoyi.common.order.vo.AgentAccountListVO;
import com.ruoyi.common.order.vo.AgentCommissionSelectVO;
import com.ruoyi.console.service.AgentCommissionService;
import com.ruoyi.console.service.AgentManagementService;
import com.ruoyi.console.service.WithdrawalRecordDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * 代理商佣金相关接口
 */
@RestController
@RequestMapping("/agentCommission")
@Slf4j
@Api(tags = "代理商佣金相关接口")
public class AgentCommissionController {

    public static final String TAG = "AgentCommissionController";

    @Resource
    AgentCommissionService agentCommissionService;



    /**
     * 代理商订单佣金列表查询
     *
     * @return
     */
    @PostMapping("/selectOrderCommissionListPage")
    @ApiOperation("代理商订单佣金列表查询")
    public ResponseEntity<PageResult<AgentCommissionSelectVO>> selectOrderCommissionListPage(@RequestBody AgentCommissionSelectBO agentCommissionSelectBO) {
        try {
            return ResponseEntity.success(agentCommissionService.selectOrderCommissionListPage(agentCommissionSelectBO, getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 代理商佣金配置查询
     *
     * @return
     */
    @PostMapping("/selectAgentCommissionConfig")
    @ApiOperation("代理商佣金配置查询(此方法无参数)")
    public ResponseEntity<CommissionConfig> selectAgentCommissionConfig() {
        try {
            return ResponseEntity.success(agentCommissionService.selectAgentCommissionConfig(getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 代理商佣金配置修改
     *
     * @return
     */
    @PostMapping("/agentUpdateCommissionConfig")
    @ApiOperation("代理商佣金配置修改")
    public ResponseEntity agentUpdateCommissionConfig(@RequestBody CommissionConfig commissionConfig) {
        try {
            agentCommissionService.agentUpdateCommissionConfig(commissionConfig, getLoginUser());
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

}
