package com.ruoyi.common.order.entity;


import lombok.Data;

/**
 * 上游API和产品信息
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/15 16:39
 */
@Data
public class UpstreamInfo {

    /**
     * 上游API信息
     */
    UpstreamApi upstreamApi;

    /**
     * 上游提交产品信息
     */
    UpstreamProduct upstreamProduct;
}
