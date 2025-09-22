package com.ruoyi.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.order.entity.WithdrawalRecordDetails;
import com.ruoyi.common.order.vo.AgentActivateOrderAPPStatisticsVO;
import com.ruoyi.common.order.vo.AgentWithdrawalAPPStatisticsVO;
import com.ruoyi.common.order.vo.RevenueVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2025/1/22 17:11
 */
@Mapper
public interface WithdrawalRecordDetailsMapper extends BaseMapper<WithdrawalRecordDetails> {


    /**
     * 查询余额总数
     * @return
     */
    Integer selectWithdrawalAmountCount(@Param(value = "withdrawalRecordId") Integer withdrawalRecordId);


    /**
     * 查询代理商 提现数据
     * @return
     */
    AgentWithdrawalAPPStatisticsVO selectWithdrawalAPPStatistics(@Param(value = "agentCode") String agentCode);


    /**
     * 收益记录
     * @return
     */
    RevenueVO selectRevenue(@Param(value = "userId") Long userId);

}
