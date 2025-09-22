package com.ruoyi.console.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.OrderCommissionSelectBO;
import com.ruoyi.common.order.bo.OrderCommissionUpdateBO;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.vo.OrderCommissionSelectVO;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.OrderCommissionDetailsMapper;
import com.ruoyi.console.mapper.OrderCommissionMapper;
import com.ruoyi.console.mapper.OrderMapper;
import com.ruoyi.console.service.AgentAccountService;
import com.ruoyi.console.service.ExportService;
import com.ruoyi.console.service.OrderCommissionService;
import com.ruoyi.console.service.WithdrawalRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedList;
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

    @Resource
    WithdrawalRecordService withdrawalRecordService;


    @Resource
    OrderMapper orderMapper;

    @Resource
    AgentAccountService agentAccountService;

    @Resource
    ExportService exportService;

    /**
     * 订单佣金列表查询
     *
     * @return
     * @throws BizException
     */
    public PageResult<OrderCommissionSelectVO> selectOrderCommissionListPage(OrderCommissionSelectBO orderCommissionSelectBO) throws BizException {
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(1L, true);
        //读取分页
        Page page = new Page(orderCommissionSelectBO.getPageNo(), orderCommissionSelectBO.getPageSize());
        Page<OrderCommission> orderCommissionPage = baseMapper.selectPage(page, new LambdaQueryWrapper<OrderCommission>()
                .eq(StringUtils.isNotEmpty(orderCommissionSelectBO.getOrderId()), OrderCommission::getOrderId, orderCommissionSelectBO.getOrderId())
                .eq(StringUtils.isNotEmpty(orderCommissionSelectBO.getOrderDownstreamId()), OrderCommission::getOrderDownstreamId, orderCommissionSelectBO.getOrderDownstreamId())
                .like(StringUtils.isNotEmpty(orderCommissionSelectBO.getCardName()), OrderCommission::getCardName, orderCommissionSelectBO.getCardName())
                .eq(StringUtils.isNotEmpty(orderCommissionSelectBO.getCardPhone()), OrderCommission::getCardPhone, orderCommissionSelectBO.getCardPhone())
                .eq(StringUtils.isNotEmpty(orderCommissionSelectBO.getCardId()), OrderCommission::getCardId, orderCommissionSelectBO.getCardId())
                .eq(orderCommissionSelectBO.getOrderStatus() != null, OrderCommission::getOrderStatus, orderCommissionSelectBO.getOrderStatus())
                .eq(orderCommissionSelectBO.getIsRecharged() != null, OrderCommission::getIsRecharged, orderCommissionSelectBO.getIsRecharged())
                .eq(StringUtils.isNotEmpty(orderCommissionSelectBO.getDownstreamAgentCode()), OrderCommission::getDownstreamAgentCode, orderCommissionSelectBO.getDownstreamAgentCode())
                .eq(orderCommissionSelectBO.getOrderCommissionStatus() != null, OrderCommission::getOrderCommissionStatus, orderCommissionSelectBO.getOrderCommissionStatus())
                //查询时间范围
                .between((orderCommissionSelectBO.getStarTime() != null && orderCommissionSelectBO.getEndTime() != null), OrderCommission::getOrderCreateTime, orderCommissionSelectBO.getStarTime(), orderCommissionSelectBO.getEndTime())
                .orderByDesc(OrderCommission::getCreateTime)
        );
        Page<OrderCommissionSelectVO> orderCommissionSelectVOPage = new Page<>();
        BeanUtil.copyProperties(orderCommissionPage, orderCommissionSelectVOPage);
        if (!CollectionUtils.isEmpty(orderCommissionPage.getRecords())) {
            List<OrderCommissionSelectVO> orderCommissionSelectVOList = new ArrayList<>();
            for (OrderCommission orderCommission : orderCommissionPage.getRecords()) {
                OrderCommissionSelectVO orderCommissionSelectVO = new OrderCommissionSelectVO();
                BeanUtil.copyProperties(orderCommission, orderCommissionSelectVO);
                orderCommissionSelectVO.setOrderId(orderCommission.getOrderId()+"");
                orderCommissionSelectVO.setDownstreamCode(orderCommission.getDownstreamAgentCode());
                orderCommissionSelectVO.setDownstreamName(orderCommission.getDownstreamAgentName());
                AgentCommissionJson agentCommissionJson = getShowDownstreamInfo(agentAccount, orderCommission);
                orderCommissionSelectVO.setShowDownstreamCode(agentCommissionJson.getAgentCode());
                orderCommissionSelectVO.setShowDownstreamName(agentCommissionJson.getAgentName());
                orderCommissionSelectVOList.add(orderCommissionSelectVO);
            }
            orderCommissionSelectVOPage.setRecords(orderCommissionSelectVOList);
        }
        return PageResultFactory.createPageResult(orderCommissionSelectVOPage);
    }


    public AgentCommissionJson getShowDownstreamInfo(AgentAccount agentAccount, OrderCommission order) throws BizException {
        AgentCommissionJson agentCommissionJson = new AgentCommissionJson();
        if (StringUtils.isNotEmpty(order.getDownstreamAgentCode()) && order.getDownstreamAgentCode().equals(agentAccount.getAgentCode())) {
            //如果是本人进的单 下游代理商展示为本人
            agentCommissionJson.setAgentCode(order.getDownstreamAgentCode());
            agentCommissionJson.setAgentName(order.getDownstreamAgentName());
            return agentCommissionJson;
        }
        //如果不是 判断是哪个下游的 取当前代理下一级作为展示项
        List<AgentCommissionJson> agentCommissionJsons = JSONObject.parseArray(order.getDownstreamFatherList(), AgentCommissionJson.class);
        if (CollectionUtils.isEmpty(agentCommissionJsons)) {
            return agentCommissionJson;
        }
        for (int i = 0; i < agentCommissionJsons.size(); i++) {
            if (agentCommissionJsons.get(i).getAgentCode().equals(agentAccount.getAgentCode())) {
                if ((i + 1) < agentCommissionJsons.size()) {
                    return agentCommissionJsons.get(i + 1);
                }
            }
        }
        return agentCommissionJson;
    }




    /**
     * 佣金结算数据导出
     * @return
     * @throws BizException
     */
    public void exportOrderCommissionList(OrderCommissionSelectBO orderCommissionSelectBO, HttpServletResponse response) throws Exception {
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(1L, true);
        List<OrderCommission> orderCommissionList = baseMapper.selectList(new LambdaQueryWrapper<OrderCommission>()
                .eq(StringUtils.isNotEmpty(orderCommissionSelectBO.getOrderId()), OrderCommission::getOrderId, orderCommissionSelectBO.getOrderId())
                .eq(StringUtils.isNotEmpty(orderCommissionSelectBO.getOrderDownstreamId()), OrderCommission::getOrderDownstreamId, orderCommissionSelectBO.getOrderDownstreamId())
                .like(StringUtils.isNotEmpty(orderCommissionSelectBO.getCardName()), OrderCommission::getCardName, orderCommissionSelectBO.getCardName())
                .eq(StringUtils.isNotEmpty(orderCommissionSelectBO.getCardPhone()), OrderCommission::getCardPhone, orderCommissionSelectBO.getCardPhone())
                .eq(StringUtils.isNotEmpty(orderCommissionSelectBO.getCardId()), OrderCommission::getCardId, orderCommissionSelectBO.getCardId())
                .eq(orderCommissionSelectBO.getOrderStatus() != null, OrderCommission::getOrderStatus, orderCommissionSelectBO.getOrderStatus())
                .eq(orderCommissionSelectBO.getIsRecharged() != null, OrderCommission::getIsRecharged, orderCommissionSelectBO.getIsRecharged())
                .eq(StringUtils.isNotEmpty(orderCommissionSelectBO.getDownstreamAgentCode()), OrderCommission::getDownstreamAgentCode, orderCommissionSelectBO.getDownstreamAgentCode())
                .eq(orderCommissionSelectBO.getOrderCommissionStatus() != null, OrderCommission::getOrderCommissionStatus, orderCommissionSelectBO.getOrderCommissionStatus())
                //查询时间范围
                .between((orderCommissionSelectBO.getStarTime() != null && orderCommissionSelectBO.getEndTime() != null), OrderCommission::getOrderCreateTime, orderCommissionSelectBO.getStarTime(), orderCommissionSelectBO.getEndTime())
                .orderByDesc(OrderCommission::getCreateTime)
        );
        //构造导出数据
        List<OrderCommissionSelectVO> orderCommissionSelectVOList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(orderCommissionList)){
            for (OrderCommission orderCommission : orderCommissionList) {
                OrderCommissionSelectVO orderCommissionSelectVO = new OrderCommissionSelectVO();
                BeanUtil.copyProperties(orderCommission, orderCommissionSelectVO);
                orderCommissionSelectVO.setOrderId(orderCommission.getOrderId()+"");
                orderCommissionSelectVO.setDownstreamCode(orderCommission.getDownstreamAgentCode());
                orderCommissionSelectVO.setDownstreamName(orderCommission.getDownstreamAgentName());
                AgentCommissionJson agentCommissionJson = getShowDownstreamInfo(agentAccount, orderCommission);
                orderCommissionSelectVO.setShowDownstreamCode(agentCommissionJson.getAgentCode());
                orderCommissionSelectVO.setShowDownstreamName(agentCommissionJson.getAgentName());
                orderCommissionSelectVOList.add(orderCommissionSelectVO);
            }
        }
        //数据导出
        exportService.writeCsvResponse(response, orderCommissionListToCsvList(orderCommissionSelectVOList),"attachment;filename=佣金结算导出.csv");
    }

    /**
     * @return
     * @throws BizException
     */
    private List<String[]> orderCommissionListToCsvList(List<OrderCommissionSelectVO> orderCommissionSelectVOList) throws BizException {
        List<String[]> csvList = new LinkedList<>();
        //构造表头
        csvList.add(new String[]{"代理商", "用户姓名", "手机号", "用户身份证", "订单ID", "订单状态", "充值状态", "产品佣金", "佣金状态", "佣金说明","结算单生成时间","结算时间"});
        //添加文本格式化，去掉回车，换行，TAB，防止csv中出错
        for (OrderCommissionSelectVO orderCommissionSelectVO : orderCommissionSelectVOList) {
            csvList.add(new String[]{
                    exportService.getCSVText(orderCommissionSelectVO.getShowDownstreamName()),
                    exportService.getCSVText(orderCommissionSelectVO.getCardName()),
                    exportService.getCSVText(orderCommissionSelectVO.getCardPhone()),
                    exportService.getCSVText(orderCommissionSelectVO.getCardId()),
                    exportService.getCSVText(orderCommissionSelectVO.getOrderId() != null ? String.valueOf(orderCommissionSelectVO.getOrderId()) : null),
                    exportService.getCSVText(OrderEnum.getOrderMessageByStatus(orderCommissionSelectVO.getOrderStatus())),
                    exportService.getCSVText(orderCommissionSelectVO.getIsRecharged() == BaseConstant.ONE_INT ? "已充值" : "未充值"),
                    exportService.getCSVText(String.valueOf(orderCommissionSelectVO.getProductCommission())),
                    exportService.getCSVText(OrderEnum.OrderCommissionEnum.getCommissionByStatus(orderCommissionSelectVO.getOrderCommissionStatus())),
                    exportService.getCSVText(orderCommissionSelectVO.getOrderCommissionMessage()),
                    exportService.getCSVDate(orderCommissionSelectVO.getCreateTime()),
                    exportService.getCSVDate(orderCommissionSelectVO.getCommissionTime())
            });
        }
        return csvList;
    }






    /**
     * 订单佣金明细列表查询
     *
     * @param orderCommissionId
     * @return
     * @throws BizException
     */
    @Override
    public List<OrderCommissionDetails> selectOrderCommissionDetailsList(Integer orderCommissionId) throws BizException {
        if (orderCommissionId == null) {
            throw new BizException("佣金ID不能为空");
        }
        return orderCommissionDetailsMapper.selectList(new LambdaQueryWrapper<OrderCommissionDetails>().eq(OrderCommissionDetails::getOrderCommissionId, orderCommissionId));
    }



    /**
     * 生成订单佣金结算记录
     *
     */
    @Async("orderSaveExecutor")
    public void saveOrderCommission(List<String> orderIdList){
        if(CollectionUtils.isEmpty(orderIdList)){
            return;
        }
        for (String orderId:orderIdList){
            try {
                Order order = orderMapper.selectById(orderId);
                if(order == null){
                    throw new BizException("订单{}不存在,生成佣金结算记录失败",orderId);
                }
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
            }catch (Exception e){
                log.info("佣金结算记录生成失败:{}",e.getMessage());
            }
        }



    }



    /**
     * 更新订单结算状态
     */
    //@Transactional
    public void updateOrderCommissionStatus(OrderCommissionUpdateBO orderCommissionUpdateBO) throws BizException {
        OrderCommission orderCommission = baseMapper.selectById(orderCommissionUpdateBO.getOrderCommissionId());
        if (orderCommission == null) {
            throw new BizException("佣金ID{}记录为空:{}", orderCommissionUpdateBO.getOrderCommissionId());
        }
        if (orderCommission.getOrderCommissionStatus() == OrderEnum.OrderCommissionEnum.TYPE_3.getCommissionType()) {
            throw new BizException("佣金ID{}的订单状态为已结算,不允许修改:{}", orderCommissionUpdateBO.getOrderCommissionId());
        }
        if (StringUtils.isNotEmpty(orderCommissionUpdateBO.getRemark())) {
            orderCommission.setRemark(orderCommissionUpdateBO.getRemark());
        }
        if (StringUtils.isNotEmpty(orderCommissionUpdateBO.getOrderCommissionMessage())) {
            orderCommission.setOrderCommissionMessage(orderCommissionUpdateBO.getOrderCommissionMessage());
        }
        if (orderCommissionUpdateBO.getOrderCommissionStatus() != null) {
            orderCommission.setOrderCommissionStatus(orderCommissionUpdateBO.getOrderCommissionStatus());
        }
        if (orderCommissionUpdateBO.getOrderCommissionStatus() != null && orderCommissionUpdateBO.getOrderCommissionStatus() == OrderEnum.OrderCommissionEnum.TYPE_3.getCommissionType()) {
            orderCommission.setCommissionTime(System.currentTimeMillis());
        }
        try {
            orderCommission.setUpdateTime(System.currentTimeMillis());
            baseMapper.updateById(orderCommission);
            //更新状态到已结算状态 需要生成对应记录
            if (orderCommissionUpdateBO.getOrderCommissionStatus() != null && orderCommissionUpdateBO.getOrderCommissionStatus() == OrderEnum.OrderCommissionEnum.TYPE_3.getCommissionType()) {
                //查询佣金明细 汇总到提现余额中
                List<OrderCommissionDetails> orderCommissionDetailsList = selectOrderCommissionDetailsList(orderCommission.getOrderCommissionId());
                for (OrderCommissionDetails orderCommissionDetails : orderCommissionDetailsList) {
                    withdrawalRecordService.insertOrderCommissionDeposit(orderCommissionDetails);
                }
            }
            //结算单状态同步一份到订单中
            Order order = orderMapper.selectById(orderCommission.getOrderId());
            if(order!=null){
                order.setOrderCommissionStatus(orderCommission.getOrderCommissionStatus());
                order.setOrderCommissionMessage(orderCommission.getOrderCommissionMessage());
                order.setUpdateTime(System.currentTimeMillis());
                orderMapper.updateById(order);
            }


        } catch (Exception e) {
            throw new BizException("请检查此订单是否有同状态佣金明细:{}", e.getMessage());
        }


    }

    /**
     * 删除佣金记录
     *
     * @param orderCommissionUpdateBO
     * @throws BizException
     */
    public void deleteOrderCommission(OrderCommissionUpdateBO orderCommissionUpdateBO) throws BizException {
        OrderCommission orderCommission = baseMapper.selectById(orderCommissionUpdateBO.getOrderCommissionId());
        if (orderCommission == null) {
            throw new BizException("佣金ID{}记录为空:{}", orderCommissionUpdateBO.getOrderCommissionId());
        }
        if (orderCommission.getOrderCommissionStatus() == OrderEnum.OrderCommissionEnum.TYPE_3.getCommissionType()) {
            throw new BizException("佣金ID{}的订单为已结算,不允许修改:{}", orderCommissionUpdateBO.getOrderCommissionId());
        }
        baseMapper.deleteById(orderCommissionUpdateBO.getOrderCommissionId());
    }
}
