package com.ruoyi.web.controller.console.agent;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.WithdrawalApplicationAddBO;
import com.ruoyi.common.order.bo.WithdrawalApplicationSelectBO;
import com.ruoyi.common.order.entity.WithdrawalApplication;
import com.ruoyi.console.service.AgentWithdrawalApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * 代理商提现申请相关接口
 */
@RestController
@RequestMapping("/agentWithdrawalApplication")
@Slf4j
@Api(tags = "代理商提现申请相关接口")
public class AgentWithdrawalApplicationController {
    public static final String TAG = "AgentWithdrawalApplicationController";

    @Resource
    AgentWithdrawalApplicationService agentWithdrawalApplicationService;


    /**
     * 申请提现记录查询
     *
     * @return
     */
    @PostMapping("/selectAgentWithdrawalApplicationListPage")
    @ApiOperation("申请提现记录查询")
    public ResponseEntity<PageResult<WithdrawalApplication>> selectAgentWithdrawalApplicationListPage(@RequestBody WithdrawalApplicationSelectBO withdrawalApplicationSelectBO) {
        try {
            return ResponseEntity.success(agentWithdrawalApplicationService.selectAgentWithdrawalApplicationListPage(withdrawalApplicationSelectBO, getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            e.getMessage();
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }


    /**
     * 代理商新增申请提现
     *
     * @return
     */
    @PostMapping("/addAgentWithdrawalApplication")
    @ApiOperation("代理商新增申请提现")
    public ResponseEntity addAgentWithdrawalApplication(@RequestBody WithdrawalApplicationAddBO withdrawalApplicationAddBO) {
        try {
            agentWithdrawalApplicationService.addAgentWithdrawalApplication(withdrawalApplicationAddBO, getLoginUser());
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
