package com.ruoyi.console.service.impl.agent;


import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentAccountSelectBO;
import com.ruoyi.common.order.bo.AgentTeamQueryBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.vo.AgentAccountListVO;
import com.ruoyi.common.order.vo.AgentActivateOrderAPPStatisticsVO;
import com.ruoyi.common.order.vo.AgentOrderAPPStatisticsVO;
import com.ruoyi.common.order.vo.AgentTeamListVO;
import com.ruoyi.common.order.vo.AgentWithdrawalAPPStatisticsVO;
import org.springframework.beans.BeanUtils;
import com.ruoyi.console.mapper.AgentAccountMapper;
import com.ruoyi.console.mapper.OrderMapper;
import com.ruoyi.console.mapper.WithdrawalRecordDetailsMapper;
import com.ruoyi.console.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


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

    @Resource
    AgentAccountMapper agentAccountMapper;



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


    /**
     * 代理商列表查询
     *
     * @return
     * @throws BizException
     */
    public PageResult<AgentAccountListVO> selectAgentAccountListPage(AgentAccountSelectBO agentAccountSelectBO) throws BizException {
        //分页获取代理商列表 查询总数
        Integer totalRows = agentAccountMapper.selectAgentAccountListCount(agentAccountSelectBO.getParentAgentName(), agentAccountSelectBO.getAgentName(), agentAccountSelectBO.getIsRealName(), agentAccountSelectBO.getIsEnabled(), agentAccountSelectBO.getLevel());
        if (totalRows == null || totalRows == BaseConstant.ZERO_INT) {
            return PageResultFactory.createPageResult(new ArrayList<>(), 0L, agentAccountSelectBO.getPageSize(), agentAccountSelectBO.getPageNo());
        }
        //分页查询代理商产品
        List<AgentAccountListVO> agentAccountListVOS = agentAccountMapper.selectAgentAccountList(agentAccountSelectBO.getParentAgentName(),
                agentAccountSelectBO.getAgentName(), agentAccountSelectBO.getIsRealName(), agentAccountSelectBO.getIsEnabled(), agentAccountSelectBO.getLevel(),
                (agentAccountSelectBO.getPageNo() - 1) * agentAccountSelectBO.getPageSize(), agentAccountSelectBO.getPageSize());
        return PageResultFactory.createPageResult(agentAccountListVOS, Long.valueOf(totalRows), agentAccountSelectBO.getPageSize(), agentAccountSelectBO.getPageNo());
    }

    /**
     * 获取我的直接团队列表（包含团队统计）
     *
     * @param queryBO 查询参数
     * @param loginUser 当前登录用户
     * @return 团队列表
     * @throws BizException 业务异常
     */
    @Override
    public PageResult<AgentTeamListVO> getMyDirectTeamList(AgentTeamQueryBO queryBO, LoginUser loginUser) throws BizException {
        // 1. 获取当前代理商信息
        AgentAccount currentAgent = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);

        // 2. 设置查询条件 - 查询当前代理商的直接下级
        queryBO.setParentAgentCode(currentAgent.getAgentCode());

        // 3. 查询总数
        Integer totalCount = agentAccountMapper.selectDirectSubAgentsCount(queryBO);

        if (totalCount == null || totalCount == BaseConstant.ZERO_INT) {
            return PageResultFactory.createPageResult(
                new ArrayList<>(), 0L, queryBO.getPageSize(), queryBO.getPageNo());
        }

        // 4. 查询直接下级列表
        List<AgentAccountListVO> directAgents = agentAccountMapper.selectDirectSubAgentsList(
            queryBO,
            (queryBO.getPageNo() - 1) * queryBO.getPageSize(),
            queryBO.getPageSize()
        );

        // 5. 转换为带团队统计的VO
        List<AgentTeamListVO> resultList = new ArrayList<>();

        // 6. 为每个直接下级查询其团队成员数
        for (AgentAccountListVO agent : directAgents) {
            AgentTeamListVO teamVO = new AgentTeamListVO();
            // 复制基础信息
            BeanUtils.copyProperties(agent, teamVO);

            // 查询该代理商的直接下级人数（下下级）
            Integer teamCount = agentAccountMapper.countAgentTeamMembers(agent.getAgentCode());
            teamVO.setTeamMemberCount(teamCount != null ? teamCount : 0);

            resultList.add(teamVO);
        }

        return PageResultFactory.createPageResult(
            resultList, Long.valueOf(totalCount),
            queryBO.getPageSize(), queryBO.getPageNo());
    }

}
