package com.ruoyi.console.service.photo.dto;

import lombok.Data;

/**
 * 照片上传结果对象
 *
 * @author Claude
 * @date 2025/01/17
 */
@Data
public class PhotoUploadResult {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 上传时间戳
     */
    private long timestamp;

    /**
     * 重试次数
     */
    private int retryCount = 0;

    public PhotoUploadResult() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 创建成功结果
     */
    public static PhotoUploadResult success() {
        return success("上传成功", null);
    }

    /**
     * 创建成功结果
     */
    public static PhotoUploadResult success(String message) {
        return success(message, null);
    }

    /**
     * 创建成功结果
     */
    public static PhotoUploadResult success(String message, Object data) {
        PhotoUploadResult result = new PhotoUploadResult();
        result.setSuccess(true);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 创建失败结果
     */
    public static PhotoUploadResult failure(String message) {
        return failure(message, null, null);
    }

    /**
     * 创建失败结果
     */
    public static PhotoUploadResult failure(String message, String errorCode) {
        return failure(message, errorCode, null);
    }

    /**
     * 创建失败结果
     */
    public static PhotoUploadResult failure(String message, String errorCode, Object data) {
        PhotoUploadResult result = new PhotoUploadResult();
        result.setSuccess(false);
        result.setMessage(message);
        result.setErrorCode(errorCode);
        result.setData(data);
        return result;
    }

    /**
     * 增加重试次数
     */
    public PhotoUploadResult incrementRetry() {
        this.retryCount++;
        return this;
    }
}