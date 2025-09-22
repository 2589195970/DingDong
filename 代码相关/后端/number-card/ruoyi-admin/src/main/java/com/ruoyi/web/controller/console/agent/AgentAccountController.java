package com.ruoyi.web.controller.console.agent;

import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.vo.AgentStatisticsVO;
import com.ruoyi.console.service.AgentAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * 代理商账号相关接口
 */
@RestController
@RequestMapping("/agentAccount")
@Slf4j
@Api(tags = "代理商账号相关接口")
public class AgentAccountController {
    public static final String TAG = "AgentAccountController";

    @Resource
    AgentAccountService agentAccountService;

    /**
     * 代理商注册
     *
     * @return
     */
    @PostMapping("/addAgentAccount")
    @ApiOperation("代理商注册")
    public ResponseEntity addAgentAccount(@RequestBody AgentAccountAddBO agentAccountAddBO) {
        try {
            agentAccountService.addAgentAccount(agentAccountAddBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }


    /**
     * 查询登录用户子代理列表
     *
     * @return
     */
    @PostMapping("/selectChildAgentList")
    @ApiOperation("查询登录用户子代理列表")
    public ResponseEntity<List<AgentAccount>> selectChildAgentList() {
        try {
            return ResponseEntity.success(agentAccountService.selectChildAgentList(getLoginUser(), BaseConstant.ZERO_INT));
        } catch (BizException e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }


    /**
     * 登录账号子代理统计
     *
     * @return
     */
    @PostMapping("/selectChildAgentStatistics")
    @ApiOperation("登录账号子代理统计")
    public ResponseEntity<AgentStatisticsVO> selectChildAgentStatistics() {
        try {
            return ResponseEntity.success(agentAccountService.selectChildAgentStatistics(getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

}
