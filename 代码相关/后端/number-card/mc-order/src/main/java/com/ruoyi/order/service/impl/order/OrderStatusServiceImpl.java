package com.ruoyi.order.service.impl.order;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.enums.ProductEnum;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.order.mapper.OrderMapper;
import com.ruoyi.order.service.order.OrderCommissionService;
import com.ruoyi.order.service.order.OrderStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:13
 */
@Service
@Slf4j
public class OrderStatusServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderStatusService {

    @Resource
    OrderCommissionService orderCommissionService;

    /**
     * 记录订单失败
     *
     * @param order
     * @param desc
     */
    public void saveOrderFail(Order order, String desc) {
        try {
            log.info("记录订单失败:{}",order.getOrderId());
            if (OrderEnum.INVALID.getStatus().equals(order.getOrderStatus())&&order.getOrderMessage().equals(desc)) {
                log.info("订单{}已处于失败状态,错误原因相同无需更新:{},{}", order.getOrderId(), order.getOrderMessage(), desc);
                return;
            }
            log.info("订单{}更新失败状态,错误信息:{}-->{}", order.getOrderId(), order.getOrderMessage(), desc);
            order.setOrderStatus(OrderEnum.INVALID.getStatus());
            order.setOrderMessage(desc);
            order.setUpdateTime(System.currentTimeMillis());
            baseMapper.updateById(order);
        }catch (Exception e){
            log.info("记录订单失败状态失败:{},{}",order.getOrderId(),e.getMessage());
        }

    }




    /**
     * 记录发货
     *
     * @param order
     * @param logistics      快递公司
     * @param trackingNumber 物流单号
     */
    public void saveDelivery(Order order, String logistics, String trackingNumber) {
        try {
            if (StrUtil.isBlankIfStr(logistics) || StrUtil.isBlankIfStr(trackingNumber)) {
                return;
            }
            boolean result1 = false;
            boolean result2 = false;
            //step1：仅保存运单,这时订单状态可能小于也可能大于发货状态
            if (StrUtil.isBlankIfStr(order.getExpress()) || StrUtil.isBlankIfStr(order.getTrackingNumber())) {
                order.setExpress(logistics);
                order.setTrackingNumber(trackingNumber);
                order.setDeliveryTime(System.currentTimeMillis());
                order.setUpdateTime(System.currentTimeMillis());
                result1 = this.update(order, new LambdaQueryWrapper<Order>().eq(Order::getOrderId, order.getOrderId()).and(wrapper -> wrapper.isNull(Order::getExpress).or().isNull(Order::getTrackingNumber)));
                if (result1) {
                    log.info("记录发货物流和单号:{}:{},订单状态:{},订单ID:{}", logistics, trackingNumber, order.getOrderStatus(),order.getOrderId());
                }
            }
            //step2：更新订单状态，根据订单状态更新情况决定是否要设置扣量和发送消息
            if (order.getOrderStatus() < OrderEnum.SHIPPED.getStatus()) {
                order.setOrderStatus(OrderEnum.SHIPPED.getStatus());
                order.setDeliveryTime(System.currentTimeMillis());
                order.setUpdateTime(System.currentTimeMillis());
                result2 = this.update(order, new LambdaQueryWrapper<Order>().eq(Order::getOrderId, order.getOrderId()).lt(Order::getOrderStatus, OrderEnum.SHIPPED.getStatus()));
                if (result2) {
                    log.info("状态更新,记录发货物流和单号:{}:{},订单状态:{},订单ID:{}", logistics, trackingNumber, order.getOrderStatus(),order.getOrderId());
                }
            }
        } catch (Exception e) {
            log.info("记录订单发货状态失败:{},{}",order.getOrderId(),e.getMessage());
        }
    }


    /**
     * 记录预占号码
     *
     * @param order
     * @param number
     */
    public void saveAccNumber(Order order, String number) {
        try {
            if (StrUtil.isBlankIfStr(number)) {
                return;
            }
            if(order.getAccNumber()==null||!number.equals(order.getAccNumber())){
                log.info("记录生产号码:{},{}", order.getOrderId(),number);
                order.setAccNumber(number);
                order.setUpdateTime(System.currentTimeMillis());
                this.updateById(order);
            }
        }catch (Exception e){
            log.info("记录生产号码失败:{},{}",order.getOrderId(),e.getMessage());
        }

    }


    /**
     * 记录激活
     *
     * @param order
     */
    public void saveActive(Order order,Long activeTime) {
        try {
            if (order.getOrderStatus() >= OrderEnum.ACTIVATED.getStatus()) {
                return;
            }
            order.setOrderStatus(OrderEnum.ACTIVATED.getStatus());
            order.setActiveTime(activeTime);
            if (order.getActiveTime() == null) {
                //有部分运营商会返回用户激活时间 以此为准
                order.setActiveTime(System.currentTimeMillis());
            }
            order.setUpdateTime(System.currentTimeMillis());
            boolean result = this.update(order, new LambdaQueryWrapper<Order>().eq(Order::getOrderId, order.getOrderId()).lt(Order::getOrderStatus, OrderEnum.ACTIVATED.getStatus()));
            //此处需要判断是否是日结产品 日结产品自动转入佣金结算流程
            if (result) {
                if(ProductEnum.DAILY_SETTLEMENT.getStatus().equals(Integer.valueOf(order.getProductType()))){
                    orderCommissionService.saveOrderCommission(order);
                }
            }
        } catch (Exception e) {
            log.info("记录激活失败:{},{}",order.getOrderId(),e.getMessage());
        }
    }


    /**
     * 记录充值
     *
     * @param order
     * @param rechargeAmount
     * @param rechargeTime 充值时间
     */
    public void saveRecharge(Order order, String rechargeAmount, Long rechargeTime) {
        try {
            if(BaseConstant.ONE_INT==order.getIsRecharged()){
                log.info("充值信息已记录,不能覆盖:{}",order.getOrderId());
            }
            order.setIsRecharged(BaseConstant.ONE_INT);
            order.setRechargeAmount(rechargeAmount);
            order.setRechargeTime(rechargeTime);
            if(order.getRechargeTime()==null){
                order.setRechargeTime(System.currentTimeMillis());
            }
            order.setUpdateTime(System.currentTimeMillis());
            boolean result = this.update(order, new LambdaQueryWrapper<Order>().eq(Order::getOrderId, order.getOrderId()).and(wapper -> wapper.eq(Order::getIsRecharged, false).or().isNull(Order::getIsRecharged)));
            if (result) {
                //判断是否生成了结算订单 如果生成了 更新
                orderCommissionService.updateOrderCommissionRecharged(order);
                log.info("充值记录成功:{}:{},订单状态:{},订单ID:{}", rechargeAmount, rechargeTime, order.getOrderStatus(),order.getOrderId());
            }
        } catch (Exception e) {
            log.info("记录充值失败:{},{}",order.getOrderId(),e.getMessage());
        }
    }

}
