package com.ruoyi.common.order.bo;

/**
 * 代理商排行榜查询参数BO
 *
 * @author ruoyi
 */
public class AgentRankingQueryBO {

    /** 排行类型：1-佣金排行 2-订单量排行 3-激活量排行 4-团队发展排行 */
    private Integer rankingType;

    /** 时间维度：1-日榜 2-月榜 */
    private Integer timeDimension;

    /** 查询TOP数量，默认10 */
    private Integer topCount = 10;

    /** 开始时间戳 */
    private Long startTime;

    /** 结束时间戳 */
    private Long endTime;

    /** 代理商编码（查询某个代理商团队内部排行） */
    private String parentAgentCode;

    public AgentRankingQueryBO() {
    }

    public AgentRankingQueryBO(Integer rankingType, Integer timeDimension) {
        this.rankingType = rankingType;
        this.timeDimension = timeDimension;
    }

    public Integer getRankingType() {
        return rankingType;
    }

    public void setRankingType(Integer rankingType) {
        this.rankingType = rankingType;
    }

    public Integer getTimeDimension() {
        return timeDimension;
    }

    public void setTimeDimension(Integer timeDimension) {
        this.timeDimension = timeDimension;
    }

    public Integer getTopCount() {
        return topCount;
    }

    public void setTopCount(Integer topCount) {
        this.topCount = topCount;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getParentAgentCode() {
        return parentAgentCode;
    }

    public void setParentAgentCode(String parentAgentCode) {
        this.parentAgentCode = parentAgentCode;
    }

    @Override
    public String toString() {
        return "AgentRankingQueryBO{" +
                "rankingType=" + rankingType +
                ", timeDimension=" + timeDimension +
                ", topCount=" + topCount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", parentAgentCode='" + parentAgentCode + '\'' +
                '}';
    }
}