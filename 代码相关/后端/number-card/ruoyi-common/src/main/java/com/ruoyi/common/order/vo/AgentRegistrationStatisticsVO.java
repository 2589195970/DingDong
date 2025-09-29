package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 代理商注册统计返回VO
 *
 * @Description 代理商注册数据按时间维度统计
 * @Author Claude
 * @Date 2024/12/27
 */
@Data
@ApiModel(value = "代理商注册统计返回VO")
public class AgentRegistrationStatisticsVO {

    /**
     * 总注册数
     */
    @ApiModelProperty("总注册数")
    private Integer totalCount = 0;

    /**
     * 今日注册数
     */
    @ApiModelProperty("今日注册数")
    private Integer todayCount = 0;

    /**
     * 昨日注册数
     */
    @ApiModelProperty("昨日注册数")
    private Integer yesterdayCount = 0;

    /**
     * 本周注册数
     */
    @ApiModelProperty("本周注册数")
    private Integer thisWeekCount = 0;

    /**
     * 本月注册数
     */
    @ApiModelProperty("本月注册数")
    private Integer thisMonthCount = 0;

    /**
     * 较昨日增长率(%)
     */
    @ApiModelProperty("较昨日增长率(%)")
    private Double growthRate = 0.0;

    /**
     * 计算增长率
     * @return 当前对象，支持链式调用
     */
    public AgentRegistrationStatisticsVO calculateGrowthRate() {
        if (yesterdayCount == null || yesterdayCount == 0) {
            // 昨日为0，今日有注册则为100%增长，否则为0%
            this.growthRate = (todayCount != null && todayCount > 0) ? 100.0 : 0.0;
        } else {
            // 计算增长率：(今日-昨日)/昨日 * 100
            BigDecimal today = new BigDecimal(todayCount != null ? todayCount : 0);
            BigDecimal yesterday = new BigDecimal(yesterdayCount);
            BigDecimal growth = today.subtract(yesterday)
                    .divide(yesterday, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100));
            this.growthRate = growth.doubleValue();
        }
        return this;
    }
}