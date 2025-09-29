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
 * 页面访问明细记录实体类
 *
 * @Description 页面访问明细记录表对应实体，记录每次访问的详细信息
 * @Author Claude
 * @Date 2025-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PageViewLog对象", description = "页面访问明细记录表")
public class PageViewLog implements Serializable {

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
     * 访客标识(cookie/UUID)
     */
    @ApiModelProperty(value = "访客标识(cookie/UUID)")
    private String visitorId;

    /**
     * IP地址
     */
    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    /**
     * 浏览器信息
     */
    @ApiModelProperty(value = "浏览器信息")
    private String userAgent;

    /**
     * 来源页面
     */
    @ApiModelProperty(value = "来源页面")
    private String referer;

    /**
     * 设备类型(PC/Mobile/Tablet)
     */
    @ApiModelProperty(value = "设备类型(PC/Mobile/Tablet)")
    private String deviceType;

    /**
     * 访问时间
     */
    @ApiModelProperty(value = "访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date visitTime;

    // ==================== 辅助方法 ====================

    /**
     * 设置访问时间为当前时间
     * @return 当前对象，支持链式调用
     */
    public PageViewLog setVisitTimeNow() {
        this.visitTime = new Date();
        return this;
    }

    /**
     * 根据User-Agent判断设备类型
     * @param userAgent 浏览器用户代理字符串
     * @return 当前对象，支持链式调用
     */
    public PageViewLog detectDeviceType(String userAgent) {
        if (userAgent == null || userAgent.trim().isEmpty()) {
            this.deviceType = "Unknown";
            return this;
        }

        String ua = userAgent.toLowerCase();

        // 检测移动设备
        if (ua.contains("mobile") || ua.contains("android") || ua.contains("iphone") || ua.contains("ipad")) {
            if (ua.contains("ipad") || (ua.contains("android") && ua.contains("tablet"))) {
                this.deviceType = "Tablet";
            } else {
                this.deviceType = "Mobile";
            }
        }
        // 检测桌面设备
        else if (ua.contains("windows") || ua.contains("macintosh") || ua.contains("linux")) {
            this.deviceType = "PC";
        }
        // 其他情况
        else {
            this.deviceType = "Unknown";
        }

        this.userAgent = userAgent;
        return this;
    }

    /**
     * 获取浏览器名称
     * @return 浏览器名称
     */
    public String getBrowserName() {
        if (userAgent == null || userAgent.trim().isEmpty()) {
            return "Unknown";
        }

        String ua = userAgent.toLowerCase();

        if (ua.contains("edge")) {
            return "Microsoft Edge";
        } else if (ua.contains("chrome")) {
            return "Google Chrome";
        } else if (ua.contains("firefox")) {
            return "Mozilla Firefox";
        } else if (ua.contains("safari") && !ua.contains("chrome")) {
            return "Safari";
        } else if (ua.contains("opera") || ua.contains("opr")) {
            return "Opera";
        } else if (ua.contains("msie") || ua.contains("trident")) {
            return "Internet Explorer";
        } else {
            return "Unknown";
        }
    }

    /**
     * 获取操作系统名称
     * @return 操作系统名称
     */
    public String getOperatingSystem() {
        if (userAgent == null || userAgent.trim().isEmpty()) {
            return "Unknown";
        }

        String ua = userAgent.toLowerCase();

        if (ua.contains("windows nt 10.0")) {
            return "Windows 10";
        } else if (ua.contains("windows nt 6.3")) {
            return "Windows 8.1";
        } else if (ua.contains("windows nt 6.2")) {
            return "Windows 8";
        } else if (ua.contains("windows nt 6.1")) {
            return "Windows 7";
        } else if (ua.contains("windows")) {
            return "Windows";
        } else if (ua.contains("mac os x")) {
            return "Mac OS X";
        } else if (ua.contains("android")) {
            return "Android";
        } else if (ua.contains("iphone os") || ua.contains("ios")) {
            return "iOS";
        } else if (ua.contains("linux")) {
            return "Linux";
        } else {
            return "Unknown";
        }
    }

    /**
     * 判断是否为移动设备
     * @return true 如果是移动设备
     */
    public boolean isMobile() {
        return "Mobile".equals(this.deviceType);
    }

    /**
     * 判断是否为平板设备
     * @return true 如果是平板设备
     */
    public boolean isTablet() {
        return "Tablet".equals(this.deviceType);
    }

    /**
     * 判断是否为PC设备
     * @return true 如果是PC设备
     */
    public boolean isPC() {
        return "PC".equals(this.deviceType);
    }

    /**
     * 判断是否有来源页面
     * @return true 如果有来源页面
     */
    public boolean hasReferer() {
        return referer != null && !referer.trim().isEmpty() && !"".equals(referer.trim());
    }

    /**
     * 获取来源域名
     * @return 来源域名，如果没有来源或解析失败则返回null
     */
    public String getRefererDomain() {
        if (!hasReferer()) {
            return null;
        }

        try {
            java.net.URL url = new java.net.URL(referer);
            return url.getHost();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是否为直接访问（无来源）
     * @return true 如果是直接访问
     */
    public boolean isDirectVisit() {
        return !hasReferer();
    }

    /**
     * 判断是否为今天的访问记录
     * @return true 如果是今天的访问记录
     */
    public boolean isToday() {
        if (this.visitTime == null) {
            return false;
        }

        Date today = new Date();
        return isSameDay(this.visitTime, today);
    }

    /**
     * 设置基本信息
     * @param agentCode 代理商编码
     * @param visitorId 访客ID
     * @param ipAddress IP地址
     * @return 当前对象，支持链式调用
     */
    public PageViewLog setBasicInfo(String agentCode, String visitorId, String ipAddress) {
        this.agentCode = agentCode;
        this.visitorId = visitorId;
        this.ipAddress = ipAddress;
        return this;
    }

    /**
     * 设置来源信息
     * @param userAgent 浏览器信息
     * @param referer 来源页面
     * @return 当前对象，支持链式调用
     */
    public PageViewLog setSourceInfo(String userAgent, String referer) {
        this.detectDeviceType(userAgent);
        this.referer = referer;
        return this;
    }

    /**
     * 判断两个日期是否为同一天
     */
    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

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
        return "PageViewLog{" +
                "id=" + id +
                ", agentCode='" + agentCode + '\'' +
                ", visitorId='" + visitorId + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", visitTime=" + visitTime +
                ", hasReferer=" + hasReferer() +
                ", browserName='" + getBrowserName() + '\'' +
                ", operatingSystem='" + getOperatingSystem() + '\'' +
                '}';
    }
}