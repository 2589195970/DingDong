package com.ruoyi.console.service.impl.agent;

import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentAccountSelectBO;
import com.ruoyi.common.order.bo.AgentUpdateBalanceBO;
import com.ruoyi.common.order.bo.AgentUpdateEnabledBO;
import com.ruoyi.common.order.bo.AgentUpdateEncryptBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.AgentProduct;
import com.ruoyi.common.order.vo.AgentAccountListVO;
import com.ruoyi.console.mapper.AgentAccountMapper;
import com.ruoyi.console.mapper.ProductMapper;
import com.ruoyi.console.service.*;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

}
