package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.SysNotice;

/**
 * 通知公告视图对象
 * 用于列表接口，包含HTML内容的纯文本概述
 *
 * @author ruoyi
 */
public class SysNoticeVO extends SysNotice
{
    private static final long serialVersionUID = 1L;

    /** 公告内容纯文本概述 */
    private String contentSummary;

    public String getContentSummary()
    {
        return contentSummary;
    }

    public void setContentSummary(String contentSummary)
    {
        this.contentSummary = contentSummary;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this, org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("noticeTitle", getNoticeTitle())
            .append("noticeType", getNoticeType())
            .append("noticeContent", getNoticeContent())
            .append("contentSummary", getContentSummary())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}