package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.OrderCommissionSelectBO;
import com.ruoyi.common.order.bo.OrderCommissionUpdateBO;
import com.ruoyi.common.order.entity.OrderCommission;
import com.ruoyi.common.order.entity.OrderCommissionDetails;
import com.ruoyi.common.order.vo.OrderCommissionSelectVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface OrderCommissionService extends IService<OrderCommission> {


    /**
     * 订单佣金列表查询
     *
     * @return
     * @throws BizException
     */
    PageResult<OrderCommissionSelectVO> selectOrderCommissionListPage(OrderCommissionSelectBO orderCommissionSelectBO) throws BizException;


    /**
     * 佣金结算数据导出
     * @return
     * @throws BizException
     */
    void exportOrderCommissionList(OrderCommissionSelectBO orderCommissionSelectBO, HttpServletResponse response) throws Exception;


    /**
     * 订单佣金明细列表查询
     *
     * @return
     * @throws BizException
     */
    List<OrderCommissionDetails> selectOrderCommissionDetailsList(Integer orderCommissionId) throws BizException;


    /**
     * 更新订单结算状态
     */
    void updateOrderCommissionStatus(OrderCommissionUpdateBO orderCommissionUpdateBO) throws BizException;


    /**
     * 删除佣金记录
     *
     * @param orderCommissionUpdateBO
     * @throws BizException
     */
    void deleteOrderCommission(OrderCommissionUpdateBO orderCommissionUpdateBO) throws BizException;

    /**
     * 生成订单佣金结算记录
     *
     */
    void saveOrderCommission(List<String> orderIdList);

}
