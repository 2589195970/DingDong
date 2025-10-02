package com.ruoyi.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.vo.AgentAccountListVO;
import com.ruoyi.common.order.vo.AgentProductVO;
import com.ruoyi.common.order.vo.AgentRegistrationStatisticsVO;
import com.ruoyi.common.order.vo.AgentRankingVO;
import com.ruoyi.common.order.bo.AgentTeamQueryBO;
import com.ruoyi.common.order.bo.AgentRankingQueryBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
@Mapper
public interface AgentAccountMapper extends BaseMapper<AgentAccount> {

    /**
     * 查询代理商列表总数
     * @return
     */
    Integer selectAgentAccountListCount(@Param(value = "parentAgentName") String parentAgentName, @Param(value = "agentName") String agentName,
                                        @Param(value = "isRealName") Integer isRealName,@Param(value = "isEnabled") Integer isEnabled,
                                        @Param(value = "level") Integer level);



    /**
     * 查询代理商列表
     * @return
     */
    List<AgentAccountListVO> selectAgentAccountList(@Param(value = "parentAgentName") String parentAgentName, @Param(value = "agentName") String agentName,
                                                    @Param(value = "isRealName") Integer isRealName, @Param(value = "isEnabled") Integer isEnabled,
                                                    @Param(value = "level") Integer level,
                                                    @Param(value = "offset") Integer offset, @Param(value = "pageSize") Integer pageSize);

    /**
     * 查询直接下级代理商总数
     * @param queryBO 查询参数
     * @return 总数
     */
    Integer selectDirectSubAgentsCount(@Param("queryBO") AgentTeamQueryBO queryBO);

    /**
     * 查询直接下级代理商列表
     * @param queryBO 查询参数
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 代理商列表
     */
    List<AgentAccountListVO> selectDirectSubAgentsList(@Param("queryBO") AgentTeamQueryBO queryBO,
                                                       @Param("offset") Integer offset,
                                                       @Param("pageSize") Integer pageSize);

    /**
     * 统计某个代理商的团队成员数（直接下级数量）
     * @param parentAgentCode 父代理商编码
     * @return 团队成员数
     */
    Integer countAgentTeamMembers(@Param("parentAgentCode") String parentAgentCode);

    /**
     * 查询代理商注册统计（按时间维度）
     * @param todayStart 今日开始时间戳
     * @param todayEnd 今日结束时间戳
     * @param yesterdayStart 昨日开始时间戳
     * @param yesterdayEnd 昨日结束时间戳
     * @param thisWeekStart 本周开始时间戳
     * @param thisMonthStart 本月开始时间戳
     * @param parentAgentCode 父代理商编码（可选，为空则查询所有）
     * @return 注册统计数据
     */
    AgentRegistrationStatisticsVO selectAgentRegistrationStatistics(
            @Param("todayStart") Long todayStart,
            @Param("todayEnd") Long todayEnd,
            @Param("yesterdayStart") Long yesterdayStart,
            @Param("yesterdayEnd") Long yesterdayEnd,
            @Param("thisWeekStart") Long thisWeekStart,
            @Param("thisMonthStart") Long thisMonthStart,
            @Param("parentAgentCode") String parentAgentCode
    );

    /**
     * 查询直接邀请的代理商数量（按时间范围）
     * @param agentCode 代理商编码
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return 邀请数量
     */
    Integer countDirectInvitesByTimeRange(
            @Param("agentCode") String agentCode,
            @Param("startTime") Long startTime,
            @Param("endTime") Long endTime
    );

    /**
     * 查询团队内所有邀请数量（按时间范围）
     * @param agentCode 代理商编码
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return 邀请数量
     */
    Integer countTeamInvitesByTimeRange(
            @Param("agentCode") String agentCode,
            @Param("startTime") Long startTime,
            @Param("endTime") Long endTime
    );

    /**
     * 查询代理商佣金排行榜
     * @param queryBO 查询参数
     * @return 排行榜列表
     */
    List<AgentRankingVO> selectAgentCommissionRanking(@Param("queryBO") AgentRankingQueryBO queryBO);

