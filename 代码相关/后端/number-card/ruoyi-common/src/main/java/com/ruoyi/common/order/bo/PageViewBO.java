package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 页面访问记录业务对象
 *
 * @Description 用于接收前端传递的页面访问参数
 * @Author Claude
 * @Date 2025-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PageViewBO对象", description = "页面访问记录参数")
public class PageViewBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码", required = false, example = "AGENT001")
    @Size(max = 100, message = "代理商编码长度不能超过100个字符")
    private String agentCode;

    /**
     * 访客标识(cookie/UUID)
     */
    @ApiModelProperty(value = "访客标识(cookie/UUID)", required = true, example = "12345678-abcd-1234-efgh-123456789abc")
    @NotBlank(message = "访客标识不能为空")
    @Size(max = 100, message = "访客标识长度不能超过100个字符")
    private String visitorId;

    /**
     * 浏览器信息
     */
    @ApiModelProperty(value = "浏览器信息", required = false, example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
    @Size(max = 500, message = "浏览器信息长度不能超过500个字符")
    private String userAgent;

    /**
     * 来源页面
     */
    @ApiModelProperty(value = "来源页面", required = false, example = "https://www.google.com/")
    @Size(max = 500, message = "来源页面长度不能超过500个字符")
    private String referer;

    // ==================== 辅助方法 ====================

    /**
     * 验证必填字段
     * @return true 如果必填字段都不为空
     */
    public boolean isValid() {
        return visitorId != null && !visitorId.trim().isEmpty();
    }

    /**
     * 获取安全的代理商编码（处理null值）
     * @return 代理商编码，如果为null则返回空字符串
     */
    public String getSafeAgentCode() {
        return agentCode != null ? agentCode.trim() : "";
    }

    /**
     * 获取安全的访客ID（处理null值）
     * @return 访客ID，如果为null则返回空字符串
     */
    public String getSafeVisitorId() {
        return visitorId != null ? visitorId.trim() : "";
    }

    /**
     * 获取安全的浏览器信息（处理null值）
     * @return 浏览器信息，如果为null则返回空字符串
     */
    public String getSafeUserAgent() {
        return userAgent != null ? userAgent.trim() : "";
    }

    /**
     * 获取安全的来源页面（处理null值）
     * @return 来源页面，如果为null则返回空字符串
     */
    public String getSafeReferer() {
        return referer != null ? referer.trim() : "";
    }

    /**
     * 判断是否有代理商编码
     * @return true 如果有代理商编码
     */
    public boolean hasAgentCode() {
        return agentCode != null && !agentCode.trim().isEmpty();
    }

    /**
     * 判断是否有浏览器信息
     * @return true 如果有浏览器信息
     */
    public boolean hasUserAgent() {
        return userAgent != null && !userAgent.trim().isEmpty();
    }

    /**
     * 判断是否有来源页面
     * @return true 如果有来源页面
     */
    public boolean hasReferer() {
        return referer != null && !referer.trim().isEmpty() && !"".equals(referer.trim());
    }

    /**
     * 判断是否为移动设备（基于User-Agent简单判断）
     * @return true 如果是移动设备
     */
    public boolean isMobileDevice() {
        if (!hasUserAgent()) {
            return false;
        }

        String ua = userAgent.toLowerCase();
        return ua.contains("mobile") || ua.contains("android") || ua.contains("iphone");
    }

    /**
     * 判断是否为平板设备（基于User-Agent简单判断）
     * @return true 如果是平板设备
     */
    public boolean isTabletDevice() {
        if (!hasUserAgent()) {
            return false;
        }

        String ua = userAgent.toLowerCase();
        return ua.contains("ipad") || (ua.contains("android") && ua.contains("tablet"));
    }

    /**
     * 判断是否为PC设备（基于User-Agent简单判断）
     * @return true 如果是PC设备
     */
    public boolean isPCDevice() {
        if (!hasUserAgent()) {
            return true; // 默认认为是PC
        }

        return !isMobileDevice() && !isTabletDevice();
    }

    /**
     * 获取设备类型
     * @return 设备类型：PC/Mobile/Tablet/Unknown
     */
    public String getDeviceType() {
        if (!hasUserAgent()) {
            return "Unknown";
        }

        if (isTabletDevice()) {
            return "Tablet";
        } else if (isMobileDevice()) {
            return "Mobile";
        } else if (isPCDevice()) {
            return "PC";
        } else {
            return "Unknown";
        }
    }

    /**
     * 判断是否为直接访问（无来源页面）
     * @return true 如果是直接访问
     */
    public boolean isDirectVisit() {
        return !hasReferer();
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
     * 数据清理和标准化
     * @return 当前对象，支持链式调用
     */
    public PageViewBO normalize() {
        // 去除字符串两端空格
        if (agentCode != null) {
            this.agentCode = agentCode.trim();
        }
        if (visitorId != null) {
            this.visitorId = visitorId.trim();
        }
        if (userAgent != null) {
            this.userAgent = userAgent.trim();
        }
        if (referer != null) {
            this.referer = referer.trim();
        }

        // 处理空字符串
        if ("".equals(this.agentCode)) {
            this.agentCode = null;
        }
        if ("".equals(this.referer)) {
            this.referer = null;
        }

        return this;
    }

    /**
     * 创建一个用于测试的PageViewBO对象
     * @param agentCode 代理商编码
     * @param visitorId 访客ID
     * @return PageViewBO对象
     */
    public static PageViewBO createForTest(String agentCode, String visitorId) {
        PageViewBO bo = new PageViewBO();
        bo.setAgentCode(agentCode);
        bo.setVisitorId(visitorId);
        bo.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        bo.setReferer("https://www.google.com/");
        return bo;
    }

    /**
     * 重写toString方法，便于调试
     */
    @Override
    public String toString() {
        return "PageViewBO{" +
                "agentCode='" + agentCode + '\'' +
                ", visitorId='" + visitorId + '\'' +
                ", hasUserAgent=" + hasUserAgent() +
                ", hasReferer=" + hasReferer() +
                ", deviceType='" + getDeviceType() + '\'' +
                ", isDirectVisit=" + isDirectVisit() +
                '}';
    }
}