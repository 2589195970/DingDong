package com.ruoyi.order.utils;

import cn.hutool.core.util.RandomUtil;
import com.ruoyi.common.order.entity.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/13 17:11
 */
@Slf4j
@Component
public class SnowflakeUtil {

    private SnowFlake snowFlake;

    @PostConstruct
    public void init() {
        int workerId = RandomUtil.randomInt(255);
        try {
            String worker_id = System.getProperty("worker_id");
            workerId = Integer.parseInt(worker_id);
        } catch (Exception e) {
            workerId = RandomUtil.randomInt(1, 100);
        }
        snowFlake = new SnowFlake(1, workerId);
    }

    public long getNextId() {
        return snowFlake.nextId();
    }

}
