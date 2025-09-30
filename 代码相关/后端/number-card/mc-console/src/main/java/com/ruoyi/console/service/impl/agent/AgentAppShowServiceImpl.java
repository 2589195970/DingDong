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
import com.ruoyi.common.order.vo.AgentDashboardStatisticsVO;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
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

    @Resource
    PageViewService pageViewService;



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

        // 6. 为每个直接下级查询其团队成员数和浏览量统计
        for (AgentAccountListVO agent : directAgents) {
            AgentTeamListVO teamVO = new AgentTeamListVO();
            // 复制基础信息
            BeanUtils.copyProperties(agent, teamVO);

            // 查询该代理商的直接下级人数（下下级）
            Integer teamCount = agentAccountMapper.countAgentTeamMembers(agent.getAgentCode());
            teamVO.setTeamMemberCount(teamCount != null ? teamCount : 0);

            // 查询该代理商的浏览量统计
            try {
                // 今日浏览量
                Object todayStats = pageViewService.getPageViewStats(agent.getAgentCode(), "today");
                Integer todayViewCount = extractViewCount(todayStats);
                teamVO.setTodayViewCount(todayViewCount);

                // 本月浏览量
                Object monthStats = pageViewService.getPageViewStats(agent.getAgentCode(), "month");
                Integer monthlyViewCount = extractViewCount(monthStats);
                teamVO.setMonthlyViewCount(monthlyViewCount);

                // 总浏览量 - 使用大范围时间查询
                Object totalStats = pageViewService.getPageViewStats(agent.getAgentCode(), "all");
                Integer totalViewCount = extractViewCount(totalStats);
                teamVO.setTotalViewCount(totalViewCount);
            } catch (Exception e) {
                log.warn("查询代理商{}的浏览量统计失败: {}", agent.getAgentCode(), e.getMessage());
                // 设置默认值
                teamVO.setTodayViewCount(0);
                teamVO.setMonthlyViewCount(0);
                teamVO.setTotalViewCount(0);
            }

            resultList.add(teamVO);
        }

        return PageResultFactory.createPageResult(
            resultList, Long.valueOf(totalCount),
            queryBO.getPageSize(), queryBO.getPageNo());
    }

    /**
     * 查询代理商统计面板数据
     * 包含今日和本月的邀请、订单、激活、佣金统计
     *
     * @param type 统计类型：0-个人统计，1-团队统计
     * @param loginUser 当前登录用户
     * @return 统计面板数据
     * @throws BizException 业务异常
     */
    @Override
    public AgentDashboardStatisticsVO selectDashboardStatistics(Integer type, LoginUser loginUser) throws BizException {
        // 1. 获取当前代理商信息
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);

        // 2. 根据type决定查询参数
        String downstreamCode = null;
        String downstreamTeam = null;

        if (type == null || type == BaseConstant.ZERO_INT) {
            // 个人统计
            downstreamCode = agentAccount.getAgentCode();
        } else if (type == BaseConstant.ONE_INT) {
            // 团队统计
            downstreamTeam = agentAccount.getAgentCode();
        }

        // 3. 查询订单、激活、佣金数据（从OrderMapper）
        AgentDashboardStatisticsVO dashboardStats = orderMapper.selectDashboardStatistics(downstreamCode, downstreamTeam);

        // 4. 如果查询结果为空，创建默认对象
        if (dashboardStats == null) {
            dashboardStats = new AgentDashboardStatisticsVO();
        }

        // 5. 单独查询邀请数据（代理商注册数据）
        Integer todayInviteCount = queryInviteCount(agentAccount.getAgentCode(), type, true);
        Integer monthInviteCount = queryInviteCount(agentAccount.getAgentCode(), type, false);

        // 6. 设置邀请数据
        dashboardStats.setTodayInviteCount(todayInviteCount);
        dashboardStats.setMonthInviteCount(monthInviteCount);

        // 7. 设置统计类型和更新时间
        dashboardStats.setStatisticsType(type != null ? type : 0);
        dashboardStats.refreshUpdateTime();

        return dashboardStats;
    }

    /**
     * 查询邀请数据（代理商注册数据）
     *
     * @param agentCode 代理商编码
     * @param type 统计类型：0-个人统计，1-团队统计
     * @param isToday true-今日，false-本月
     * @return 邀请数量
     */
    private Integer queryInviteCount(String agentCode, Integer type, boolean isToday) {
        try {
            // 计算时间范围
            LocalDateTime now = LocalDateTime.now();
            Long startTime;
            Long endTime;

            if (isToday) {
                // 今日时间范围（00:00:00 - 23:59:59）
                startTime = now.with(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                endTime = now.with(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            } else {
                // 本月时间范围（1号00:00:00 - 当前时间）
                startTime = now.with(TemporalAdjusters.firstDayOfMonth())
                        .with(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                endTime = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }

            // 根据统计类型查询
            if (type == null || type == BaseConstant.ZERO_INT) {
                // 个人统计：查询直接邀请的代理商数量
                return agentAccountMapper.countDirectInvitesByTimeRange(agentCode, startTime, endTime);
            } else {
                // 团队统计：查询团队内所有邀请数量
                return agentAccountMapper.countTeamInvitesByTimeRange(agentCode, startTime, endTime);
            }
        } catch (Exception e) {
            log.error("查询邀请数据异常：agentCode={}, type={}, isToday={}", agentCode, type, isToday, e);
            return 0;
        }
    }

    /**
     * 从页面访问统计结果中提取浏览量
     * @param statsResult 统计结果对象
     * @return 浏览量数值
     */
    private Integer extractViewCount(Object statsResult) {
        if (statsResult == null) {
            return 0;
        }

        try {
            if (statsResult instanceof java.util.Map) {
                java.util.Map<String, Object> statsMap = (java.util.Map<String, Object>) statsResult;
                Object viewCount = statsMap.get("viewCount");
                if (viewCount != null) {
                    if (viewCount instanceof Integer) {
                        return (Integer) viewCount;
                    } else if (viewCount instanceof Number) {
                        return ((Number) viewCount).intValue();
                    } else {
                        return Integer.parseInt(viewCount.toString());
                    }
                }

                // 尝试其他可能的字段名
                Object totalViews = statsMap.get("totalViews");
                if (totalViews != null) {
                    if (totalViews instanceof Integer) {
                        return (Integer) totalViews;
                    } else if (totalViews instanceof Number) {
                        return ((Number) totalViews).intValue();
                    } else {
                        return Integer.parseInt(totalViews.toString());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("解析浏览量统计数据失败: {}", e.getMessage());
        }

        return 0;
    }

}
