package com.ruoyi.console.service.impl.agent;

import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentAccountSelectBO;
import com.ruoyi.common.order.bo.AgentRankingSelectBO;
import com.ruoyi.common.order.bo.AgentUpdateBalanceBO;
import com.ruoyi.common.order.bo.AgentUpdateEnabledBO;
import com.ruoyi.common.order.bo.AgentUpdateEncryptBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.AgentProduct;
import com.ruoyi.common.order.vo.AgentAccountListVO;
import com.ruoyi.common.order.vo.AgentRankingVO;
import com.ruoyi.console.mapper.AgentAccountMapper;
import com.ruoyi.console.mapper.ProductMapper;
import com.ruoyi.console.service.*;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


/**
 * 代理商(admin) 管理相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/03/07 17:13
 */
@Service
@Slf4j
public class AgentManagementServiceImpl  implements AgentManagementService {

    @Resource
    AgentAccountMapper agentAccountMapper;

    @Resource
    AgentProductService agentProductService;

    @Resource
    WithdrawalRecordService withdrawalRecordService;

    @Resource
    ISysUserService iSysUserService;



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
     * 更新代理生效状态
     * @param agentUpdateEnabledBO
     * @throws BizException
     */
    public void updateAgentStatus(AgentUpdateEnabledBO agentUpdateEnabledBO) throws BizException {
        AgentAccount agentAccount = new AgentAccount();
        agentAccount.setAgentAccountId(agentUpdateEnabledBO.getAgentAccountId());
        agentAccount.setIsEnabled(agentUpdateEnabledBO.getIsEnabled());
        agentAccount.setUpdateTime(System.currentTimeMillis());
        agentAccountMapper.updateById(agentAccount);
    }


    /**
     * 更新代理加密状态状态
     * @throws BizException
     */
    public void updateAgentEncryptStatus(AgentUpdateEncryptBO agentUpdateEncryptBO) throws BizException {
        AgentAccount agentAccount = new AgentAccount();
        agentAccount.setAgentAccountId(agentUpdateEncryptBO.getAgentAccountId());
        agentAccount.setIsEncrypt(agentUpdateEncryptBO.getIsEncrypt());
        agentAccount.setUpdateTime(System.currentTimeMillis());
        agentAccountMapper.updateById(agentAccount);
    }



    /**
     * 调整余额
     * @param agentUpdateEnabledBO
     * @throws BizException
     */
    public void updateBalance(AgentUpdateBalanceBO agentUpdateEnabledBO) throws BizException {
        AgentAccount agentAccount = agentAccountMapper.selectById(agentUpdateEnabledBO.getAgentAccountId());
        if(agentAccount == null){
            throw new BizException("代理商ID不存在");
        }
        withdrawalRecordService.insertAdminBalanceWithdrawal(agentAccount.getAgentCode(),agentUpdateEnabledBO.getBalanceYun(),agentUpdateEnabledBO.getType(),agentAccount);
    }


    /**
     * 删除代理商
     * @throws BizException
     */
    public void deleteAgentAccount(Integer agentAccountId) throws BizException {
        AgentAccount agentAccount = agentAccountMapper.selectById(agentAccountId);
        if(agentAccount == null){
            throw new BizException("用户不存在");
        }
        agentAccountMapper.deleteById(agentAccount.getAgentAccountId());
        //删除user
        iSysUserService.deleteUserById(agentAccount.getSysUserId());
        //删除相关代理商产品
        //agentProductService.deleteSubAgentProduct();
    }

    /**
     * 代理商排名查询
     * @param agentRankingSelectBO 查询条件
     * @return 代理商排名分页结果
     * @throws BizException
     * 
     * 排序规则：按订单总数降序排列，相同订单数时按创建时间升序排列
     * 佣金数据：从t_order_commission表获取真实佣金数据，只统计已激活且已结算的订单佣金
     */
    public PageResult<AgentRankingVO> selectAgentRankingPage(AgentRankingSelectBO agentRankingSelectBO) throws BizException {
        try {
            // 根据排名类型计算时间范围
            Long startTime = null;
            Long endTime = null;
            
            if (agentRankingSelectBO.getRankingType() != null && agentRankingSelectBO.getRankingType() != 0) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime start;
                LocalDateTime end;
                
                switch (agentRankingSelectBO.getRankingType()) {
                    case 1: // 本月排名
                        start = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                        end = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999_000_000);
                        break;
                    case 2: // 昨日排名
                        LocalDate yesterday = now.toLocalDate().minusDays(1);
                        start = yesterday.atStartOfDay();
                        end = yesterday.atTime(23, 59, 59, 999_000_000);
                        break;
                    case 3: // 今日排名
                        start = now.toLocalDate().atStartOfDay();
                        end = now.toLocalDate().atTime(23, 59, 59, 999_000_000);
                        break;
                    default:
                        start = null;
                        end = null;
                        break;
                }
                
                if (start != null && end != null) {
                    startTime = start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    endTime = end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                }
            }
            
            // 查询总数
            Integer totalRows = agentAccountMapper.selectAgentRankingCount(
                agentRankingSelectBO.getAgentName(),
                agentRankingSelectBO.getRankingType(),
                startTime,
                endTime
            );
            
            if (totalRows == null || totalRows == BaseConstant.ZERO_INT) {
                return PageResultFactory.createPageResult(new ArrayList<>(), 0L, 
                    agentRankingSelectBO.getPageSize(), agentRankingSelectBO.getPageNo());
            }
            
            // 分页查询代理商排名
            List<AgentRankingVO> agentRankingList = agentAccountMapper.selectAgentRankingList(
                agentRankingSelectBO.getAgentName(),
                agentRankingSelectBO.getRankingType(),
                startTime,
                endTime,
                (agentRankingSelectBO.getPageNo() - 1) * agentRankingSelectBO.getPageSize(),
                agentRankingSelectBO.getPageSize()
            );
            
            return PageResultFactory.createPageResult(agentRankingList, Long.valueOf(totalRows), 
                agentRankingSelectBO.getPageSize(), agentRankingSelectBO.getPageNo());
                
        } catch (Exception e) {
            log.error("查询代理商排名异常: {}", e.getMessage(), e);
            throw new BizException("查询代理商排名失败: " + e.getMessage());
        }
    }

}
