package com.ruoyi.console.service;

import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.Product;
import org.springframework.scheduling.annotation.Async;


/**
 * 代理产品初始化
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
public interface AgentProductInitService  {

    /**
     * 根据父代理商新增
     * 代理商账号注册默认产品
     *
     */
    void addRegisterAccountProduct(AgentAccount agentAccount) throws BizException;


    /**
     * 生成海报图
     */
    void addSubAgentProductPoster(AgentAccount agentAccount, Product product) throws BizException;

    /**
     * 产品海报图
     * @param agentAccount
     * @param product
     * @throws BizException
     */
    void addProductPoster(AgentAccount agentAccount, Product product) throws BizException;


    /**
     * 生成推广海报图
     */
    void addRegisterQrcodeMap(AgentAccount agentAccount, String url, Integer number) throws BizException;


    void updateAgentProductCommission(String productCode,String agentCode,Integer commission) throws BizException;


    /**
     * 删除代理下所有子代理的产品 包括本身
     */
    void deleteSubAgentProduct(AgentAccount agentAccount, Product product) throws BizException;


    /**
     * 创建 代理商下所有子代理的产品 包括本身
     */
    void addSubAgentProduct(AgentAccount agentAccount,AgentAccount parentAgentAccount, Product product,Integer productCommission) throws BizException;
}
