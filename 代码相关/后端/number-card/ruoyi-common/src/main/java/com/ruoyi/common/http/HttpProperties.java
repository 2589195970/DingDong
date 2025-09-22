package com.ruoyi.common.http;

import lombok.Data;

@Data
public class HttpProperties {
    /**
     * 超时时间，单位（毫秒）
     */
    private Long timeout;

    /**
     * 最大连接数量
     */
    private Integer maxConnection;

    /**
     * 连接失败是否重试
     */
    private boolean resetConnection;


    /**
     * 单域名最大请求线程
     */
    private Integer maxHostConnection;

    /**
     * 最大空闲
     */
    private Integer maxIdleConnections;

    /**
     * 保持连接
     */
    private Long keepAliveDurationMill;

    /**
     * 是否开启代理
     */
    private Boolean isEnabled = false;
}
