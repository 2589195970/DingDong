package com.ruoyi.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.vo.AgentActivateOrderAPPStatisticsVO;
import com.ruoyi.common.order.vo.AgentDashboardStatisticsVO;
import com.ruoyi.common.order.vo.AgentOrderAPPStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/8/10 17:11
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {


    /**
     * 查询代理商订单统计
     * 昨日订单 今日订单 全部订单
     * @return
     */
    AgentOrderAPPStatisticsVO selectAgentOrderAPPStatistics(@Param(value = "downstreamCode") String downstreamCode, @Param(value = "downstreamTeam") String downstreamTeam);


    /**
     * 查询代理商 激活 订单统计
     * 昨日订单 今日订单 全部订单
     * @return
     */
    AgentActivateOrderAPPStatisticsVO selectActivateAgentOrderAPPStatistics(@Param(value = "downstreamCode") String downstreamCode, @Param(value = "downstreamTeam") String downstreamTeam);

    /**
     * 查询代理商统计面板数据
     * 包含今日和本月的订单、激活、佣金统计
     * @param downstreamCode 代理商编码（个人统计）
     * @param downstreamTeam 代理商编码（团队统计）
     * @return 统计面板数据
     */
    AgentDashboardStatisticsVO selectDashboardStatistics(@Param(value = "downstreamCode") String downstreamCode, @Param(value = "downstreamTeam") String downstreamTeam);

}
