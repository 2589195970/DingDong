package com.ruoyi.console.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.order.bo.AgainOrderBO;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.reuqest.OrderSubmitRequest;
import com.ruoyi.console.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:13
 */
@Service
@Slf4j
public class ImportServiceImpl implements ImportOrderService {


    @Value(value = "${submit.url}")
    private String submitOrderUrl;

    @Resource
    HttpClient httpClient;

    /**
     * 导入订单
     * @return
     * @throws BizException
     */
    @Async("orderSaveExecutor")
    public void importOrderListSubmit(List<OrderSubmitRequest> orderSubmitRequestList) {
        for(OrderSubmitRequest orderSubmitRequest:orderSubmitRequestList){
            try {
                //此处http调用一次order服务
                httpClient.postJsonForString(submitOrderUrl,orderSubmitRequest,null);
            }catch (Exception e){
                log.info("{}导入异常:{}",orderSubmitRequest.getCardId(),e.getMessage());
            }
        }
    }



}
