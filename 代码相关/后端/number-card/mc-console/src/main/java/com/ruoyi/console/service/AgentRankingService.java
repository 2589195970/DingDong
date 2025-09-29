package com.ruoyi.console.service;

import com.ruoyi.common.order.bo.AgentRankingQueryBO;
import com.ruoyi.common.order.vo.AgentRankingVO;

import java.util.List;

/**
 * 代理商排行榜服务接口
 *
 * @author ruoyi
 */
public interface AgentRankingService {

    /**
     * 获取代理商排行榜
     *
     * @param queryBO 查询参数
     * @return 排行榜列表
     */
    List<AgentRankingVO> getAgentRanking(AgentRankingQueryBO queryBO);

    /**
     * 获取代理商日榜
     *
     * @param rankingType 排行类型：1-佣金排行 2-订单量排行 3-激活量排行 4-团队发展排行
     * @param topCount 查询TOP数量
     * @param parentAgentCode 父代理商编码（可选）
     * @return 排行榜列表
     */
    List<AgentRankingVO> getDailyRanking(Integer rankingType, Integer topCount, String parentAgentCode);

    /**
     * 获取代理商月榜
     *
     * @param rankingType 排行类型：1-佣金排行 2-订单量排行 3-激活量排行 4-团队发展排行
     * @param topCount 查询TOP数量
     * @param parentAgentCode 父代理商编码（可选）
     * @return 排行榜列表
     */
    List<AgentRankingVO> getMonthlyRanking(Integer rankingType, Integer topCount, String parentAgentCode);
}