package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商排名查询条件
 *
 * @Description
 * @Author 系统
 * @Date 2025/01/27
 */
@Data
@ApiModel(value = "代理商排名查询条件")
public class AgentRankingSelectBO {

    /**
     * 页码
     */
    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    /**
     * 每页大小
     */
    @ApiModelProperty("每页大小")
    private Integer pageSize = 10;

    /**
     * 代理商姓名（模糊查询）
     */
    @ApiModelProperty("代理商姓名")
    private String agentName;

    /**
     * 排名类型：0-总排名，1-本月排名，2-昨日排名，3-今日排名
     */
    @ApiModelProperty("排名类型：0-总排名，1-本月排名，2-昨日排名，3-今日排名")
    private Integer rankingType = 0;
}
