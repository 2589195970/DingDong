package com.ruoyi.console.service.impl.agent;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentCommissionSelectBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.OrderCommissionDetails;
import com.ruoyi.common.order.vo.AgentCommissionSelectVO;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.vip.entity.VipConfig;
import com.ruoyi.common.vip.vo.VipConfigVO;
import com.ruoyi.console.mapper.OrderCommissionDetailsMapper;
import com.ruoyi.console.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
    VipConfigService vipConfigService;

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


    @Override
    public List<VipConfigVO> selectVipCommissionCards(LoginUser loginUser){
        List<VipConfig> configs = vipConfigService.selectEnabledVipConfigs();
        if (CollectionUtils.isEmpty(configs)) {
            return new ArrayList<>();
        }
        return configs.stream().map(this::convertToVipConfigVO).collect(Collectors.toList());
    }

    /**
     * 代理商订单佣金列表导出
     * @param response HTTP响应
     * @param agentCommissionSelectBO 查询条件
     * @param loginUser 登录用户
     * @throws BizException
     */
    @Override
    public void exportOrderCommissionList(HttpServletResponse response, AgentCommissionSelectBO agentCommissionSelectBO, LoginUser loginUser) throws BizException {
        try {
            // 获取代理账户信息
            AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
            
            // 查询所有符合条件的数据（不分页）
            List<AgentCommissionSelectVO> agentCommissionSelectVOS = baseMapper.selectAgentCommissionList(
                agentAccount.getAgentCode(), 
                agentCommissionSelectBO.getOrderId(), 
                agentCommissionSelectBO.getCardName(),
                agentCommissionSelectBO.getCardPhone(), 
                agentCommissionSelectBO.getCardId(),
                agentCommissionSelectBO.getOrderStatus(),
                agentCommissionSelectBO.getIsRecharged(),
                agentCommissionSelectBO.getStarTime(),
                agentCommissionSelectBO.getEndTime(),
                agentCommissionSelectBO.getOrderCommissionStatus(),
                0, // offset = 0
                Integer.MAX_VALUE // 获取所有数据
            );
            
            if (CollectionUtils.isEmpty(agentCommissionSelectVOS)) {
                throw new BizException("没有符合条件的数据可导出");
            }
            
            // 使用 ExcelUtil 导出数据
            ExcelUtil<AgentCommissionSelectVO> util = new ExcelUtil<>(AgentCommissionSelectVO.class);
            util.exportExcel(response, agentCommissionSelectVOS, "代理商佣金数据");
            
        } catch (Exception e) {
            log.error("导出代理商佣金数据异常: {}", e.getMessage(), e);
            throw new BizException("导出代理商佣金数据失败: " + e.getMessage());
        }
    }

    private VipConfigVO convertToVipConfigVO(VipConfig config) {
        VipConfigVO vo = new VipConfigVO();
        BeanUtils.copyProperties(config, vo);
        return vo;
    }
}
