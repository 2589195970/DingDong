package com.ruoyi.console.task;

import com.ruoyi.console.service.AgentRankingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 代理商排行榜缓存定时任务
 *
 * @author ruoyi
 */
@Component
public class AgentRankingCacheTask {

    private static final Logger log = LoggerFactory.getLogger(AgentRankingCacheTask.class);

    @Autowired
    private AgentRankingService agentRankingService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * 每小时刷新日榜缓存（每小时的第1分钟执行）
     */
    @Scheduled(cron = "0 1 * * * ?")
    @CacheEvict(value = "agentDailyRanking", allEntries = true)
    public void refreshDailyRankingCache() {
        log.info("开始刷新代理商日榜缓存...");

        try {
            // 预热主要的排行榜数据（TOP10）
            for (int rankingType = 1; rankingType <= 4; rankingType++) {
                agentRankingService.getDailyRanking(rankingType, 10, null);
            }

            log.info("代理商日榜缓存刷新完成");
        } catch (Exception e) {
            log.error("代理商日榜缓存刷新失败", e);
        }
    }

    /**
     * 每天凌晨1点刷新月榜缓存
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @CacheEvict(value = "agentMonthlyRanking", allEntries = true)
    public void refreshMonthlyRankingCache() {
        log.info("开始刷新代理商月榜缓存...");

        try {
            // 预热主要的排行榜数据（TOP10）
            for (int rankingType = 1; rankingType <= 4; rankingType++) {
                agentRankingService.getMonthlyRanking(rankingType, 10, null);
            }

            log.info("代理商月榜缓存刷新完成");
        } catch (Exception e) {
            log.error("代理商月榜缓存刷新失败", e);
        }
    }

    /**
     * 每天凌晨2点清理过期缓存
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void clearExpiredCache() {
        log.info("开始清理过期缓存...");

        try {
            // 清理自定义查询缓存等
            if (cacheManager.getCache("agentCustomRanking") != null) {
                cacheManager.getCache("agentCustomRanking").clear();
            }

            log.info("过期缓存清理完成");
        } catch (Exception e) {
            log.error("过期缓存清理失败", e);
        }
    }
}