package com.ruoyi.common.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商品提卡费特例表实体
 */
@TableName("t_agent_product_card_fee_override")
@Data
public class AgentProductCardFeeOverride {

    /**
     * 主键ID
     */
    @TableId(value = "override_id", type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long overrideId;

    /**
     * 商品编码
     */
    @ApiModelProperty("商品编码")
    private String productCode;

    /**
     * 上级代理编码
     */
    @ApiModelProperty("上级代理编码")
    private String parentAgentCode;

    /**
     * 特例下级代理编码
     */
    @ApiModelProperty("特例下级代理编码")
    private String targetAgentCode;

    /**
     * 特例提卡费售价(元)
     */
    @ApiModelProperty("特例提卡费售价(元)")
    private Integer overrideFee;

    /**
     * 创建时参考上级成本(元)
     */
    @ApiModelProperty("创建时参考上级成本(元)")
    private Integer incomingCardFee;

    /**
     * 状态 1-生效 0-失效
     */
    @ApiModelProperty("状态 1-生效 0-失效")
    private Integer status;

    /**
     * 生效时间(毫秒时间戳)
     */
    @ApiModelProperty("生效时间(毫秒时间戳)")
    private Long effectiveTime;

    /**
     * 失效时间(毫秒时间戳, 可为空)
     */
    @ApiModelProperty("失效时间(毫秒时间戳, 可为空)")
    private Long expireTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String memo;

    /**
     * 最后操作人
     */
    @ApiModelProperty("最后操作人")
    private String operator;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Long updateTime;
}
