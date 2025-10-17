package com.ruoyi.console.service.photo.dto;

import lombok.Data;

/**
 * 照片上传请求对象
 *
 * @author Claude
 * @date 2025/01/17
 */
@Data
public class PhotoUploadRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 上游API代码
     */
    private String upstreamApi;

    /**
     * 产品代码
     */
    private String productCode;

    /**
     * 身份证正面照片URL
     */
    private String idCardFrontUrl;

    /**
     * 身份证背面照片URL
     */
    private String idCardBackUrl;

    /**
     * 手持身份证照片URL
     */
    private String personPhotoUrl;

    /**
     * 自定义照片URL（如学生证）
     */
    private String customPhotoUrl;

    /**
     * 检查是否有照片需要上传
     */
    public boolean hasPhotos() {
        return (idCardFrontUrl != null && !idCardFrontUrl.trim().isEmpty()) ||
               (idCardBackUrl != null && !idCardBackUrl.trim().isEmpty()) ||
               (personPhotoUrl != null && !personPhotoUrl.trim().isEmpty()) ||
               (customPhotoUrl != null && !customPhotoUrl.trim().isEmpty());
    }

    /**
     * 检查是否有必要的身份证照片
     */
    public boolean hasRequiredPhotos() {
        return (idCardFrontUrl != null && !idCardFrontUrl.trim().isEmpty()) &&
               (idCardBackUrl != null && !idCardBackUrl.trim().isEmpty()) &&
               (personPhotoUrl != null && !personPhotoUrl.trim().isEmpty());
    }
}