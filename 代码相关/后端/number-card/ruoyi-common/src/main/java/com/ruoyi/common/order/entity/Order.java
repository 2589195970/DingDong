package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.order.reuqest.BaseSubmitOrderRequest;
import com.ruoyi.common.utils.order.EmojiUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 16:39
 */
@TableName(value = "t_order", autoResultMap = true)
@Data
public class Order {

    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("订单ID")
    private Long orderId;

    /**
     * 上游订单号
     */
    @ApiModelProperty("上游订单号")
    private String orderUpstreamId;

    /**
     * 下游订单号
     */
    @ApiModelProperty("下游订单号")
    private String orderDownstreamId;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String cardName;

    /**
     * 用户下单手机号
     */
    @ApiModelProperty("用户下单手机号")
    private String cardPhone;

    /**
     * 用户身份证
     */
    @ApiModelProperty("用户身份证")
    private String cardId;

    /**
     * 用户下单姓名 默认与身份证姓名保持一致
     */
    @ApiModelProperty("用户下单姓名")
    private String receiverName;


    /**
     * 省编码(国家标准地址编码)
     */
    @ApiModelProperty("省编码")
    private String provinceCode;

    /**
     * 省名称
     */
    @ApiModelProperty("省名称")
    private String provinceName;

    /**
     * 市编码(国家标准地址编码)
     */
    @ApiModelProperty("市编码")
    private String cityCode;

    /**
     * 市名称
     */
    @ApiModelProperty("市名称")
    private String cityName;

    /**
     * 县/区编码(国家标准地址编码)
     */
    @ApiModelProperty("县/区编码")
    private String countyCode;

    /**
     * 县/区名称
     */
    @ApiModelProperty("县/区名称")
    private String countyName;


    /**
     * 用户详细地址
     */
    @ApiModelProperty("用户详细地址")
    private String cardAddress;

    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private Integer orderStatus;

    /**
     * 订单说明信息
     */
    @ApiModelProperty("订单说明信息")
    private String orderMessage;

    /**
     * 产品编码
     */
    @ApiModelProperty("产品编码")
    private String productCode;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 产品类型
     */
    @ApiModelProperty("产品类型")
    private String productType;

    /**
     * 上游API
     */
    @ApiModelProperty("上游API")
    private String upstreamApi;

    /**
     * 上游API名称
     */
    @ApiModelProperty("上游API名称")
    private String upstreamApiName;

    /**
     * 上游产品CODE
     */
    @ApiModelProperty("上游产品CODE")
    private String upstreamProductCode;

    /**
     * 上游产品名称
     */
    @ApiModelProperty("上游产品名称")
    private String upstreamProductName;

    /**
     * 上游订单状态
     */
    @ApiModelProperty("上游订单状态")
    private String upstreamOrderStatus;

    /**
     * 上游订单状态说明
     */
    @ApiModelProperty("上游订单状态说明")
    private String upstreamOrderStatusMessage;

    /**
     * 订单向上游推送时间
     */
    @ApiModelProperty("订单向上游推送时间")
    private Long upstreamPushTime;

    /**
     * 下游代理商code
     */
    @ApiModelProperty("下游代理商code")
    private String downstreamCode;

    /**
     * 下游代理商名称
     */
    @ApiModelProperty("下游代理商名称")
    private String downstreamName;

    /**
     * 下游代理商归属列表 json格式 记录订单各级代理及佣金信息
     */
    @ApiModelProperty("下游代理商归属列表")
    private String downstreamFatherList;

    /**
     * 是否充值 0 未充值 1已充值是否充值 0 未充值 1已充值
     */
    @ApiModelProperty("是否充值")
    private Integer isRecharged;

    /**
     * 充值信息
     */
    @ApiModelProperty("充值信息")
    private String rechargeAmount;


    /**
     * 充值时间
     */
    @ApiModelProperty("充值时间")
    private Long rechargeTime;


    /**
     * 快递公司
     */
    @ApiModelProperty("快递公司")
    private String express;


    /**
     * 下快递单号
     */
    @ApiModelProperty("下快递单号")
    private String trackingNumber;

    /**
     * 发货时间
     */
    @ApiModelProperty("发货时间")
    private Long deliveryTime;

    /**
     * 激活时间
     */
    @ApiModelProperty("激活时间")
    private Long activeTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Long updateTime;

    /**
     * 生产号码
     */
    @ApiModelProperty("生产号码")
    private String accNumber;

    /**
     * 生产号码归属地信息 json格式
     */
    @ApiModelProperty("生产号码归属地信息")
    private String accNumberAddress;

    /**
     * '订单来源 具体来源值看枚举'
     */
    @ApiModelProperty("订单来源")
    private Integer orderSource;

    /**
     * 订单异步标记 0 同步订单 1异步订单
     */
    @ApiModelProperty("订单异步标记")
    private Integer orderAsync;

    /**
     * '订单临时参数
     */
    @ApiModelProperty("订单临时参数")
    private String jsonParam;

    /**
     * 订单佣金状态 0 未到结算状态 1 待结算 22 部分结算 3已结算 4无法结算
     */
    @ApiModelProperty("订单佣金状态")
    private Integer orderCommissionStatus;

    /**
     * 订单佣金说明
     */
    @ApiModelProperty("订单佣金说明")
    private String orderCommissionMessage;

    /**
     * 运营商类型
     */
    @ApiModelProperty("运营商类型")
    private Integer operatorType;





    private static Order buildBaseOrder(Long orderId, BaseSubmitOrderRequest request, Product product, UpstreamApi upstreamApi) throws Exception {
        Order order = new Order();
        order.orderId = orderId;
        order.createTime = System.currentTimeMillis();
        order.updateTime = 0L;
        order.orderAsync = 0;
        order.orderCommissionStatus = 0;
        order.isRecharged = 0;
        return order;
    }

    public static Order buildOrderFromGeneral(Long orderId, BaseSubmitOrderRequest request, Product product, UpstreamApi upstreamApi) throws Exception {
        Order order = buildBaseOrder(orderId, request, product,upstreamApi);
        order.orderDownstreamId = request.getOrderDownstreamId();
        order.cardName = EmojiUtils.removeEmoji(request.getCardName());
        order.cardPhone = request.getCardPhone();
        order.cardId = request.getCardId();
        order.receiverName = request.getReceiverName();
        order.provinceCode = request.getProvinceCode();
        order.provinceName = request.getProvinceName();
        order.cityCode = request.getCityCode();
        order.cityName = request.getCityName();
        order.countyCode = request.getCountyCode();
        order.countyName = request.getCountyName();
        order.cardAddress = EmojiUtils.removeEmoji(request.getCardAddress());
        order.orderStatus = OrderEnum.CREATE.getStatus();
        order.jsonParam = request.getJsonParam();
        order.orderMessage = OrderEnum.CREATE.getMessage();
        order.orderSource = request.getOrderSource();
        order.orderAsync = upstreamApi.getIsAsync();
        //产品信息
        order.productCode = product.getProductCode();
        order.productName = product.getProductName();
        order.productType = product.getProductType().toString();
        //上游信息
        order.upstreamApi = upstreamApi.getUpstreamApiCode();
        order.upstreamApiName = upstreamApi.getUpstreamApiName();
        return order;
    }

}
