package com.ruoyi.console.service.photo;

import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.console.service.UpstreamApiService;
import com.ruoyi.console.service.photo.adapter.PhotoUploadAdapter;
import com.ruoyi.console.service.photo.dto.PhotoUploadRequest;
import com.ruoyi.console.service.photo.dto.PhotoUploadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 照片上传服务
 * 主要业务逻辑，负责协调各个适配器进行照片上传
 * 基于实际mc-order代码模式实现
 *
 * @author Claude
 * @date 2025/01/17
 */
@Slf4j
@Service
public class PhotoUploadService {

    @Autowired
    private List<PhotoUploadAdapter> adapters;

    @Resource
    private UpstreamApiService upstreamApiService;

    /**
     * 根据订单和产品信息上传照片
     *
     * @param order   订单信息
     * @param product 产品信息
     * @return 上传结果
     */
    public PhotoUploadResult uploadPhotos(Order order, Product product) {
        log.info("开始处理照片上传，订单ID: {}, 上游API: {}", order.getOrderId(), order.getUpstreamApi());

        try {
            // 1. 检查是否需要上传照片
            if (!shouldUploadPhotos(order, product)) {
                log.info("订单{} 不需要上传照片", order.getOrderId());
                return PhotoUploadResult.success("无需上传照片");
            }

            // 2. 构建上传请求
            PhotoUploadRequest request = buildUploadRequest(order, product);
            if (!request.hasPhotos()) {
                log.warn("订单{} 没有照片需要上传", order.getOrderId());
                return PhotoUploadResult.success("没有照片需要上传");
            }

            // 3. 选择合适的适配器
            PhotoUploadAdapter adapter = findAdapter(order.getUpstreamApi());
            if (adapter == null) {
                return PhotoUploadResult.failure("不支持的供应商: " + order.getUpstreamApi(), "UNSUPPORTED_PROVIDER");
            }

            // 4. 执行上传
            log.info("使用适配器{}为订单{}上传照片", adapter.getAdapterName(), order.getOrderId());
            return adapter.uploadPhotos(request);

        } catch (Exception e) {
            log.error("照片上传服务异常，订单ID: {}, 错误: {}", order.getOrderId(), e.getMessage(), e);
            return PhotoUploadResult.failure("服务异常: " + e.getMessage(), "SERVICE_ERROR");
        }
    }

    /**
     * 检查是否需要上传照片
     */
    private boolean shouldUploadPhotos(Order order, Product product) {
        // 1. 检查产品是否需要照片审核
        if (product.getPhotoRequired() == null || product.getPhotoRequired() != 1) {
            log.debug("订单{} 对应产品不需要照片审核", order.getOrderId());
            return false;
        }

        // 2. 检查订单是否有照片信息
        if (order.getIdCardFrontUrl() == null && order.getIdCardBackUrl() == null &&
            order.getPersonPhotoUrl() == null && order.getCustomPhotoUrl() == null) {
            log.debug("订单{} 没有照片信息", order.getOrderId());
            return false;
        }

        return true;
    }

  
    /**
     * 构建照片上传请求
     */
    private PhotoUploadRequest buildUploadRequest(Order order, Product product) {
        PhotoUploadRequest request = new PhotoUploadRequest();
        request.setOrderId(order.getOrderId());
        request.setUpstreamApi(order.getUpstreamApi());
        request.setProductCode(product.getProductCode());
        request.setIdCardFrontUrl(order.getIdCardFrontUrl());
        request.setIdCardBackUrl(order.getIdCardBackUrl());
        request.setPersonPhotoUrl(order.getPersonPhotoUrl());
        request.setCustomPhotoUrl(order.getCustomPhotoUrl());

        return request;
    }

    /**
     * 根据上游API代码查找合适的适配器
     */
    private PhotoUploadAdapter findAdapter(String upstreamApi) {
        if (adapters == null || adapters.isEmpty()) {
            log.error("没有注册任何照片上传适配器");
            return null;
        }

        for (PhotoUploadAdapter adapter : adapters) {
            if (adapter.supports(upstreamApi)) {
                log.debug("找到适配器: {} 支持供应商: {}", adapter.getAdapterName(), upstreamApi);
                return adapter;
            }
        }

        log.error("未找到支持供应商{}的适配器", upstreamApi);
        return null;
    }

    /**
     * 获取所有支持的供应商
     */
    public String[] getSupportedProviders() {
        if (adapters == null || adapters.isEmpty()) {
            return new String[0];
        }

        return adapters.stream()
                .flatMap(adapter -> java.util.Arrays.stream(adapter.getSupportedProviders()))
                .distinct()
                .toArray(String[]::new);
    }
}