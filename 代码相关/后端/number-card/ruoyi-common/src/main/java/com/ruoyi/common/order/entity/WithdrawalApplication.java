package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提现申请记录表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/2/22 16:39
 */
@TableName(value = "t_withdrawal_application", autoResultMap = true)
@Data
public class WithdrawalApplication {

    /**
     * 提现记录详情表ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("提现申请表ID")
    private Integer withdrawalApplicationId;

    /**
     * 提现单号(年月日+六位随机数)
     */
    @ApiModelProperty(value = "提现单号(年月日时分秒+六位随机数)")
    private String applicationNumber;

    /**
     * 申请用户账号ID
     */
    @ApiModelProperty(value = "申请用户账号ID")
    private Long applyUserId;

    /**
     * 申请代理商编码
     */
    @ApiModelProperty(value = "申请代理商编码")
    private String applyAgentCode;

    /**
     * 申请代理商名称
     */
    @ApiModelProperty(value = "申请代理商名称")
    private String applyAgentName;

    /**
     * 提现金额 单位分
     */
    @ApiModelProperty(value = "提现金额 单位分")
    private Integer withdrawalAmount;

    /**
     * 实际金额 单位分
     */
    @ApiModelProperty(value = "实际金额 单位分")
    private Integer receivedAmount;

    /**
     * 提现费率
     */
    @ApiModelProperty(value = "提现费率")
    private Integer withdrawalRate;

    /**
     * 提现手续费 单位分
     */
    @ApiModelProperty(value = "提现手续费 单位分")
    private Integer withdrawalRateAmount;

    /**
     * 提现状态 0 申请中 1 打款中 2 提现成功 3 提现失败
     */
    @ApiModelProperty(value = "提现状态 0 申请中 1 打款中 2 提现成功 3 提现失败")
    private Integer withdrawalStatus;

    /**
     * 提现类型 0 微信 1 支付宝 2 已打款
     */
    @ApiModelProperty(value = "提现类型 0 微信 1 支付宝 2 已打款")
    private Integer withdrawalType;

    /**
     * 微信收款码url
     */
    @ApiModelProperty(value = "微信收款码url")
    private String wxUrl;

    /**
     * 支付宝账号
     */
    @ApiModelProperty(value = "支付宝账号")
    private String zfbAccount;

    /**
     * 支付宝账号真实姓名
     */
    @ApiModelProperty(value = "支付宝账号真实姓名")
    private String zfbAccountName;

    /**
     * 银行卡号
     */
    @ApiModelProperty(value = "银行卡号")
    private String bankNumber;

    /**
     * 银行开户行名称
     */
    @ApiModelProperty(value = "银行开户行名称")
    private String bankName;

    /**
     * 银行卡账号真实姓名
     */
    @ApiModelProperty(value = "银行卡账号真实姓名")
    private String bankNumberName;

    /**
     * 银行卡预留手机号
     */
    @ApiModelProperty(value = "银行卡预留手机号")
    private String bankNumberPhone;

    /**
     * 交易流水号
     */
    @ApiModelProperty(value = "交易流水号")
    private String serialNumber;


    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    
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
