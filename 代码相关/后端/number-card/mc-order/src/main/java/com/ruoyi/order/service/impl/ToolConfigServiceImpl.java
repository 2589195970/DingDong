package com.ruoyi.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.ToolConfig;
import com.ruoyi.common.utils.CacheUtils;

import com.ruoyi.order.mapper.AgentAccountMapper;
import com.ruoyi.order.mapper.ToolConfigMapper;
import com.ruoyi.order.service.ToolConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2023/8/24 17:13
 */
@Service
@Slf4j
public class ToolConfigServiceImpl extends ServiceImpl<ToolConfigMapper, ToolConfig> implements ToolConfigService {

    @Resource(name = "configStringRedisTemplate")
    private StringRedisTemplate configStringRedisTemplate;

    /**
     * 根据类型获取配置
     *
     * @param
     * @return
     */
    @Override
    public ToolConfig getToolConfig(Integer toolConfigType) throws BizException {
        if (StrUtil.isBlankIfStr(toolConfigType)) {
            throw new BizException("toolConfigType 不存在:{}", toolConfigType);
        }
        ToolConfig toolConfig;
        // 注释掉缓存查询，直接从数据库查询
        // String cacheKey = CacheUtils.generalKey(CacheKeyConstants.TOOL_CONFIG, toolConfigType);
        // String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        // if (StrUtil.isBlankIfStr(json)) {
            toolConfig = baseMapper.selectOne(new LambdaQueryWrapper<ToolConfig>().eq(ToolConfig::getToolConfigType, toolConfigType));
            if (toolConfig == null) {
                throw new BizException("工具配置不存在:{}", toolConfig);
            }
            // 注释掉缓存写入
            // configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(toolConfig), 10, TimeUnit.MINUTES);
        // } else {
        //     toolConfig = JSONObject.parseObject(json, ToolConfig.class);
        // }
        return toolConfig;
    }


    /**
     * 更新工具配置数据
     * @param
     * @return
     */
    @Override
    public void updateToolConfig(ToolConfig toolConfig) throws BizException {
        if (toolConfig == null||toolConfig.getToolConfigId() == null) {
            throw new BizException("参数不能为空");
        }
        toolConfig.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(toolConfig);
        // 注释掉缓存删除
        // String cacheKey = CacheUtils.generalKey(CacheKeyConstants.TOOL_CONFIG, toolConfig.getToolConfigType());
        // configStringRedisTemplate.delete(cacheKey);
    }


}
