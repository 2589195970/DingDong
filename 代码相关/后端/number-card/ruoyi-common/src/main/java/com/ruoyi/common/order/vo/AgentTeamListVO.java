package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商团队列表返回值
 * @Description 包含代理商基础信息和团队统计信息
 * @Author
 * @Date 2024/12/25
 */
@Data
@ApiModel(value = "代理商团队列表返回值")
public class AgentTeamListVO extends AgentAccountListVO {

    /**
     * 团队成员数（该代理商的直接下级数量）
     */
    @ApiModelProperty("团队成员数（该代理商的直接下级数量）")
    private Integer teamMemberCount;
}