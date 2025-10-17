package com.ruoyi.console.service.photo.adapter;

import com.ruoyi.console.service.photo.dto.PhotoUploadRequest;
import com.ruoyi.console.service.photo.dto.PhotoUploadResult;

/**
 * 照片上传适配器接口
 * 用于支持不同的上游供应商
 * 基于实际mc-order代码模式实现
 *
 * @author Claude
 * @date 2025/01/17
 */
public interface PhotoUploadAdapter {

    /**
     * 检查是否支持指定的上游供应商
     *
     * @param upstreamApi 上游API代码
     * @return 是否支持
     */
    boolean supports(String upstreamApi);

    /**
     * 上传照片到上游供应商
     *
     * @param request 照片上传请求
     * @return 上传结果
     */
    PhotoUploadResult uploadPhotos(PhotoUploadRequest request);

    /**
     * 获取适配器名称
     *
     * @return 适配器名称
     */
    String getAdapterName();

    /**
     * 获取支持的供应商代码列表
     *
     * @return 支持的供应商代码列表
     */
    String[] getSupportedProviders();
}