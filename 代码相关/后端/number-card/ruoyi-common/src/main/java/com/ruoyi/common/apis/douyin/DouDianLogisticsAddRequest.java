package com.ruoyi.common.apis.douyin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author 陈思伟
 * @version 1.0
 * @description: 发货回传实体类
 * @date 2025/7/14 16:56
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class DouDianLogisticsAddRequest extends DouDianBaseRequest {
    /**
     * 订单ID
     * 必传
     * 其余选项都是非必传
     */
    private String orderId;
    /**
     * 物流公司名称
     */
    private String company;
    /**
     * 物流公司code
     */
    private String companyCode;
    /**
     * 快递单号
     */
    private String logisticsCode;
    /**
     *是否拒绝退款申请（true表示拒绝退款，并继续发货；不传或为false表示有退款需要处理，拒绝发货），is_refund_reject和is_reject_refund随机使用一个即可
     */
    private Boolean isRefundReject;

    /**
     * 是否拒绝退款申请（true表示拒绝退款，并继续发货；不传或为false表示有退款需要处理，拒绝发货），is_refund_reject和is_reject_refund随机使用一个即可
     */
    private Boolean isRejectRefund;
    /**
     * 发货地址ID,通过地址库接口查询
     */
    private Long addressId;



}
