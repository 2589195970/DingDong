package com.ruoyi.common.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 页面访问统计汇总实体类
 *
 * @Description 页面访问统计汇总表对应实体，记录按天汇总的访问数据
 * @Author Claude
 * @Date 2025-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PageViewStatistics对象", description = "页面访问统计汇总表")
public class PageViewStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码")
    private String agentCode;

    /**
     * 访问日期
     */
    @ApiModelProperty(value = "访问日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date viewDate;

    /**
     * 访问次数
     */
    @ApiModelProperty(value = "访问次数")
    private Integer viewCount;

    /**
     * 独立访客数
     */
    @ApiModelProperty(value = "独立访客数")
    private Integer visitorCount;

    /**
     * 独立IP数
     */
    @ApiModelProperty(value = "独立IP数")
    private Integer ipCount;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    // ==================== 辅助方法 ====================

    /**
     * 设置统计数据
     * @param viewCount 访问次数
     * @param visitorCount 独立访客数
     * @param ipCount 独立IP数
     * @return 当前对象，支持链式调用
     */
    public PageViewStatistics setStatisticsData(Integer viewCount, Integer visitorCount, Integer ipCount) {
        this.viewCount = viewCount != null ? viewCount : 0;
        this.visitorCount = visitorCount != null ? visitorCount : 0;
        this.ipCount = ipCount != null ? ipCount : 0;
        return this;
    }

    /**
     * 增加访问次数
     * @param increment 增加的次数，默认为1
     * @return 当前对象，支持链式调用
     */
    public PageViewStatistics incrementViewCount(Integer increment) {
        this.viewCount = (this.viewCount != null ? this.viewCount : 0) + (increment != null ? increment : 1);
        return this;
    }

    /**
     * 设置访问日期为今天
     * @return 当前对象，支持链式调用
     */
    public PageViewStatistics setViewDateToday() {
        this.viewDate = new Date();
        return this;
    }

    /**
     * 检查是否为今天的统计数据
     * @return true 如果是今天的数据
     */
    public boolean isToday() {
        if (this.viewDate == null) {
            return false;
        }

        Date today = new Date();
        return isSameDay(this.viewDate, today);
    }

    /**
     * 获取访问率（访客数/访问次数）
     * @return 访问率，如果访问次数为0则返回0
     */
    public double getVisitorRate() {
        if (this.viewCount == null || this.viewCount == 0) {
            return 0.0;
        }

        if (this.visitorCount == null) {
            return 0.0;
        }

        return (double) this.visitorCount / this.viewCount;
    }

    /**
     * 获取IP访问率（IP数/访问次数）
     * @return IP访问率，如果访问次数为0则返回0
     */
    public double getIpRate() {
        if (this.viewCount == null || this.viewCount == 0) {
            return 0.0;
        }

        if (this.ipCount == null) {
            return 0.0;
        }

        return (double) this.ipCount / this.viewCount;
    }

    /**
     * 获取平均每个访客的访问次数
     * @return 平均访问次数，如果访客数为0则返回0
     */
    public double getAvgViewsPerVisitor() {
        if (this.visitorCount == null || this.visitorCount == 0) {
            return 0.0;
        }

        if (this.viewCount == null) {
            return 0.0;
        }

        return (double) this.viewCount / this.visitorCount;
    }

    /**
     * 判断两个日期是否为同一天
     */
    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        // 使用日历进行日期比较
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
               cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR);
    }

    /**
     * 重写toString方法，便于调试
     */
    @Override
    public String toString() {
        return "PageViewStatistics{" +
                "id=" + id +
                ", agentCode='" + agentCode + '\'' +
                ", viewDate=" + viewDate +
                ", viewCount=" + viewCount +
                ", visitorCount=" + visitorCount +
                ", ipCount=" + ipCount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}