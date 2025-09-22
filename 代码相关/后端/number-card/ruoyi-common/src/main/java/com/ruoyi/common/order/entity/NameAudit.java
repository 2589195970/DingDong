package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 实名认证
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 16:39
 */
@TableName(value = "t_name_audit", autoResultMap = true)
@Data
public class NameAudit {

    /**
     * 实名审核记录ID
     */
    @TableId(type = IdType.AUTO)
    private Integer nameAuditId;

    /**
     * 登录账号ID
     */
    @ApiModelProperty("登录账号ID")
    private Long sysUserId;

    /**
     * 代理商编码
     */
    @ApiModelProperty("代理商编码")
    private String agentCode;

    /**
     * 代理商名称
     */
    @ApiModelProperty("代理商名称")
    private String agentName;

    /**
     * 身份证正面url
     */
    @ApiModelProperty("身份证正面url")
    private String cardIdUrlFront;

    /**
     * 身份证反面url
     */
    @ApiModelProperty("身份证反面url")
    private String cardIdUrlBack;

    /**
     * 状态 0 待认证 1 实名审核失败 2实名认证成功
     */
    @ApiModelProperty("状态 0 待认证 1 实名审核失败 2实名认证成功")
    private Integer status;

    /**
     * '备注'
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * '用户姓名'
     */
    @ApiModelProperty("用户姓名")
    private String cardName;

    /**
     * 用户身份证
     */
    @ApiModelProperty("用户身份证")
    private String cardId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;


}