    /**
     * 查询代理商订单量排行榜
     * @param queryBO 查询参数
     * @return 排行榜列表
     */
    List<AgentRankingVO> selectAgentOrderRanking(@Param("queryBO") AgentRankingQueryBO queryBO);

    /**
     * 查询代理商激活量排行榜
     * @param queryBO 查询参数
     * @return 排行榜列表
     */
    List<AgentRankingVO> selectAgentActivateRanking(@Param("queryBO") AgentRankingQueryBO queryBO);

    /**
     * 查询代理商团队发展排行榜
     * @param queryBO 查询参数
     * @return 排行榜列表
     */
    List<AgentRankingVO> selectAgentTeamRanking(@Param("queryBO") AgentRankingQueryBO queryBO);

    /**
     * 查询代理商排行总数（通用方法）
     * @param agentName 代理商姓名（模糊查询）
     * @param rankingType 排行类型（1-佣金 2-订单量 3-激活量 4-团队发展）
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return 总数
     */
    Integer selectAgentRankingCount(@Param("agentName") String agentName,
                                   @Param("rankingType") Integer rankingType,
                                   @Param("startTime") Long startTime,
                                   @Param("endTime") Long endTime);

    /**
     * 查询代理商排行列表（通用方法）
     * @param agentName 代理商姓名（模糊查询）
     * @param rankingType 排行类型（1-佣金 2-订单量 3-激活量 4-团队发展）
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 排行榜列表
     */
    List<AgentRankingVO> selectAgentRankingList(@Param("agentName") String agentName,
                                               @Param("rankingType") Integer rankingType,
                                               @Param("startTime") Long startTime,
                                               @Param("endTime") Long endTime,
                                               @Param("offset") Integer offset,
                                               @Param("pageSize") Integer pageSize);

    // ==================== 直接下游代理商查询方法 ====================
    
    /**
     * 查询直接下游代理商列表总数（包含团队人数）
     * @param parentAgentCode 父代理商编码
     * @param agentName 代理商姓名（模糊查询）
     * @param isRealName 是否实名
     * @param isEnabled 是否启用
     * @param level 等级
     * @return 总数
     */
    Integer selectDirectSubAgentsCountWithTeam(@Param("parentAgentCode") String parentAgentCode,
                                               @Param("agentName") String agentName,
                                               @Param("isRealName") Integer isRealName,
                                               @Param("isEnabled") Integer isEnabled,
                                               @Param("level") Integer level);

    /**
     * 查询直接下游代理商列表（包含团队人数）
     * @param parentAgentCode 父代理商编码
     * @param agentName 代理商姓名（模糊查询）
     * @param isRealName 是否实名
     * @param isEnabled 是否启用
     * @param level 等级
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 代理商列表
     */
    List<AgentAccountListVO> selectDirectSubAgentsListWithTeam(@Param("parentAgentCode") String parentAgentCode,
                                                               @Param("agentName") String agentName,
                                                               @Param("isRealName") Integer isRealName,
                                                               @Param("isEnabled") Integer isEnabled,
                                                               @Param("level") Integer level,
                                                               @Param("offset") Integer offset,
                                                               @Param("pageSize") Integer pageSize);

    /**
     * 查询直接下游代理商列表（包含团队人数）- 简化版本
     * @param parentAgentCode 父代理商编码
     * @param agentName 代理商姓名（模糊查询）
     * @param isRealName 是否实名
     * @param isEnabled 是否启用
     * @param level 等级
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 代理商列表
     */
    List<AgentAccountListVO> selectDirectSubAgentsListWithTeamSimple(@Param("parentAgentCode") String parentAgentCode,
                                                                     @Param("agentName") String agentName,
                                                                     @Param("isRealName") Integer isRealName,
                                                                     @Param("isEnabled") Integer isEnabled,
                                                                     @Param("level") Integer level,
                                                                     @Param("offset") Integer offset,
                                                                     @Param("pageSize") Integer pageSize);

}
