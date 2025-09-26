package com.ruoyi.web.controller.console.agent;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentAccountSelectBO;
import com.ruoyi.common.order.bo.AgentTeamQueryBO;
import com.ruoyi.common.order.vo.AgentAccountListVO;
import com.ruoyi.common.order.vo.AgentTeamListVO;
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

    /**
     * 代理商列表查询
     *
     * @return 代理商列表分页结果
     */
    @PostMapping("/agentSelectOrderListPage")
    @ApiOperation("代理商分页查询订单")
    public ResponseEntity<PageResult<AgentAccountListVO>> selectAgentAccountListPage(@RequestBody AgentAccountSelectBO agentAccountSelectBO) {
        try {
            return ResponseEntity.success(agentAppShowService.selectAgentAccountListPage(agentAccountSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "agentSelectOrderListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "agentSelectOrderListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 获取我的直接团队列表
     *
     * @param queryBO 查询参数
     * @return 团队列表分页结果
     */
    @PostMapping("/getMyDirectTeamList")
    @ApiOperation("获取我的直接团队列表（包含团队统计）")
    public ResponseEntity<PageResult<AgentTeamListVO>> getMyDirectTeamList(@RequestBody AgentTeamQueryBO queryBO) {
        try {
            return ResponseEntity.success(agentAppShowService.getMyDirectTeamList(queryBO, getLoginUser()));
        } catch (BizException e) {
            log.info("getMyDirectTeamList方法异常:{}", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.error("getMyDirectTeamList方法异常:", e);
            return ResponseEntity.error("出错了,请稍候重试", null);
        }
    }

}
