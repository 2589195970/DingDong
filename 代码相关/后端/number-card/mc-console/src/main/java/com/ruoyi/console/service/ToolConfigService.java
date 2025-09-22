package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.ToolConfig;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface ToolConfigService extends IService<ToolConfig> {


    /**
     * 根据类型获取配置
     *
     * @param
     * @return
     */
     ToolConfig getToolConfig(Integer toolConfigType) throws BizException;



    /**
     * 更新工具配置数据
     * @param
     * @return
     */
    void updateToolConfig(ToolConfig toolConfig) throws BizException;

    /**
     *  更新推广海报图
     * @param
     * @return
     */
    void updateRegisterQrcodeMap(ToolConfig toolConfig) throws BizException;

}
