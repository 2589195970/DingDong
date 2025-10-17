package com.ruoyi.console.service.photo;

import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.console.service.photo.dto.PhotoUploadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * 异步照片上传服务
 * 提供异步上传功能，避免阻塞主业务流程
 *
 * @author Claude
 * @date 2025/01/17
 */
@Slf4j
@Service
public class AsyncPhotoUploadService {

    @Autowired
    private PhotoUploadService photoUploadService;

    /**
     * 异步上传照片
     *
     * @param order   订单信息
     * @param product 产品信息
     * @return CompletableFuture
     */
    @Async("photoUploadExecutor")
    public CompletableFuture<PhotoUploadResult> uploadPhotosAsync(Order order, Product product) {
        log.info("开始异步上传照片，订单ID: {}", order.getOrderId());

        try {
            PhotoUploadResult result = photoUploadService.uploadPhotos(order, product);

            if (result.isSuccess()) {
                log.info("异步照片上传成功，订单ID: {}, 消息: {}", order.getOrderId(), result.getMessage());
            } else {
                log.warn("异步照片上传失败，订单ID: {}, 错误: {}, 错误码: {}",
                        order.getOrderId(), result.getMessage(), result.getErrorCode());
            }

            return CompletableFuture.completedFuture(result);

        } catch (Exception e) {
            log.error("异步照片上传异常，订单ID: {}, 错误: {}", order.getOrderId(), e.getMessage(), e);
            PhotoUploadResult errorResult = PhotoUploadResult.failure("异步上传异常: " + e.getMessage(), "ASYNC_ERROR");
            CompletableFuture<PhotoUploadResult> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new RuntimeException(errorResult.getMessage(), e));
            return failedFuture;
        }
    }

    /**
     * 异步上传照片（带重试）
     *
     * @param order   订单信息
     * @param product 产品信息
     * @param maxRetries 最大重试次数
     * @return CompletableFuture
     */
    @Async("photoUploadExecutor")
    public CompletableFuture<PhotoUploadResult> uploadPhotosWithRetryAsync(Order order, Product product, int maxRetries) {
        log.info("开始异步上传照片（带重试），订单ID: {}, 最大重试次数: {}", order.getOrderId(), maxRetries);

        return uploadPhotosWithRetry(order, product, maxRetries, 0);
    }

    /**
     * 递归重试上传
     */
    private CompletableFuture<PhotoUploadResult> uploadPhotosWithRetry(Order order, Product product, int maxRetries, int currentRetry) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return photoUploadService.uploadPhotos(order, product);
            } catch (Exception e) {
                log.error("照片上传失败，当前重试次数: {}/{}, 订单ID: {}, 错误: {}",
                        currentRetry, maxRetries, order.getOrderId(), e.getMessage());
                throw new RuntimeException(e);
            }
        }).thenCompose(result -> {
            if (result.isSuccess()) {
                return CompletableFuture.completedFuture(result);
            } else if (currentRetry < maxRetries) {
                log.warn("照片上传失败，准备重试，当前重试次数: {}/{}, 订单ID: {}, 错误: {}",
                        currentRetry, maxRetries, order.getOrderId(), result.getMessage());

                // 指数退避等待
                try {
                    Thread.sleep(1000L * (currentRetry + 1));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    CompletableFuture<PhotoUploadResult> failedFuture = new CompletableFuture<>();
                    failedFuture.completeExceptionally(e);
                    return failedFuture;
                }

                return uploadPhotosWithRetry(order, product, maxRetries, currentRetry + 1);
            } else {
                log.error("照片上传重试次数已达上限，订单ID: {}, 最终错误: {}", order.getOrderId(), result.getMessage());
                result.setRetryCount(currentRetry);
                return CompletableFuture.completedFuture(result);
            }
        }).exceptionally(throwable -> {
            log.error("照片上传异常，重试次数: {}/{}, 订单ID: {}, 异常: {}",
                    currentRetry, maxRetries, order.getOrderId(), throwable.getMessage());

            if (currentRetry < maxRetries) {
                // 指数退避等待
                try {
                    Thread.sleep(1000L * (currentRetry + 1));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return PhotoUploadResult.failure("上传被中断: " + e.getMessage(), "INTERRUPTED");
                }

                return uploadPhotosWithRetry(order, product, maxRetries, currentRetry + 1).join();
            } else {
                return PhotoUploadResult.failure("重试次数已达上限: " + throwable.getMessage(), "MAX_RETRIES_EXCEEDED");
            }
        });
    }

    /**
     * 仅记录上传结果（不影响主流程）
     *
     * @param order   订单信息
     * @param product 产品信息
     */
    @Async("photoUploadExecutor")
    public void uploadPhotosAndLog(Order order, Product product) {
        try {
            PhotoUploadResult result = photoUploadService.uploadPhotos(order, product);

            if (result.isSuccess()) {
                log.info("照片上传成功并记录日志，订单ID: {}", order.getOrderId());
            } else {
                log.error("照片上传失败并记录日志，订单ID: {}, 错误: {}", order.getOrderId(), result.getMessage());
            }

            // 这里可以添加更多的日志记录，比如记录到数据库、发送通知等

        } catch (Exception e) {
            log.error("照片上传记录异常，订单ID: {}, 错误: {}", order.getOrderId(), e.getMessage(), e);
        }
    }
}