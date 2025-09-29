package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 代理商统计面板数据VO
 *
 * @Description 包含今日和本月的邀请、订单、激活、佣金统计数据
 * @Author Claude
 * @Date 2024/12/27
 */
@Data
@ApiModel(value = "代理商统计面板数据VO")
public class AgentDashboardStatisticsVO {

    // ==================== 今日统计 ====================

    /**
     * 今日邀请数
     */
    @ApiModelProperty("今日邀请数")
    private Integer todayInviteCount = 0;

    /**
     * 今日订单数
     */
    @ApiModelProperty("今日订单数")
    private Integer todayOrderCount = 0;

    /**
     * 今日激活数
     */
    @ApiModelProperty("今日激活数")
    private Integer todayActivateCount = 0;

    /**
     * 今日佣金（元）
     */
    @ApiModelProperty("今日佣金（元）")
    private BigDecimal todayCommission = BigDecimal.ZERO;

    // ==================== 本月统计 ====================

    /**
     * 本月邀请数
     */
    @ApiModelProperty("本月邀请数")
    private Integer monthInviteCount = 0;

    /**
     * 本月订单数
     */
    @ApiModelProperty("本月订单数")
    private Integer monthOrderCount = 0;

    /**
     * 本月激活数
     */
    @ApiModelProperty("本月激活数")
    private Integer monthActivateCount = 0;

    /**
     * 本月佣金（元）
     */
    @ApiModelProperty("本月佣金（元）")
    private BigDecimal monthCommission = BigDecimal.ZERO;

    // ==================== 附加信息 ====================

    /**
     * 统计类型 0-个人 1-团队
     */
    @ApiModelProperty("统计类型：0-个人，1-团队")
    private Integer statisticsType = 0;

    /**
     * 数据更新时间戳
     */
    @ApiModelProperty("数据更新时间戳")
    private Long updateTime = System.currentTimeMillis();

    // ==================== 辅助方法 ====================

    /**
     * 设置今日数据
     * @param inviteCount 邀请数
     * @param orderCount 订单数
     * @param activateCount 激活数
     * @param commission 佣金
     * @return 当前对象，支持链式调用
     */
    public AgentDashboardStatisticsVO setTodayData(Integer inviteCount, Integer orderCount,
                                                  Integer activateCount, BigDecimal commission) {
        this.todayInviteCount = inviteCount != null ? inviteCount : 0;
        this.todayOrderCount = orderCount != null ? orderCount : 0;
        this.todayActivateCount = activateCount != null ? activateCount : 0;
        this.todayCommission = commission != null ? commission : BigDecimal.ZERO;
        return this;
    }

    /**
     * 设置本月数据
     * @param inviteCount 邀请数
     * @param orderCount 订单数
     * @param activateCount 激活数
     * @param commission 佣金
     * @return 当前对象，支持链式调用
     */
    public AgentDashboardStatisticsVO setMonthData(Integer inviteCount, Integer orderCount,
                                                  Integer activateCount, BigDecimal commission) {
        this.monthInviteCount = inviteCount != null ? inviteCount : 0;
        this.monthOrderCount = orderCount != null ? orderCount : 0;
        this.monthActivateCount = activateCount != null ? activateCount : 0;
        this.monthCommission = commission != null ? commission : BigDecimal.ZERO;
        return this;
    }

    /**
     * 刷新更新时间
     * @return 当前对象，支持链式调用
     */
    public AgentDashboardStatisticsVO refreshUpdateTime() {
        this.updateTime = System.currentTimeMillis();
        return this;
    }
}