package com.ruoyi.common.apis.yqe;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruoyi.common.order.reuqest.BaseNotifyRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("172回调信息")
public class CallbackData {

    /**
     * 合作方订单号
     */
    @JSONField(name = "OrderNo")
    private String orderNo;

    /**
     * 172平台订单号，请求头sign里加密的是这个号码。
     */
    @JSONField(name = "OrderNo172")
    private String orderNo172;

    /**
     * 订单状态：已发货，已完成，审核不通过，已取消，已撤单
     */
    @JSONField(name = "OrderStatus")
    private String orderStatus;

    /**
     * 办理号码
     */
    @JSONField(name = "ThirdPhone")
    private String thirdPhone;

    /**
     * 备注失败原因
     */
    @JSONField(name = "Remark")
    private String remark;

    /**
     * 物流公司
     */
    @JSONField(name = "ExpressName")
    private String expressName;

    /**
     * 物流单号
     */
    @JSONField(name = "ExpressCode")
    private String expressCode;

    /**
     * 激活状态：已激活，未激活，可能为Null
     */
    @JSONField(name = "CardStatus")
    private String cardStatus;

    /**
     * 激活时间格式yyyy-MM-dd HH:mm:ss，可能为Null
     */
    @JSONField(name = "ActiveTime")
    private String activeTime;

    /**
     * 是否首充  0 未首充   1已首充
     */
    @JSONField(name = "IsFirstCharge")
    private Integer isFirstCharge;

    /**
     * 首充金额
     */
    @JSONField(name = "FirstCharge")
    private String firstCharge;

}
