package com.ruoyi.common.apis.douyin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店订单查询请求类
 * @date 2025/7/18 14:05
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class DouDianOrderSearchRequest extends DouDianBaseRequest {
    /**
     *商品，number型代表商品ID，其它代表商品名称
     * 非必传
     */
    private String product;

    /**
     * 【下单端】 0、站外 1、火山 2、抖音 3、头条 4、西瓜 5、微信 6、值点app 7、头条lite 8、懂车帝 9、皮皮虾
     * 11、抖音极速版 12、TikTok 13、musically 14、穿山甲 15、火山极速版 16、服务市场
     * 26、番茄小说 27、UG教育营销电商平台 28、Jumanji 29、电商SDK
     * 类型Int64
     * 非必传
     */
    private Long bType;


    /**
     * 售后状态：all-全部，in_aftersale-售后中，refund-退款中，refund_success-退款成功，refund_fail-退款失败，exchange_success-换货成功 aftersale_close-售后关闭
     * 非必传
     */
    private String afterSaleStatusDesc;


    /**
     * 物流单号
     * 非必传
     */
    private Long trackingNo;


    /**
     * 预售类型：1 全款预售
     * 类型Int64
     * 非必传
     */
    private Long presellType;

    /**
     * 【订单类型】 0、普通订单 2、虚拟商品订单 4、电子券（poi核销） 5、三方核销
     * 非必传
     * 类型Int64
     */
    private Long orderType;

    /**
     * 下单开始时间，秒级时间戳；create_time_start和create_time_end最大支持查询最近90天内的数据；
     * 非必传
     * 类型Int64
     */
    private Long createTimeStart;

    /**
     * 下单结束时间，秒级时间戳；create_time_start和create_time_end最大支持查询最近90天内的数据
     * 非必传
     * 类型Int64
     */
    private Long createTimeEnd;

    /**
     * 异常订单，1-异常取消，2-风控审核中
     * 非必传
     * 类型Int64
     */
    private Long abnormalOrder;

    /**
     * 【交易类型】 0、普通 1、拼团 2、定金预售 3、订金找贷 4、拍卖 5、0元单 6、回收 7、寄卖 10、样品
     * 非必传
     * 类型Int64
     */
    private Long tradeType;

    /**
     * 状态组合查询，直接输入状态码（只支持一个元素）
     * 非必传
     * list类型
     *
     * order_status
     * String
     * 订单状态，","分隔多个状态
     * main_status
     * String
     * 主流程状态，","分隔多个状态
     * update_time_start
     * Int64
     * 订单更新开始时间，秒级时间戳；
     * update_time_end
     * Int64
     * 订单更新结束时间，秒级时间戳；
     */
    private List<Object> combineStatus;

    /**
     *单页大小，限制100以内。size* page最大不能超过5万条。超过5万条请使用创建时间或更新时间分割。
     * 必传
     */
    private Long size;

    /**
     * 页码，0页开始；size* page最大不能超过5万条。超过5万条请使用创建时间或更新事情分割。
     * 必传
     */
    private Long page;

    /**
     * 排序时间条件；1、不传默认create_time；2、create_time：按照订单创建时间，降序排列；3、update_time：按照更新时间，降序排列；
     * 非必传
     */
    private String orderBy;

    /**
     * 排序类型，1、不传默认为false；2、false：降序排列；3、true：升序排列；
     * 非必传
     */
    private Boolean orderAsc;

    private String orderStatus;
}

