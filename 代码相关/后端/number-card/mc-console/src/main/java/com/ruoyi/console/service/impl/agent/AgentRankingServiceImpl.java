package com.ruoyi.console.service.impl.agent;

import com.ruoyi.common.order.bo.AgentRankingQueryBO;
import com.ruoyi.common.order.vo.AgentRankingVO;
import com.ruoyi.console.mapper.AgentAccountMapper;
import com.ruoyi.console.service.AgentRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

/**
 * 代理商排行榜服务实现类
 *
 * @author ruoyi
 */
@Service
public class AgentRankingServiceImpl implements AgentRankingService {

    @Autowired
    private AgentAccountMapper agentAccountMapper;

    @Override
    public List<AgentRankingVO> getAgentRanking(AgentRankingQueryBO queryBO) {
        // 设置时间范围
        setupTimeRange(queryBO);

        // 根据排行类型调用不同的查询方法
        List<AgentRankingVO> rankings;
        switch (queryBO.getRankingType()) {
            case 1: // 佣金排行
                rankings = agentAccountMapper.selectAgentCommissionRanking(queryBO);
                setStatDesc(rankings, "佣金(分)");
                break;
            case 2: // 订单量排行
                rankings = agentAccountMapper.selectAgentOrderRanking(queryBO);
                setStatDesc(rankings, "订单数");
                break;
            case 3: // 激活量排行
                rankings = agentAccountMapper.selectAgentActivateRanking(queryBO);
                setStatDesc(rankings, "激活数");
                break;
            case 4: // 团队发展排行
                rankings = agentAccountMapper.selectAgentTeamRanking(queryBO);
                setStatDesc(rankings, "邀请数");
                break;
            default:
                throw new IllegalArgumentException("不支持的排行类型: " + queryBO.getRankingType());
        }

        // 设置统计时间
        setStatTime(rankings);

        return rankings;
    }

    @Override
    @Cacheable(value = "agentDailyRanking", key = "#rankingType + '_' + #topCount + '_' + (#parentAgentCode != null ? #parentAgentCode : 'all')")
    public List<AgentRankingVO> getDailyRanking(Integer rankingType, Integer topCount, String parentAgentCode) {
        AgentRankingQueryBO queryBO = new AgentRankingQueryBO(rankingType, 1);
        queryBO.setTopCount(topCount);
        queryBO.setParentAgentCode(parentAgentCode);
        return getAgentRanking(queryBO);
    }

    @Override
    @Cacheable(value = "agentMonthlyRanking", key = "#rankingType + '_' + #topCount + '_' + (#parentAgentCode != null ? #parentAgentCode : 'all')")
    public List<AgentRankingVO> getMonthlyRanking(Integer rankingType, Integer topCount, String parentAgentCode) {
        AgentRankingQueryBO queryBO = new AgentRankingQueryBO(rankingType, 2);
        queryBO.setTopCount(topCount);
        queryBO.setParentAgentCode(parentAgentCode);
        return getAgentRanking(queryBO);
    }

    /**
     * 设置时间范围
     */
    private void setupTimeRange(AgentRankingQueryBO queryBO) {
        LocalDate now = LocalDate.now();

        if (queryBO.getTimeDimension() == 1) { // 日榜
            LocalDate today = now;
            queryBO.setStartTime(today.atStartOfDay().toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
            queryBO.setEndTime(today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.ofHours(8)).toEpochMilli() - 1);
        } else if (queryBO.getTimeDimension() == 2) { // 月榜
            LocalDate firstDayOfMonth = now.withDayOfMonth(1);
            queryBO.setStartTime(firstDayOfMonth.atStartOfDay().toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
            queryBO.setEndTime(now.plusDays(1).atStartOfDay().toInstant(ZoneOffset.ofHours(8)).toEpochMilli() - 1);
        }

        // 如果已经设置了自定义时间范围，则使用自定义时间
        // 这样可以支持查询历史排行榜
    }

    /**
     * 设置统计描述
     */
    private void setStatDesc(List<AgentRankingVO> rankings, String desc) {
        if (rankings != null) {
            rankings.forEach(ranking -> ranking.setStatDesc(desc));
        }
    }

    /**
     * 设置统计时间
     */
    private void setStatTime(List<AgentRankingVO> rankings) {
        if (rankings != null) {
            long currentTime = System.currentTimeMillis();
            rankings.forEach(ranking -> ranking.setStatTime(currentTime));
        }
    }
}