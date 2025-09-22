package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提现记录详情表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/2/22 16:39
 */
@TableName(value = "t_withdrawal_record_details", autoResultMap = true)
@Data
public class WithdrawalRecordDetails {

    /**
     * 提现记录详情表ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("提现记录详情表ID")
    private Integer withdrawalRecordDetailsId;

    /**
     * 提现记录表id
     */
    @ApiModelProperty("提现记录表id")
    private Integer withdrawalRecordId;

    /**
     * sysUser表ID
     */
    @ApiModelProperty(value = "sysUser表ID")
    private Long sysUserId;

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码")
    private String agentCode;

    /**
     * 代理商名称
     */
    @ApiModelProperty(value = "代理商名称")
    private String agentName;

    /**
     * 来源单号
     */
    @ApiModelProperty("来源单号")
    private String sourceNumber;

    /**
     *  金额类型 '0 存入 1 提现'
     */
    @ApiModelProperty(value = "'0 存入 1 提现'")
    private Integer amountType;

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
     * 提现类型 0 号卡佣金 1代理商提现 2 管理员操作
     */
    @ApiModelProperty(value = "提现类型  0 号卡佣金 1代理商提现 2 管理员操作")
    private Integer withdrawalType;

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
     * 提现类型 0 微信 1 支付宝 2 已打款
     */
    @ApiModelProperty(value = "提现类型 0 微信 1 支付宝 2 已打款")
    private Integer applyWithdrawalType;

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
