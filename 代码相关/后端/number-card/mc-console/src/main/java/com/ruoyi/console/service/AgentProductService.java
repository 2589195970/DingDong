package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.AgentProduct;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.vo.AgentOrderSelectVO;
import com.ruoyi.common.order.vo.AgentProductVO;

import java.util.List;


/**
 * 代理产品相关接口
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
public interface AgentProductService extends IService<AgentProduct> {



    /**
     * 代理商分页查询产品列表
     *
     * @return
     * @throws BizException
     */
    PageResult<AgentProductVO> agentSelectProductListPage(AgentProductSelectBO agentProductSelectBO, LoginUser loginUser) throws BizException;


    /**
     * 新增代理商产品
     * @param agentProduct
     */
    void addAgentProduct(AgentProduct agentProduct) throws BizException;

    /**
     * 根据父代理商新增
     * 代理商账号注册默认产品
     *
     */
     void addRegisterAccountProduct(AgentAccount agentAccount) throws BizException;

    /**
     * 删除代理商产品
     * @param agentProductId
     */
    void deleteAgentProduct(Integer agentProductId);

    /**
     * （已废弃）按旧逻辑级联刷新子代理佣金。
     */
    //void updateAgentProductCommission(AgentProductUpdateCommissionBO productUpdateStatusBO, LoginUser loginUser) throws BizException;

    /**
     * 修改代理商产品佣金，仅更新当前代理自身数据。
     */
     void updateAgentProductCommission(String productCode,String agentCode,Integer commission) throws BizException;


    /**
     * 代理商 下游分配产品
     * @return
     * @throws BizException
     */
    void updateAgentProduct(AgentProductUpdateBO agentProductUpdateBO, LoginUser loginUser) throws BizException;


    /**
     * 此方法会创建 代理商下所有子代理的产品包括本身
     * @param agentAccounts 多个待分配代理
     * @param parentAgentAccount 父级代理
     * @param product 产品
     * @param productCommission 父级佣金
     */
    void addSubAgentProducts(List<AgentAccount> agentAccounts, AgentAccount parentAgentAccount, Product product, Integer productCommission) throws BizException;

    /**
     * 此方法会创建 代理商下所有子代理的产品包括本身
     * @param agentAccount
     * @param product
     */
    void addSubAgentProduct(AgentAccount agentAccount,AgentAccount parentAgentAccount, Product product,Integer productCommission) throws BizException;


    /**
     * 此方法会删除代理下所有子代理的产品 包括本身
     * @param product
     */
    void deleteSubAgentProduct(AgentAccount agentAccount, Product product);


    /**
     * 此方法会生成 代理商下所有子代理的产品海报图
     * @param agentAccount
     * @param product
     */
    void addSubAgentProductPoster(AgentAccount agentAccount, Product product) throws BizException;

    /**
     * 生成产品海报图
     * @return
     */
    void addProductPoster(AgentAccount agentAccount, Product product);


    /**
     * 生成推广海报图
     * @return
     */
    void addRegisterQrcodeMap(AgentAccount agentAccount,String url, Integer number) throws BizException;
}
