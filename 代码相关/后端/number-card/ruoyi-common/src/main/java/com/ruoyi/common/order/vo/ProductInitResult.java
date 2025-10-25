package com.ruoyi.common.order.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品初始化结果
 */
@Data
public class ProductInitResult {

    /**
     * 新增的代理商品数量
     */
    private int created;

    /**
     * 更新的代理商品数量
     */
    private int updated;

    /**
     * 删除的代理商品数量
     */
    private int deleted;

    /**
     * 同步佣金次数
     */
    private int commissionSynced;

    /**
     * 同步海报次数
     */
    private int posterSynced;

    /**
     * 警告或提示信息
     */
    private List<String> warnings = new ArrayList<>();

    /**
     * 任务ID（预留异步处理）
     */
    private String taskId;

    public void addWarning(String warning) {
        if (warning == null) {
            return;
        }
        this.warnings.add(warning);
    }
}

