package com.ruoyi.console.service.impl.agent;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentOrderSelectBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.AgentCommissionJson;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.vo.AgentOrderSelectVO;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.AgentAccountMapper;
import com.ruoyi.console.mapper.OrderMapper;
import com.ruoyi.console.service.AgentAccountService;
import com.ruoyi.console.service.AgentOrderService;
import com.ruoyi.console.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 代理商订单相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class AgentOrderServiceImpl extends ServiceImpl<AgentAccountMapper, AgentAccount> implements AgentOrderService  {

    @Resource
    AgentAccountService agentAccountService;

    @Resource
    OrderMapper orderMapper;

    @Resource
    ProductService productService;

    /**
     * 代理商订单查询
     *
     * @return
     * @throws BizException
     */
    public PageResult<AgentOrderSelectVO> agentSelectOrderListPage(AgentOrderSelectBO agentOrderSelectBO, LoginUser loginUser) throws BizException {
        //获取代理账户信息
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        //读取分页
        Page page = new Page(agentOrderSelectBO.getPageNo(), agentOrderSelectBO.getPageSize());
        Page<Order> orderPage = orderMapper.selectPage(page, new LambdaQueryWrapper<Order>()
                .eq(StringUtils.isNotEmpty(agentOrderSelectBO.getOrderId()), Order::getOrderId, agentOrderSelectBO.getOrderId())
                .eq(StringUtils.isNotEmpty(agentOrderSelectBO.getOrderDownstreamId()), Order::getOrderDownstreamId, agentOrderSelectBO.getOrderDownstreamId())
                .like(StringUtils.isNotEmpty(agentOrderSelectBO.getCardName()), Order::getCardName, agentOrderSelectBO.getCardName())
                .eq(StringUtils.isNotEmpty(agentOrderSelectBO.getCardPhone()), Order::getCardPhone, agentOrderSelectBO.getCardPhone())
                .eq(StringUtils.isNotEmpty(agentOrderSelectBO.getCardId()), Order::getCardId, agentOrderSelectBO.getCardId())
                .eq(agentOrderSelectBO.getOrderStatus() != null, Order::getOrderStatus, agentOrderSelectBO.getOrderStatus())
                .eq(agentOrderSelectBO.getIsRecharged() != null, Order::getIsRecharged, agentOrderSelectBO.getIsRecharged())
                .eq(agentOrderSelectBO.getOrderSource() != null, Order::getOrderSource, agentOrderSelectBO.getOrderSource())
                .eq(agentOrderSelectBO.getOrderCommissionStatus() != null, Order::getOrderCommissionStatus, agentOrderSelectBO.getOrderCommissionStatus())
                .eq(StringUtils.isNotEmpty(agentOrderSelectBO.getAccNumber()), Order::getAccNumber, agentOrderSelectBO.getAccNumber())
                //此处查的是账号下归属代理的订单 所以只要归属中出现就要查出来
                .like(StringUtils.isNotEmpty(agentOrderSelectBO.getDownstreamCode()), Order::getDownstreamFatherList, agentOrderSelectBO.getDownstreamCode())
                .like(Order::getDownstreamFatherList, agentAccount.getAgentCode())
                //查询时间范围
                .between((agentOrderSelectBO.getStarTime() != null && agentOrderSelectBO.getEndTime() != null), Order::getCreateTime, agentOrderSelectBO.getStarTime(), agentOrderSelectBO.getEndTime())
                .orderByDesc(Order::getCreateTime)
        );
        Page<AgentOrderSelectVO> agentOrderSelectVOPage = new Page<>();
        BeanUtil.copyProperties(orderPage, agentOrderSelectVOPage);
        if (!CollectionUtils.isEmpty(orderPage.getRecords())) {
            List<AgentOrderSelectVO> agentOrderSelectVOS = new ArrayList<>();
            for (Order order : orderPage.getRecords()) {
                AgentOrderSelectVO agentOrderSelectVO = new AgentOrderSelectVO();
                BeanUtil.copyProperties(order, agentOrderSelectVO);
                agentOrderSelectVO.setOrderId(order.getOrderId()+"");
                AgentCommissionJson agentCommissionJson = getShowDownstreamInfo(agentAccount, order);
                agentOrderSelectVO.setShowDownstreamCode(agentCommissionJson.getAgentCode());
                agentOrderSelectVO.setShowDownstreamName(agentCommissionJson.getAgentName());
                if(agentAccount.getIsEncrypt() == BaseConstant.ZERO_INT){
                    encipherAgentOrderSelectVO(agentOrderSelectVO);
                }
                //查询产品头图
                Product product = productService.getProductNotStatus(order.getProductCode());
                if(product!=null){
                    agentOrderSelectVO.setProductMasterMap(product.getProductMasterMap());
                }
                agentOrderSelectVOS.add(agentOrderSelectVO);
            }
            agentOrderSelectVOPage.setRecords(agentOrderSelectVOS);
        }
        return PageResultFactory.createPageResult(agentOrderSelectVOPage);
    }

    /**
     * 判断代理商归属下级
     * @param agentAccount
     * @param order
     * @return
     * @throws BizException
     */
    public AgentCommissionJson getShowDownstreamInfo(AgentAccount agentAccount, Order order) throws BizException {
        AgentCommissionJson agentCommissionJson = new AgentCommissionJson();
        if (StringUtils.isNotEmpty(order.getDownstreamCode()) && order.getDownstreamCode().equals(agentAccount.getAgentCode())) {
            //如果是本人进的单 下游代理商展示为本人
            agentCommissionJson.setAgentCode(order.getDownstreamCode());
            agentCommissionJson.setAgentName(order.getDownstreamName());
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
     * 订单数据脱敏展示
     *
     * @return
     */
    public AgentOrderSelectVO encipherAgentOrderSelectVO(AgentOrderSelectVO agentOrderSelectVO) {
        if (agentOrderSelectVO == null) {
            return agentOrderSelectVO;
        }
        agentOrderSelectVO.setCardId(encipherInfo(agentOrderSelectVO.getCardId(), 10, 18));
        if (org.springframework.util.StringUtils.hasLength(agentOrderSelectVO.getCardAddress())) {
            agentOrderSelectVO.setCardAddress(encipherInfo(agentOrderSelectVO.getCardAddress(), 1, agentOrderSelectVO.getCardAddress().length()));
        }
        return agentOrderSelectVO;
    }

    /**
     * 订单信息脱敏方法
     *
     * @param str
     * @param startNumber
     * @param endNumber
     * @return
     */
    public String encipherInfo(String str, Integer startNumber, Integer endNumber) {
        if (!org.springframework.util.StringUtils.hasLength(str) || startNumber > str.length() || endNumber > str.length() || startNumber > endNumber) {
            return str;
        }
        StringBuilder encipher = new StringBuilder();
        for (int i = 0; i < endNumber - startNumber; i++) {
            encipher.append("*");
        }
        StringBuilder stringBuilder = new StringBuilder(str);
        return stringBuilder.replace(startNumber, endNumber, encipher.toString()).toString();
    }
}
