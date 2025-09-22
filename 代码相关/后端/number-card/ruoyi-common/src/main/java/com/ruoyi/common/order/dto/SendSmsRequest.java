package com.ruoyi.common.order.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 发送短信请求参数
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025-03-09 11:21
 */
@Data
public class SendSmsRequest {
    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private String userid;

    /**
     * 发送用户帐号
     */
    @ApiModelProperty("发送用户帐号")
    private String account;

    /**
     * 发送帐号密码
     */
    @ApiModelProperty("发送帐号密码")
    private String password;

    /**
     * 全部被叫号码
     */
    @ApiModelProperty("全部被叫号码")
    private String mobile;

    /**
     * 短信的内容
     */
    @ApiModelProperty("短信的内容")
    private String content;

    /**
     * 定时发送时间	为空表示立即发送，定时发送格式2010-10-24 09:08:10
     */
    @ApiModelProperty("定时发送时间\t为空表示立即发送，定时发送格式2010-10-24 09:08:10")
    private String sendTime;

    /**
     * 发送任务命令	设置为固定的:send
     */
    @ApiModelProperty("发送任务命令\t设置为固定的:send")
    private String action;

    /**
     * 是否检查内容包含非法关键字	当设置为1时表示需要检查，默认0为不检查
     */
    @ApiModelProperty("是否检查内容包含非法关键字\t当设置为1时表示需要检查，默认0为不检查")
    private String checkcontent;

}
