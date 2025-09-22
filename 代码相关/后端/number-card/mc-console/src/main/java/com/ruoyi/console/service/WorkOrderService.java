package com.ruoyi.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.WorkOrderAddBO;
import com.ruoyi.common.order.bo.WorkOrderSelectBO;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.vo.WorkOrderSelectVO;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface WorkOrderService extends IService<WorkOrder> {


    /**
     * 工单列表查询
     * @return
     * @throws BizException
     */
     PageResult<WorkOrderSelectVO> selectWorkOrderListPage(WorkOrderSelectBO workOrderSelectBO, LoginUser loginUser) throws BizException;


    /**
     * 新增工单记录
     * @param loginUser
     * @return
     */
     void addWorkOrder(WorkOrderAddBO workOrderAddBO, LoginUser loginUser) throws BizException;



    /**
     * 新增工单回复
     * @param loginUser
     * @return
     */
     void addWorkOrderRecover(WorkOrderRecover workOrderRecover, LoginUser loginUser) throws BizException;


    /**
     * 删除工单
     * @return
     */
     void deleteWorkOrderRecover(Integer workOrderId) throws BizException;
}
