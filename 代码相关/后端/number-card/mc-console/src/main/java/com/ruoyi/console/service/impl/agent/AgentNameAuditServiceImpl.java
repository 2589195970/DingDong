package com.ruoyi.console.service.impl.agent;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.NameAuditSelectBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.NameAudit;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.NameAuditMapper;
import com.ruoyi.console.service.AgentAccountService;
import com.ruoyi.console.service.AgentNameAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class AgentNameAuditServiceImpl extends ServiceImpl<NameAuditMapper, NameAudit> implements AgentNameAuditService {

    @Resource
    AgentAccountService agentAccountService;


    /**
     * 代理商实名认证列表查询
     *
     * @return
     * @throws BizException
     */
    public PageResult<NameAudit> selectNameAuditListPage(NameAuditSelectBO nameAuditSelectBO) throws BizException {
        //读取分页
        Page page = new Page(nameAuditSelectBO.getPageNo(), nameAuditSelectBO.getPageSize());
        Page<NameAudit> nameAuditPage = baseMapper.selectPage(page, new LambdaQueryWrapper<NameAudit>()
                .like(StringUtils.isNotEmpty(nameAuditSelectBO.getAgentName()), NameAudit::getAgentName, nameAuditSelectBO.getAgentName())
                .eq(nameAuditSelectBO.getStatus() != null, NameAudit::getStatus, nameAuditSelectBO.getStatus())
                .orderByDesc(NameAudit::getCreateTime)
        );
        return PageResultFactory.createPageResult(nameAuditPage);
    }


    /**
     * 新增实名审核记录
     *
     * @return
     * @throws BizException
     */
    @Override
    public void addNameAudit(NameAudit nameAudit) throws BizException {
        AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(nameAudit.getAgentCode(), true);
        List<NameAudit> nameAuditList = baseMapper.selectList(new LambdaQueryWrapper<NameAudit>().eq(NameAudit::getAgentCode, nameAudit.getAgentCode()));
        if (!CollectionUtil.isEmpty(nameAuditList)) {
            throw new BizException("已存在实名审核记录");
        }
        if(StringUtils.isEmpty(nameAudit.getCardIdUrlFront())||StringUtils.isEmpty(nameAudit.getCardIdUrlBack())){
            throw new BizException("身份证正反面不能为空");
        }
        nameAudit.setAgentCode(agentAccount.getAgentCode());
        nameAudit.setAgentName(agentAccount.getAgentName());
        nameAudit.setSysUserId(agentAccount.getSysUserId());
        nameAudit.setStatus(BaseConstant.ZERO_INT);
        nameAudit.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(nameAudit);
        //更新代理商实名状态为认证中
        agentAccountService.updateAgentRealNameStatus(agentAccount.getAgentCode(),BaseConstant.TWO_INT,null,null);
    }


    /**
     * 查询登陆用户实名审核
     *
     * @return
     * @throws BizException
     */
    @Override
    public NameAudit selectNameAudit(LoginUser loginUser) throws BizException {
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        return baseMapper.selectOne(new LambdaQueryWrapper<NameAudit>().eq(NameAudit::getAgentCode,agentAccount.getAgentCode()));
    }

    /**
     * 更新实名审核记录
     *
     * @return
     * @throws BizException
     */
    @Override
    public void updateNameAudit(NameAudit nameAudit) throws BizException {
        NameAudit nameAuditOld = baseMapper.selectById(nameAudit.getNameAuditId());
        if (nameAuditOld == null) {
            throw new BizException("实名审核记录ID:{}不存在",nameAudit.getNameAuditId());
        }
        nameAuditOld.setCardId(nameAudit.getCardId());
        nameAuditOld.setCardName(nameAudit.getCardName());
        nameAuditOld.setCardIdUrlBack(nameAudit.getCardIdUrlBack());
        nameAuditOld.setCardIdUrlFront(nameAudit.getCardIdUrlFront());
        nameAuditOld.setStatus(BaseConstant.ZERO_INT);
        nameAuditOld.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(nameAuditOld);
        //更新代理商实名状态为认证中
        agentAccountService.updateAgentRealNameStatus(nameAuditOld.getAgentCode(),BaseConstant.TWO_INT,null,null);
    }

    /**
     * 更新实名审核状态
     *
     * @return
     * @throws BizException
     */
    @Override
    public void updateNameAuditStatus(NameAudit nameAudit) throws BizException {
        NameAudit nameAuditOld = baseMapper.selectById(nameAudit.getNameAuditId());
        if (nameAuditOld == null) {
            throw new BizException("实名审核记录ID:{}不存在",nameAudit.getNameAuditId());
        }
        nameAuditOld.setRemark(nameAudit.getRemark());
        nameAuditOld.setStatus(nameAudit.getStatus());
        nameAuditOld.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(nameAuditOld);
        //变更为失败状态
        if(nameAuditOld.getStatus() ==BaseConstant.ONE_INT){
            agentAccountService.updateAgentRealNameStatus(nameAuditOld.getAgentCode(),BaseConstant.THREE_INT,null,null);
        }
        //变更为失败状态
        if(nameAuditOld.getStatus() ==BaseConstant.TWO_INT){
            agentAccountService.updateAgentRealNameStatus(nameAuditOld.getAgentCode(),BaseConstant.ONE_INT,nameAudit.getCardIdUrlFront(),nameAudit.getCardIdUrlBack());
        }
    }


    /**
     * 删除实名审核记录
     * @throws BizException
     */
    public void deleteNameAudit(Integer nameAuditId) throws BizException {
        baseMapper.deleteById(nameAuditId);
    }

}
