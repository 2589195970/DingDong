package com.ruoyi.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.order.entity.OrderCommission;
import com.ruoyi.common.order.entity.OrderCommissionDetails;
import com.ruoyi.common.order.vo.AgentAccountListVO;
import com.ruoyi.common.order.vo.AgentCommissionSelectVO;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
@Mapper
public interface OrderCommissionDetailsMapper extends BaseMapper<OrderCommissionDetails> {

    /**
     * 查询代理商佣金列表总数
     * @return
     */
    Integer selectAgentCommissionListCount(@Param(value = "agentCode") String agentCode,@Param(value = "orderId") String orderId, @Param(value = "cardName") String cardName,
                                        @Param(value = "cardPhone") String cardPhone, @Param(value = "cardId") String cardId,
                                        @Param(value = "orderStatus") Integer orderStatus, @Param(value = "isRecharged") Integer isRecharged,
                                        @Param(value = "starTime") Long starTime, @Param(value = "endTime") Long endTime,
                                        @Param(value = "orderCommissionStatus") Integer orderCommissionStatus);







    /**
     * 查询代理商佣金列表
     * @return
     */
    List<AgentCommissionSelectVO> selectAgentCommissionList(@Param(value = "agentCode") String agentCode,@Param(value = "orderId") String orderId, @Param(value = "cardName") String cardName,
                                                         @Param(value = "cardPhone") String cardPhone, @Param(value = "cardId") String cardId,
                                                         @Param(value = "orderStatus") Integer orderStatus, @Param(value = "isRecharged") Integer isRecharged,
                                                         @Param(value = "starTime") Long starTime, @Param(value = "endTime") Long endTime,
                                                         @Param(value = "orderCommissionStatus") Integer orderCommissionStatus,
                                                         @Param(value = "offset") Integer offset, @Param(value = "pageSize") Integer pageSize);

}
