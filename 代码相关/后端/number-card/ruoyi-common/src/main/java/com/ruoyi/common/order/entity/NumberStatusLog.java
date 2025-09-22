package com.ruoyi.common.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 号码状态日志表
 *
 * @Description
 * @Author 陈思伟
 */
@TableName(value = "t_number_status_log", autoResultMap = true)
@Data
public class NumberStatusLog {

    /**
     * 号码状态日志表ID
     */
    @TableId(type = IdType.AUTO)
    private Integer numberStatusLogId;

    /**
     * 检测号码
     */
    @ApiModelProperty(value = "检测号码")
    private String phone;

    /**
     * 消息
     */
    @ApiModelProperty(value = "消息")
    private String message;

    /**
     * 请求类型
     * 请求类型 0 炫咖移动余额查询 1炫咖联通余额查询 2炫咖电信余额查询 3炫咖携号转网 4 炫咖号码查询 5 炫咖空号检测 6 额查查余额查询 7 额查查携号查询 8 额查查空号查询
     */
    @ApiModelProperty(value = "请求类型")
    private Integer requestType;

    /**
     * 请求url
     */
    @ApiModelProperty(value = "请求url")
    private String requestUrl;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private String requestBody;

    /**
     * 请求返回
     */
    @ApiModelProperty(value = "请求返回")
    private String requestMsg;

    /**
     * 号码余额
     */
    @ApiModelProperty(value = "号码余额")
    private String mobileFee;

    /**
     * 号码归属地
     */
    @ApiModelProperty(value = "号码归属地")
    private String area;

    /**
     * 号码归属省
     */
    @ApiModelProperty(value = "号码归属省")
    private String province;

    /**
     * 号码归属地
     */
    @ApiModelProperty(value = "号码归属市")
    private String city;

    /**
     * 原运营商名称
     */
    @ApiModelProperty(value = "原运营商名称")
    private String priIspName;

    /**
     * 现运营商名称
     */
    @ApiModelProperty(value = "现运营商名称")
    private String newIspName;

    /**
     * 是否携号转网，0：否；1:是
     */
    @ApiModelProperty(value = "是否携号转网，0：否；1:是 ")
    private Integer isChange;

    /**
     * 运营商类型
     */
    @ApiModelProperty(value = "运营商类型")
    private String numberType;

    /**
     * 状态名称
     */
    @ApiModelProperty(value = "状态名称")
    private String statusName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
}