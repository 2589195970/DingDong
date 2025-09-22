package com.ruoyi.console.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.WorkOrderAddBO;
import com.ruoyi.common.order.bo.WorkOrderSelectBO;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.vo.WorkOrderSelectVO;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.WorkOrderMapper;
import com.ruoyi.console.mapper.WorkOrderRecoverMapper;
import com.ruoyi.console.service.AgentAccountService;
import com.ruoyi.console.service.WorkOrderService;
import com.ruoyi.framework.web.domain.server.Sys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 工单记录相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements WorkOrderService {


    @Resource
    WorkOrderRecoverMapper workOrderRecoverMapper;

    @Resource
    AgentAccountService agentAccountService;


    /**
     * 工单列表查询
     * @return
     * @throws BizException
     */
    public PageResult<WorkOrderSelectVO> selectWorkOrderListPage(WorkOrderSelectBO workOrderSelectBO, LoginUser loginUser) throws BizException {
        //读取分页
        Page page = new Page(workOrderSelectBO.getPageNo(),workOrderSelectBO.getPageSize());
        if(loginUser!=null){
            AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
            workOrderSelectBO.setAgentCode(agentAccount.getAgentCode());
        }
        Page<WorkOrder> workOrderPage  = baseMapper.selectPage(page,new LambdaQueryWrapper<WorkOrder>()
                .eq(StringUtils.isNotEmpty(workOrderSelectBO.getAgentCode()),WorkOrder::getAgentCode,workOrderSelectBO.getAgentCode())
                .eq(workOrderSelectBO.getWorkOrderType()!=null,WorkOrder::getWorkOrderType,workOrderSelectBO.getWorkOrderType())
                .eq(workOrderSelectBO.getWorkOrderStatus()!=null,WorkOrder::getWorkOrderStatus,workOrderSelectBO.getWorkOrderStatus())
                .like(StringUtils.isNotEmpty(workOrderSelectBO.getWorkOrderTitle()),WorkOrder::getWorkOrderTitle,workOrderSelectBO.getWorkOrderTitle())
        );
        Page<WorkOrderSelectVO> workOrderSelectVOPage  = new Page<>();
        BeanUtil.copyProperties(workOrderPage,workOrderSelectVOPage);
        if(CollectionUtils.isEmpty(workOrderPage.getRecords())){
            List<WorkOrderSelectVO> workOrderSelectVOS = new ArrayList<>();
            //查询工单详情
            for (WorkOrder workOrder:workOrderPage.getRecords()){
                WorkOrderSelectVO workOrderSelectVO = new WorkOrderSelectVO();
                BeanUtil.copyProperties(workOrder,workOrderSelectVO);
                List<WorkOrderRecover> workOrderRecoverList = workOrderRecoverMapper.selectList(new LambdaQueryWrapper<WorkOrderRecover>().eq(WorkOrderRecover::getWorkOrderId,workOrder.getWorkOrderId()));
                workOrderSelectVO.setWorkOrderRecovers(workOrderRecoverList);
                workOrderSelectVOS.add(workOrderSelectVO);
            }
            workOrderSelectVOPage.setRecords(workOrderSelectVOS);
        }
        return PageResultFactory.createPageResult(workOrderSelectVOPage);
    }


    /**
     * 新增工单记录
     * @param loginUser
     * @return
     */
    public void addWorkOrder(WorkOrderAddBO workOrderAddBO, LoginUser loginUser) throws BizException {
        //获取代理账户信息
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        WorkOrder workOrder = new WorkOrder();
        workOrder.setSysUserId(agentAccount.getSysUserId());
        workOrder.setAgentCode(agentAccount.getAgentCode());
        workOrder.setAgentName(agentAccount.getAgentName());
        workOrder.setWorkOrderType(workOrderAddBO.getWorkOrderType());
        workOrder.setWorkOrderTitle(workOrderAddBO.getWorkOrderTitle());
        workOrder.setWorkOrderContent(workOrderAddBO.getWorkOrderContent());
        workOrder.setWorkOrderUrl(workOrderAddBO.getWorkOrderUrl());
        workOrder.setCreateTime(System.currentTimeMillis());
        workOrder.setWorkOrderStatus(BaseConstant.ZERO_INT);
        baseMapper.insert(workOrder);
    }

    /**
     * 新增工单回复
     * @param loginUser
     * @return
     */
    public void addWorkOrderRecover(WorkOrderRecover workOrderRecover, LoginUser loginUser) throws BizException {
        //获取代理账户信息
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        WorkOrder workOrder = baseMapper.selectById(workOrderRecover.getWorkOrderId());
        if(workOrder==null){
            throw new BizException("工单ID不存在:{}",workOrderRecover.getWorkOrderId());
        }
        workOrderRecover.setSysUserId(loginUser.getUserId());
        workOrderRecover.setCreateTime(System.currentTimeMillis());
        workOrderRecoverMapper.insert(workOrderRecover);
        //更新工单状态 工单状态 0 待管理回复 1待代理商回复 2已完结
        workOrder.setWorkOrderStatus(BaseConstant.ONE_INT);
        //如果属于代理
        if(loginUser.getUser().getDept().getDeptName().contains("代理")){
            workOrder.setWorkOrderStatus(BaseConstant.ZERO_INT);
        }
        workOrder.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(workOrder);
    }


    /**
     * 删除工单
     * @return
     */
    public void deleteWorkOrderRecover(Integer workOrderId) throws BizException {
        if(workOrderId==null){
            throw new BizException("工单ID不存在:{}",workOrderId);
        }
        baseMapper.deleteById(workOrderId);
        workOrderRecoverMapper.delete(new LambdaQueryWrapper<WorkOrderRecover>().eq(WorkOrderRecover::getWorkOrderId,workOrderId));
    }



}
