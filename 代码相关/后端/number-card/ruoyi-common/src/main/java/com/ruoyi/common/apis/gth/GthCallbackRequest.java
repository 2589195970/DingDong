package com.ruoyi.common.apis.gth;

import com.ruoyi.common.order.reuqest.BaseNotifyRequest;
import com.ruoyi.common.order.reuqest.ProductStatusNotifyRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("感叹号回调信息")
public class GthCallbackRequest extends BaseNotifyRequest implements ProductStatusNotifyRequest {

    /**
     * 回调事件类型
     */
    private String event_type;
    /**
     * 商品编码
     */
    private String product_code;

    /**
     * 合作方id
     */
    private String outer_id;

    /**
     * 商品状态
     */
    private String product_status;
    /**
     * 生产号码
     */
    private String plan_mobile_produced;

    /**
     * 状态 待发货 已发货 开卡失败 身份证审核失败 订单终止 其余状态不会回调
     */
    private String status;

    /**
     * iccid
     */
    private String iccid;

    /**
     * 签名 可把token和参数传至《订单状态回调验签》测试
     */
    private String sign;

    /**
     * 物流单号
     */
    private String tracking_number;

    /**
     * 物流公司名称
     */
    private String tracking_company;

    /**
     * 失败原因
     */
    private String reason;

    /**
     * 激活 1 已激活
     */
    private String is_activated;

    /**
     * 激活时间
     */
    private String activated_at;

    /**
     * 首充 1 已首充
     */
    private String is_recharged;

    /**
     * 首充时间
     */
    private String recharged_at;

    /**
     * 首充金额
     */
    private String recharge_amount;

    @Override
    public String getCallbackProductStatus() {
        return this.product_status;
    }

    @Override
    public String getCallbackProductCode() {
        return this.product_code;
    }

}
