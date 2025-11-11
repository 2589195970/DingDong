package com.ruoyi.console.service;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.Product;
import java.util.List;


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

    /**
     * 批量创建 代理商下所有子代理的产品 包括本身（异步）
     */
    void addSubAgentProducts(Integer isAllAgent,
                             List<String> targetAgentCodes,
                             AgentAccount parentAgentAccount,
                             Product product,
                             Integer productCommission,
                             LoginUser loginUser) throws BizException;

    /**
     * 异步更新指定代理及其下游代理的产品上下架状态
     */
    void asyncUpdateAgentProductStatus(String productCode,
                                       String parentAgentCode,
                                       Integer productStatus) throws BizException;

    /**
     * 异步创建提现记录
     */
    void addWithdrawalRecordAsync(Long sysUserId, String agentCode) throws BizException;
}
