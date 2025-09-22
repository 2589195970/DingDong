package com.ruoyi.console.service.impl.agent;


import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.console.service.AgentProductInitService;
import com.ruoyi.console.service.AgentProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;



/**
 * 代理商产品相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/1/24 17:13
 */
@Service
@Slf4j
public class AgentProductInitServiceImpl  implements AgentProductInitService {

    @Resource
    AgentProductService agentProductService;


    /**
     * 根据父代理商新增
     * 代理商账号注册默认产品
     * admin 需要判断默认上架开放产品
     */
    @Async("addAgentExecutor")
    public void addRegisterAccountProduct(AgentAccount agentAccount) throws BizException {
        agentProductService.addRegisterAccountProduct(agentAccount);
    }


    /**
     * 生成代理商产品海报图
     */
    @Async("addAgentExecutor")
    public void addSubAgentProductPoster(AgentAccount agentAccount, Product product) throws BizException {
        agentProductService.addSubAgentProductPoster(agentAccount,product);
    }

    /**
     * 生成产品海报图
     */
    @Async("addAgentExecutor")
    public void addProductPoster(AgentAccount agentAccount, Product product) throws BizException {
        agentProductService.addProductPoster(agentAccount,product);
    }

    /**
     * 生成推广海报图
     */
    @Async("addAgentExecutor")
    public void addRegisterQrcodeMap(AgentAccount agentAccount, String url, Integer number) throws BizException {
        agentProductService.addRegisterQrcodeMap(agentAccount,url,number);
    }

    /**
     * 计算代理商佣金
     * @param productCode
     * @param agentCode
     * @param commission
     * @throws BizException
     */
    @Override
    @Async("addAgentExecutor")
    public void updateAgentProductCommission(String productCode, String agentCode, Integer commission) throws BizException {
        agentProductService.updateAgentProductCommission(productCode,agentCode,commission);
    }

    /**
     * 删除代理下所有子代理的产品 包括本身
     */
    @Async("addAgentExecutor")
    public void deleteSubAgentProduct(AgentAccount agentAccount, Product product) throws BizException {
        agentProductService.deleteSubAgentProduct(agentAccount,product);
    }

    /**
     * 创建 代理商下所有子代理的产品 包括本身
     */
    @Async("addAgentExecutor")
    public void addSubAgentProduct(AgentAccount agentAccount,AgentAccount parentAgentAccount, Product product,Integer productCommission) throws BizException {
        agentProductService.addSubAgentProduct(agentAccount,parentAgentAccount,product,productCommission);
    }


}
