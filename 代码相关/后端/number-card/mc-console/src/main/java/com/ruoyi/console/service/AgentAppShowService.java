package com.ruoyi.console.service;


import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.vo.AgentActivateOrderAPPStatisticsVO;
import com.ruoyi.common.order.vo.AgentOrderAPPStatisticsVO;
import com.ruoyi.common.order.vo.AgentWithdrawalAPPStatisticsVO;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2025/05/09 17:11
 */
public interface AgentAppShowService {


    /**
     * 查询代理商订单统计
     * type 0 自身 1 团队
     * @return
     * @throws BizException
     */
    AgentOrderAPPStatisticsVO selectAgentOrderAPPStatistics(Integer type, LoginUser loginUser) throws BizException;



    /**
     * 查询代理商激活订单统计
     * type 0 自身 1 团队
     * @return
     * @throws BizException
     */
    AgentActivateOrderAPPStatisticsVO selectActivateAgentOrderAPPStatistics(Integer type, LoginUser loginUser) throws BizException;


    /**
     * 查询代理商 提现数据
     * @return
     * @throws BizException
     */
    AgentWithdrawalAPPStatisticsVO selectWithdrawalAPPStatistics(LoginUser loginUser) throws BizException;
}
