package com.ruoyi.web.controller.console.agent;

import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.vo.AgentActivateOrderAPPStatisticsVO;
import com.ruoyi.common.order.vo.AgentOrderAPPStatisticsVO;
import com.ruoyi.common.order.vo.AgentWithdrawalAPPStatisticsVO;
import com.ruoyi.console.service.AgentAppShowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * 代理商账号相关接口
 */
@RestController
@RequestMapping("/agentAppShow")
@Slf4j
@Api(tags = "代理商账号相关接口")
public class AgentAppShowController {
    public static final String TAG = "AgentAppShowController";

    @Resource
    AgentAppShowService agentAppShowService;


    /**
     * 查询代理商订单统计
     *
     * @return
     */
    @GetMapping("/selectAgentOrderAPPStatistics")
    @ApiOperation("查询代理商订单统计")
    public ResponseEntity<AgentOrderAPPStatisticsVO> selectAgentOrderAPPStatistics(@RequestParam(required = false, value = "type") Integer type) {
        try {
            return ResponseEntity.success(agentAppShowService.selectAgentOrderAPPStatistics(type,getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectAgentOrderAPPStatistics", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectAgentOrderAPPStatistics", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 查询代理商激活订单统计
     *
     * @return
     */
    @GetMapping("/selectActivateAgentOrderAPPStatistics")
    @ApiOperation("查询代理商激活订单统计")
    public ResponseEntity<AgentActivateOrderAPPStatisticsVO> selectActivateAgentOrderAPPStatistics(@RequestParam(required = false, value = "type") Integer type) {
        try {
            return ResponseEntity.success(agentAppShowService.selectActivateAgentOrderAPPStatistics(type,getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectActivateAgentOrderAPPStatistics", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectActivateAgentOrderAPPStatistics", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 查询代理商 提现数据
     *
     * @return
     */
    @GetMapping("/selectWithdrawalAPPStatistics")
    @ApiOperation("查询代理商 提现数据")
    public ResponseEntity<AgentWithdrawalAPPStatisticsVO> selectWithdrawalAPPStatistics(@RequestParam(required = false, value = "type") Integer type) {
        try {
            return ResponseEntity.success(agentAppShowService.selectWithdrawalAPPStatistics(getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectWithdrawalAPPStatistics", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectWithdrawalAPPStatistics", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }
}
