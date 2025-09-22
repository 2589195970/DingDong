package com.ruoyi.order.service.impl.order;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.order.entity.AgentCommissionJson;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.OrderCommission;
import com.ruoyi.common.order.entity.OrderCommissionDetails;
import com.ruoyi.order.mapper.OrderCommissionDetailsMapper;
import com.ruoyi.order.mapper.OrderCommissionMapper;
import com.ruoyi.order.service.order.OrderCommissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:13
 */
@Service
@Slf4j
public class OrderCommissionServiceImpl extends ServiceImpl<OrderCommissionMapper, OrderCommission> implements OrderCommissionService {

    @Resource
    OrderCommissionDetailsMapper orderCommissionDetailsMapper;

    /**
     * 生成订单佣金结算记录
     *
     */
    public void saveOrderCommission(Order order){
        log.info("订单佣金结算生产记录开始：{}",order.getOrderId());
        OrderCommission orderCommission = new OrderCommission();
        BeanUtil.copyProperties(order,orderCommission);
        orderCommission.setOrderCommissionStatus(OrderEnum.OrderCommissionEnum.TYPE_1.getCommissionType());
        orderCommission.setDownstreamAgentCode(order.getDownstreamCode());
        orderCommission.setDownstreamAgentName(order.getDownstreamName());
        orderCommission.setOrderCreateTime(order.getCreateTime());
        orderCommission.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(orderCommission);
        List<AgentCommissionJson> agentCommissionJsons = JSONObject.parseArray(order.getDownstreamFatherList(),AgentCommissionJson.class);
        if(!CollectionUtils.isEmpty(agentCommissionJsons)){
            orderCommission.setProductCommission(agentCommissionJsons.get(BaseConstant.ZERO_INT).getProductCommission());
            baseMapper.updateById(orderCommission);
        }
        //添加佣金详情
        List<OrderCommissionDetails> orderCommissionDetailsList = new ArrayList<>();
        for (int i = 0;i<agentCommissionJsons.size();i++){
            OrderCommissionDetails orderCommissionDetails = new OrderCommissionDetails();
            orderCommissionDetails.setOrderId(order.getOrderId());
            orderCommissionDetails.setOrderCommissionId(orderCommission.getOrderCommissionId());
            orderCommissionDetails.setSysUserId(agentCommissionJsons.get(i).getSysUserId());
            orderCommissionDetails.setAgentCode(agentCommissionJsons.get(i).getAgentCode());
            orderCommissionDetails.setAgentName(agentCommissionJsons.get(i).getAgentName());
            orderCommissionDetails.setProductCommission(agentCommissionJsons.get(i).getProductCommission());
            orderCommissionDetails.setDistributionProductCommission(agentCommissionJsons.get(i).getDistributionProductCommission());
            orderCommissionDetails.setRevenueProductCommission(agentCommissionJsons.get(i).getRevenueProductCommission());
            orderCommissionDetails.setCreateTime(System.currentTimeMillis());
            //如果是进单代理 无需分佣金到下级
            int j =((agentCommissionJsons.size()-1)==i)?i: i+1;
            orderCommissionDetails.setAgentSourceCode(agentCommissionJsons.get(j).getAgentCode());
            orderCommissionDetails.setAgentSourceName(agentCommissionJsons.get(j).getAgentName());
            orderCommissionDetailsList.add(orderCommissionDetails);
        }
        orderCommissionDetailsMapper.insert(orderCommissionDetailsList);
        log.info("订单佣金结算生产记录结束,订单佣金记录ID：{},订单ID:{}",orderCommission.getOrderCommissionId(),order.getOrderId());
    }


    /**
     * 更新订单佣金充值信息
     */
    public void updateOrderCommissionRecharged(Order order){
      List<OrderCommission> orderCommissions = this.list(new LambdaQueryWrapper<OrderCommission>().eq(OrderCommission::getOrderId,order.getOrderId()));
      //更新一下订单佣金充值信息
        for (OrderCommission orderCommission:orderCommissions){
            orderCommission.setIsRecharged(order.getIsRecharged());
            orderCommission.setRechargeAmount(order.getRechargeAmount());
            orderCommission.setRechargeTime(order.getRechargeTime());
            orderCommission.setUpdateTime(System.currentTimeMillis());
            this.updateById(orderCommission);
        }

    }

}
