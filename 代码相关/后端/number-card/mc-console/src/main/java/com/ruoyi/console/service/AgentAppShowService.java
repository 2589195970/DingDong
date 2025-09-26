package com.ruoyi.console.service;


import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentAccountSelectBO;
import com.ruoyi.common.order.bo.AgentTeamQueryBO;
import com.ruoyi.common.order.vo.AgentAccountListVO;
import com.ruoyi.common.order.vo.AgentActivateOrderAPPStatisticsVO;
import com.ruoyi.common.order.vo.AgentOrderAPPStatisticsVO;
import com.ruoyi.common.order.vo.AgentTeamListVO;
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

    /**
     * 代理商列表查询
     *
     * @return
     * @throws BizException
     */
    PageResult<AgentAccountListVO> selectAgentAccountListPage(AgentAccountSelectBO agentAccountSelectBO) throws BizException;

    /**
     * 获取我的直接团队列表（包含团队统计）
     *
     * @param queryBO 查询参数
     * @param loginUser 当前登录用户
     * @return 团队列表
     * @throws BizException 业务异常
     */
    PageResult<AgentTeamListVO> getMyDirectTeamList(AgentTeamQueryBO queryBO, LoginUser loginUser) throws BizException;

}
