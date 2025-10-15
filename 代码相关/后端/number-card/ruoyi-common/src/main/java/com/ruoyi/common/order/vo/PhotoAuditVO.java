package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 照片审核信息视图对象
 *
 * @author 陈思伟
 * @date 2024/12/12
 */
@ApiModel("照片审核信息视图对象")
@Data
public class PhotoAuditVO {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderDownstreamId;

    @ApiModelProperty("用户姓名")
    private String cardName;

    @ApiModelProperty("用户手机号")
    private String cardPhone;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("身份证正面照片URL")
    private String idCardFrontUrl;

    @ApiModelProperty("身份证反面照片URL")
    private String idCardBackUrl;

    @ApiModelProperty("免冠照片URL")
    private String personPhotoUrl;

    @ApiModelProperty("自定义照片URL")
    private String customPhotoUrl;

    @ApiModelProperty("照片审核状态")
    private Integer photoStatus;

    @ApiModelProperty("照片审核状态名称")
    private String photoStatusName;

    @ApiModelProperty("照片上传时间")
    private Long photoUploadTime;

    @ApiModelProperty("照片审核员ID")
    private Long photoAuditUserId;

    @ApiModelProperty("照片审核员姓名")
    private String photoAuditUserName;

    @ApiModelProperty("照片审核时间")
    private Long photoAuditTime;

    @ApiModelProperty("照片审核备注")
    private String photoAuditRemark;

    @ApiModelProperty("代理商名称")
    private String downstreamName;

    @ApiModelProperty("订单创建时间")
    private Long createTime;

    @ApiModelProperty("照片审核历史记录")
    private List<PhotoAuditHistoryVO> auditHistory;
}