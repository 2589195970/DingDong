package com.ruoyi.common.order.vo;

import java.math.BigDecimal;

/**
 * 代理商排行榜VO
 *
 * @author ruoyi
 */
public class AgentRankingVO {

    /** 排名 */
    private Integer ranking;

    /** 代理商编码 */
    private String agentCode;

    /** 代理商名称 */
    private String agentName;

    /** 代理商等级 */
    private Integer level;

    /** 统计数值 */
    private BigDecimal statValue;

    /** 统计类型描述 */
    private String statDesc;

    /** 统计时间戳 */
    private Long statTime;

    public AgentRankingVO() {
    }

    public AgentRankingVO(Integer ranking, String agentCode, String agentName, Integer level, BigDecimal statValue) {
        this.ranking = ranking;
        this.agentCode = agentCode;
        this.agentName = agentName;
        this.level = level;
        this.statValue = statValue;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public BigDecimal getStatValue() {
        return statValue;
    }

    public void setStatValue(BigDecimal statValue) {
        this.statValue = statValue;
    }

    public String getStatDesc() {
        return statDesc;
    }

    public void setStatDesc(String statDesc) {
        this.statDesc = statDesc;
    }

    public Long getStatTime() {
        return statTime;
    }

    public void setStatTime(Long statTime) {
        this.statTime = statTime;
    }

    @Override
    public String toString() {
        return "AgentRankingVO{" +
                "ranking=" + ranking +
                ", agentCode='" + agentCode + '\'' +
                ", agentName='" + agentName + '\'' +
                ", level=" + level +
                ", statValue=" + statValue +
                ", statDesc='" + statDesc + '\'' +
                ", statTime=" + statTime +
                '}';
    }
}