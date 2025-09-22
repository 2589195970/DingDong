package com.ruoyi.common.apis.douyin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author 陈思伟
 * @version 1.0
 * @description: 商家订单回传请求类
 * @date 2025/7/18 10:40
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class DouDianOrderReviewRequest extends DouDianBaseRequest {
    /**
     * 订单审核的类型
     * 必传
     */
    @NotNull(message ="订单审核状态不能为空" )
    private Long taskType;

    /**
     * 审核状态码
     * 必传
     */
    @NotNull(message ="订单审核状态不能为空" )
    private Long rejectCode;

    /**
     * 审核的单id
     * 必传
     */
    @NotNull(message ="审核的单id不能为空" )
    private String objectId;
}
