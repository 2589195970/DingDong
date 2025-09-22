package com.ruoyi.console.service.impl.agent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentCommissionSelectBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.AgentProduct;
import com.ruoyi.common.order.entity.CommissionConfig;
import com.ruoyi.common.order.entity.OrderCommissionDetails;
import com.ruoyi.common.order.vo.AgentCommissionSelectVO;
import com.ruoyi.console.mapper.OrderCommissionDetailsMapper;
import com.ruoyi.console.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 代理商 佣金相关
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/03/07 17:13
 */
@Service
@Slf4j
public class AgentCommissionServiceImpl extends ServiceImpl<OrderCommissionDetailsMapper, OrderCommissionDetails> implements AgentCommissionService {

    @Resource
    AgentAccountService agentAccountService;

    @Resource
    CommissionConfigService commissionConfigService;

    @Resource
    AgentProductInitService agentProductInitService;

    @Resource
    AgentProductService agentProductService;

    /**
     * 代理商订单佣金列表查询
     *
     * @return
     * @throws BizException
     */
    public PageResult<AgentCommissionSelectVO> selectOrderCommissionListPage(AgentCommissionSelectBO agentCommissionSelectBO, LoginUser loginUser) throws BizException {
        //获取代理账户信息
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        //分页获取代理商佣金 查询总数
        Integer totalRows = baseMapper.selectAgentCommissionListCount(agentAccount.getAgentCode(), agentCommissionSelectBO.getOrderId(), agentCommissionSelectBO.getCardName(), agentCommissionSelectBO.getCardPhone(), agentCommissionSelectBO.getCardId(),
                agentCommissionSelectBO.getOrderStatus(),agentCommissionSelectBO.getIsRecharged(),agentCommissionSelectBO.getStarTime(),agentCommissionSelectBO.getEndTime(),agentCommissionSelectBO.getOrderCommissionStatus());
        if (totalRows == null || totalRows == BaseConstant.ZERO_INT) {
            return PageResultFactory.createPageResult(new ArrayList<>(), 0L, agentCommissionSelectBO.getPageSize(), agentCommissionSelectBO.getPageNo());
        }
        //分页查询代理商佣金列表
        List<AgentCommissionSelectVO> agentCommissionSelectVOS = baseMapper.selectAgentCommissionList(agentAccount.getAgentCode(), agentCommissionSelectBO.getOrderId(), agentCommissionSelectBO.getCardName(),
                agentCommissionSelectBO.getCardPhone(), agentCommissionSelectBO.getCardId(),agentCommissionSelectBO.getOrderStatus(),agentCommissionSelectBO.getIsRecharged(),agentCommissionSelectBO.getStarTime(),
                agentCommissionSelectBO.getEndTime(),agentCommissionSelectBO.getOrderCommissionStatus(),(agentCommissionSelectBO.getPageNo() - 1) * agentCommissionSelectBO.getPageSize(), agentCommissionSelectBO.getPageSize());
        return PageResultFactory.createPageResult(agentCommissionSelectVOS, Long.valueOf(totalRows), agentCommissionSelectBO.getPageSize(), agentCommissionSelectBO.getPageNo());
    }


    /**
     * 代理商佣金配置查询
     * @param loginUser
     */
    public CommissionConfig selectAgentCommissionConfig(LoginUser loginUser) throws BizException {
        CommissionConfig commissionConfig = commissionConfigService.getOne(new LambdaQueryWrapper<CommissionConfig>().eq(CommissionConfig::getSysUserId,loginUser.getUserId()));
        return commissionConfig;
    }

    /**
     * 代理商佣金配置修改
     * @param loginUser
     */
    public void agentUpdateCommissionConfig(CommissionConfig commissionConfig,LoginUser loginUser) throws BizException {
        CommissionConfig commissionConfigOld = commissionConfigService.getOne(new LambdaQueryWrapper<CommissionConfig>().eq(CommissionConfig::getSysUserId,loginUser.getUserId()));
        if(commissionConfigOld == null){
            throw new BizException("当前账号不存在佣金配置");
        }
        commissionConfigOld.setCommissionConfigType(commissionConfig.getCommissionConfigType());
        commissionConfigOld.setFixedCommission(commissionConfig.getFixedCommission());
        commissionConfigOld.setScaleCommission(commissionConfig.getScaleCommission());
        commissionConfigOld.setRemark(commissionConfig.getRemark());
        commissionConfigService.updateCommissionConfig(commissionConfigOld);
        //佣金配置修改后 触发一次 佣金重新计算
        List<AgentProduct> agentProductList = agentProductService.list(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getAgentCode,commissionConfigOld.getAgentCode()));
        if(!CollectionUtils.isEmpty(agentProductList)){
            for(AgentProduct agentProduct:agentProductList){
                agentProductInitService.updateAgentProductCommission(agentProduct.getParentProductCode(),agentProduct.getAgentCode(),agentProduct.getProductCommission());
            }
        }
    }
}
