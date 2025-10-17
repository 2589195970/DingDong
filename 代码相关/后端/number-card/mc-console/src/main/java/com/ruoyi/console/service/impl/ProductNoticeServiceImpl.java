package com.ruoyi.console.service.impl;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.console.service.ProductNoticeService;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 产品公告服务实现
 *
 * @Description 产品状态变更时自动创建公告
 * @Author Claude
 * @Date 2025/01/09
 */
@Service
@Slf4j
public class ProductNoticeServiceImpl implements ProductNoticeService {

    @Autowired
    private ISysNoticeService noticeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "产品状态变更公告", businessType = BusinessType.INSERT)
    public void createProductStatusNotice(Product product, Integer oldStatus, Integer newStatus, String operator) {
        // 只有状态真正发生变化时才创建公告
        if (!Objects.equals(oldStatus, newStatus)) {
            try {
                SysNotice notice = buildProductStatusNotice(product, newStatus, operator);
                int result = noticeService.insertNotice(notice);
                if (result > 0) {
                    log.info("产品{}状态变更公告创建成功，状态：{} -> {}", product.getProductName(), oldStatus, newStatus);
                } else {
                    log.warn("产品{}状态变更公告创建失败", product.getProductName());
                }
            } catch (Exception e) {
                log.error("创建产品状态变更公告异常，产品：{}, 错误：{}", product.getProductName(), e.getMessage(), e);
                // 公告创建失败不影响主流程，只记录日志
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "批量产品状态变更公告", businessType = BusinessType.INSERT)
    public void createBatchProductStatusNotice(List<Product> products, Integer newStatus, String operator) {
        if (products == null || products.isEmpty()) {
            return;
        }

        try {
            List<SysNotice> notices = products.stream()
                    .map(product -> buildProductStatusNotice(product, newStatus, operator))
                    .collect(Collectors.toList());

            int successCount = 0;
            for (SysNotice notice : notices) {
                try {
                    int result = noticeService.insertNotice(notice);
                    if (result > 0) {
                        successCount++;
                    }
                } catch (Exception e) {
                    log.warn("创建单个产品公告失败，标题：{}, 错误：{}", notice.getNoticeTitle(), e.getMessage());
                }
            }

            log.info("批量产品状态变更公告创建完成，成功：{}, 总数：{}", successCount, notices.size());
        } catch (Exception e) {
            log.error("批量创建产品状态变更公告异常，错误：{}", e.getMessage(), e);
        }
    }

    /**
     * 构建产品状态变更公告
     */
    private SysNotice buildProductStatusNotice(Product product, Integer status, String operator) {
        SysNotice notice = new SysNotice();

        // 设置公告基本信息
        notice.setNoticeTitle("产品上下架公告");
        notice.setNoticeType("2"); // 公告类型：2-公告
        notice.setStatus("0");    // 状态：0-正常

        // 构建公告内容
        String content = buildNoticeContent(product, status);
        notice.setNoticeContent(content);

        // 设置创建信息
        notice.setCreateBy(operator);
        notice.setCreateTime(new java.util.Date());

        return notice;
    }

    /**
     * 构建公告内容（HTML格式）- 适配手机端
     */
    private String buildNoticeContent(Product product, Integer status) {
        String statusText = status == 1 ? "已上架" : "已下架";
        String statusColor = status == 1 ? "#52c41a" : "#ff4d4f";
        String statusBgColor = status == 1 ? "#f6ffed" : "#fff2f0";
        String operTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return String.format(
            "<div class='product-notice' style='padding: 12px; border: 1px solid #d9d9d9; border-radius: 8px; margin: 8px 0; background: #fafafa; max-width: 100%%; box-sizing: border-box;'>" +
            // 响应式样式定义
            "<style>" +
            ".product-notice { " +
            "  padding: 12px !important; " +
            "  margin: 8px 0 !important; " +
            "  border-radius: 8px !important; " +
            "  background: #fafafa !important; " +
            "  border: 1px solid #d9d9d9 !important; " +
            "  max-width: 100%% !important; " +
            "  box-sizing: border-box !important; " +
            "} " +
            ".product-layout { " +
            "  display: flex !important; " +
            "  align-items: flex-start !important; " +
            "  gap: 12px !important; " +
            "} " +
            ".product-image { " +
            "  flex-shrink: 0 !important; " +
            "  width: 60px !important; " +
            "  height: 60px !important; " +
            "  overflow: hidden !important; " +
            "  border-radius: 6px !important; " +
            "  border: 1px solid #f0f0f0 !important; " +
            "  background: #fff !important; " +
            "  position: relative !important; " +
            "} " +
            ".product-info { " +
            "  flex: 1 !important; " +
            "  min-width: 0 !important; " +
            "} " +
            ".product-title { " +
            "  margin: 0 0 8px 0 !important; " +
            "  font-size: 15px !important; " +
            "  font-weight: 600 !important; " +
            "  color: #262626 !important; " +
            "  line-height: 1.3 !important; " +
            "  word-break: break-word !important; " +
            "} " +
            ".status-row { " +
            "  display: flex !important; " +
            "  align-items: center !important; " +
            "  gap: 6px !important; " +
            "  margin-bottom: 6px !important; " +
            "  flex-wrap: wrap !important; " +
            "} " +
            ".status-label { " +
            "  font-size: 13px !important; " +
            "  color: #595959 !important; " +
            "  white-space: nowrap !important; " +
            "} " +
            ".status-badge { " +
            "  font-size: 12px !important; " +
            "  font-weight: 500 !important; " +
            "  padding: 2px 8px !important; " +
            "  border-radius: 10px !important; " +
            "  white-space: nowrap !important; " +
            "} " +
            ".code-row, .time-row { " +
            "  margin-bottom: 4px !important; " +
            "} " +
            ".code-text { " +
            "  font-size: 12px !important; " +
            "  color: #8c8c8c !important; " +
            "  word-break: break-all !important; " +
            "} " +
            ".time-text { " +
            "  margin: 0 !important; " +
            "  font-size: 11px !important; " +
            "  color: #999 !important; " +
            "} " +
            ".no-image { " +
            "  width: 100%% !important; " +
            "  height: 100%% !important; " +
            "  background: #f5f5f5 !important; " +
            "  border-radius: 6px !important; " +
            "  display: flex !important; " +
            "  align-items: center !important; " +
            "  justify-content: center !important; " +
            "  color: #999 !important; " +
            "  font-size: 11px !important; " +
            "} " +

            // 手机端响应式优化
            "@media (max-width: 480px) { " +
            "  .product-notice { " +
            "    padding: 10px !important; " +
            "    margin: 6px 0 !important; " +
            "  } " +
            "  .product-layout { " +
            "    gap: 10px !important; " +
            "  } " +
            "  .product-image { " +
            "    width: 50px !important; " +
            "    height: 50px !important; " +
            "  } " +
            "  .product-title { " +
            "    font-size: 14px !important; " +
            "    margin-bottom: 6px !important; " +
            "  } " +
            "  .status-label { " +
            "    font-size: 12px !important; " +
            "  } " +
            "  .status-badge { " +
            "    font-size: 11px !important; " +
            "    padding: 1px 6px !important; " +
            "  } " +
            "  .code-text { " +
            "    font-size: 11px !important; " +
            "  } " +
            "  .time-text { " +
            "    font-size: 10px !important; " +
            "  } " +
            "} " +

            // 超小屏幕优化
            "@media (max-width: 360px) { " +
            "  .product-layout { " +
            "    flex-direction: column !important; " +
            "    align-items: center !important; " +
            "    text-align: center !important; " +
            "  } " +
            "  .product-image { " +
            "    width: 40px !important; " +
            "    height: 40px !important; " +
            "    margin-bottom: 8px !important; " +
            "  } " +
            "  .status-row { " +
            "    justify-content: center !important; " +
            "  } " +
            "} " +
            "</style>" +

            // HTML结构
            "<div class='product-layout'>" +
            "<div class='product-image'>" +
            "<img src='%s' alt='%s' width='60' height='60' style='width:60px !important;height:60px !important;max-width:60px !important;max-height:60px !important;min-width:60px !important;min-height:60px !important;object-fit:cover !important;display:block !important;border:none !important;margin:0 !important;padding:0 !important;box-sizing:border-box !important;' onerror=\"this.style.display='none'; this.parentElement.innerHTML='<div class=\\'no-image\\'>暂无图片</div>'\"/>" +
            "</div>" +
            "<div class='product-info'>" +
            "<h3 class='product-title'>%s</h3>" +
            "<div class='status-row'>" +
            "<span class='status-label'>状态变更：</span>" +
            "<span class='status-badge' style='color: %s; background: %s; border: 1px solid %s;'>%s</span>" +
            "</div>" +
            "<div class='code-row'>" +
            "<span class='code-text'>产品编码：%s</span>" +
            "</div>" +
            "<p class='time-text'>操作时间：%s</p>" +
            "</div>" +
            "</div>" +
            "</div>",
            product.getProductMasterMap(),     // 商品主图
            product.getProductName(),         // 商品名称（alt属性）
            product.getProductName(),         // 商品名称
            statusColor,                      // 状态文字颜色
            statusBgColor,                    // 状态背景色
            statusColor + "33",               // 状态边框颜色（透明度）
            statusText,                       // 状态描述
            product.getProductCode(),         // 产品编码
            operTime                          // 操作时间
        );
    }
}