package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商团队查询参数
 * @Description 查询当前代理商的直接下级团队
 * @Author
 * @Date 2024/12/25
 */
@Data
public class AgentTeamQueryBO extends BaseBO {

    /**
     * 父代理商编码（自动设置为当前登录代理商）
     */
    @ApiModelProperty("父代理商编码（自动设置）")
    private String parentAgentCode;

    /**
     * 综合搜索关键词（支持agent_account_id, agent_code, agent_name, phone）
     */
    @ApiModelProperty("综合搜索关键词（ID/编码/名称/手机号）")
    private String searchKeyword;

    /**
     * 是否实名 0否 1是
     */
    @ApiModelProperty("是否实名 0否 1是")
    private Integer isRealName;

    /**
     * 是否启用  0 启用 1禁用
     */
    @ApiModelProperty("0 启用 1禁用")
    private Integer isEnabled;

    /**
     * 代理商等级(1-5)
     */
    @ApiModelProperty("代理商等级(1-5)")
    private Integer level;
}