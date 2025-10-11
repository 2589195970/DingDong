package com.ruoyi.console.service;

import com.ruoyi.common.order.entity.Product;
import java.util.List;

/**
 * 产品公告服务接口
 *
 * @Description 产品状态变更时自动创建公告
 * @Author Claude
 * @Date 2025/01/09
 */
public interface ProductNoticeService {

    /**
     * 创建产品上下架公告
     * @param product 产品信息
     * @param oldStatus 原状态 (0:下架, 1:上架)
     * @param newStatus 新状态 (0:下架, 1:上架)
     * @param operator 操作人
     */
    void createProductStatusNotice(Product product, Integer oldStatus, Integer newStatus, String operator);

    /**
     * 批量创建产品上下架公告
     * @param products 产品列表
     * @param newStatus 新状态
     * @param operator 操作人
     */
    void createBatchProductStatusNotice(List<Product> products, Integer newStatus, String operator);
}