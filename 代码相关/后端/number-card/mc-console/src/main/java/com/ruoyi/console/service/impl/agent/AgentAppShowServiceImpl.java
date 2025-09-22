package com.ruoyi.console.service.impl.agent;


import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.vo.AgentActivateOrderAPPStatisticsVO;
import com.ruoyi.common.order.vo.AgentOrderAPPStatisticsVO;
import com.ruoyi.common.order.vo.AgentWithdrawalAPPStatisticsVO;
import com.ruoyi.console.mapper.OrderMapper;
import com.ruoyi.console.mapper.WithdrawalRecordDetailsMapper;
import com.ruoyi.console.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 代理商 移动端展示接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class AgentAppShowServiceImpl  implements AgentAppShowService {

    @Resource
    OrderMapper orderMapper;

    @Resource
    WithdrawalRecordDetailsMapper withdrawalRecordDetailsMapper;

    @Resource
    AgentAccountService agentAccountService;


    /**
     * 查询代理商订单统计
     * type 0 自身 1 团队
     * @return
     * @throws BizException
     */
    public AgentOrderAPPStatisticsVO selectAgentOrderAPPStatistics(Integer type, LoginUser loginUser) throws BizException {
        //获取代理账户信息
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        if(type == null||type==BaseConstant.ZERO_INT){
            return orderMapper.selectAgentOrderAPPStatistics(agentAccount.getAgentCode(),null);
        }
        if(type != null||type==BaseConstant.ONE_INT){
            return orderMapper.selectAgentOrderAPPStatistics(null,agentAccount.getAgentCode());
        }
        return null;
    }


    /**
     * 查询代理商激活订单统计
     * type 0 自身 1 团队
     * @return
     * @throws BizException
     */
    public AgentActivateOrderAPPStatisticsVO selectActivateAgentOrderAPPStatistics(Integer type, LoginUser loginUser) throws BizException {
        //获取代理账户信息
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        if(type == null||type==BaseConstant.ZERO_INT){
            return orderMapper.selectActivateAgentOrderAPPStatistics(agentAccount.getAgentCode(),null);
        }
        if(type != null||type==BaseConstant.ONE_INT){
            return orderMapper.selectActivateAgentOrderAPPStatistics(null,agentAccount.getAgentCode());
        }
        return null;
    }

    /**
     * 查询代理商 提现数据
     * @return
     * @throws BizException
     */
    public AgentWithdrawalAPPStatisticsVO selectWithdrawalAPPStatistics(LoginUser loginUser) throws BizException {
        //获取代理账户信息
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        return withdrawalRecordDetailsMapper.selectWithdrawalAPPStatistics(agentAccount.getAgentCode());
    }
}
