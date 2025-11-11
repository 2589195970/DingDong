package com.ruoyi.console.service.impl.agent;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.AgentProduct;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.console.service.AgentAccountService;
import com.ruoyi.console.service.AgentProductInitService;
import com.ruoyi.console.service.AgentProductService;
import com.ruoyi.console.service.WithdrawalRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;



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

    private static final int CASCADE_BATCH_SIZE = 200;

    @Resource
    AgentProductService agentProductService;

    @Resource
    AgentAccountService agentAccountService;

    @Resource
    WithdrawalRecordService withdrawalRecordService;


    /**
     * 根据父代理商新增
     * 代理商账号注册默认产品
     * admin 需要判断默认上架开放产品
     */
    @Async("addAgentExecutor")
    public void addRegisterAccountProduct(AgentAccount agentAccount) throws BizException {
        agentProductService.addRegisterAccountProduct(agentAccount);
    }

    @Override
    @Async("addAgentExecutor")
    public void addWithdrawalRecordAsync(Long sysUserId, String agentCode) throws BizException {
        if (sysUserId == null || !StringUtils.hasText(agentCode)) {
            log.warn("Async task skipped: addWithdrawalRecordAsync invalid args sysUserId={} agentCode={}", sysUserId, agentCode);
            return;
        }
        try {
            withdrawalRecordService.addWithdrawalRecord(sysUserId, agentCode);
            log.info("Async task[success]: addWithdrawalRecord sysUserId={} agentCode={}", sysUserId, agentCode);
        } catch (BizException ex) {
            log.error("Async task[failed]: addWithdrawalRecord sysUserId={} agentCode={} error={}",
                    sysUserId,
                    agentCode,
                    ex.getMessage(),
                    ex);
            throw ex;
        }
    }


    /**
     * 生成代理商产品海报图
     */
    @Async("addAgentExecutor")
    public void addSubAgentProductPoster(AgentAccount agentAccount, Product product) throws BizException {
        if (agentAccount == null || product == null) {
            log.warn("Async task skipped: addSubAgentProductPoster missing input agentAccount={} product={}", agentAccount, product);
            return;
        }
        String agentCode = agentAccount.getAgentCode();
        String productCode = product.getProductCode();
        log.info("Async task[start]: addSubAgentProductPoster productCode={} agentCode={}", productCode, agentCode);
        try {
            agentProductService.addSubAgentProductPoster(agentAccount, product);
            log.info("Async task[success]: addSubAgentProductPoster productCode={} agentCode={}", productCode, agentCode);
        } catch (Exception ex) {
            log.error("Async task[failed]: addSubAgentProductPoster productCode={} agentCode={} error={}",
                    productCode, agentCode, ex.getMessage(), ex);
        }
    }

    /**
     * 生成产品海报图
     */
    @Async("addAgentExecutor")
    public void addProductPoster(AgentAccount agentAccount, Product product) {
        if (agentAccount == null || product == null) {
            log.warn("Async task skipped: addProductPoster missing input agentAccount={} product={}", agentAccount, product);
            return;
        }
        String agentCode = agentAccount.getAgentCode();
        String productCode = product.getProductCode();
        log.info("Async task[start]: addProductPoster productCode={} agentCode={}", productCode, agentCode);
        try {
            agentProductService.addProductPoster(agentAccount, product);
            log.info("Async task[success]: addProductPoster productCode={} agentCode={}", productCode, agentCode);
        } catch (Exception ex) {
            log.error("Async task[failed]: addProductPoster productCode={} agentCode={} error={}",
                    productCode, agentCode, ex.getMessage(), ex);
        }
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
    @Override
    @Async("addAgentExecutor")
    public void addSubAgentProduct(AgentAccount agentAccount,AgentAccount parentAgentAccount, Product product,Integer productCommission) throws BizException {
        if (agentAccount == null || product == null) {
            log.warn("Async task skipped: addSubAgentProduct missing input agentAccount={} product={}", agentAccount, product);
            return;
        }
        String agentCode = agentAccount.getAgentCode();
        String parentAgentCode = parentAgentAccount == null ? "UNKNOWN" : parentAgentAccount.getAgentCode();
        String productCode = product.getProductCode();
        log.info("Async task[start]: addSubAgentProduct productCode={} parentAgentCode={} targetAgentCode={}",
                productCode, parentAgentCode, agentCode);
        try {
            agentProductService.addSubAgentProduct(agentAccount,parentAgentAccount,product,productCommission);
            log.info("Async task[success]: addSubAgentProduct productCode={} parentAgentCode={} targetAgentCode={}",
                    productCode, parentAgentCode, agentCode);
        } catch (Exception ex) {
            log.error("Async task[failed]: addSubAgentProduct productCode={} parentAgentCode={} targetAgentCode={} error={}",
                    productCode, parentAgentCode, agentCode, ex.getMessage(), ex);
        }
    }

    /**
     * 批量创建代理商产品
     */
    @Override
    @Async("addAgentExecutor")
    public void addSubAgentProducts(Integer isAllAgent,
                                     List<String> targetAgentCodes,
                                     AgentAccount parentAgentAccount,
                                     Product product,
                                     Integer productCommission,
                                     LoginUser loginUser) throws BizException {
        String productCode = product == null ? "UNKNOWN" : product.getProductCode();
        String parentAgentCode = parentAgentAccount == null ? "UNKNOWN" : parentAgentAccount.getAgentCode();
        if (product == null || loginUser == null) {
            log.warn("Async task skipped: addSubAgentProducts missing required parameter product={} loginUser={}", product, loginUser);
            return;
        }
        List<AgentAccount> agentAccounts;
        if (Objects.equals(isAllAgent, BaseConstant.ONE_INT)) {
            agentAccounts = agentAccountService.selectChildAgentList(loginUser, BaseConstant.ZERO_INT);
        } else if (!CollectionUtils.isEmpty(targetAgentCodes)) {
            agentAccounts = agentAccountService.list(new LambdaQueryWrapper<AgentAccount>().in(AgentAccount::getAgentCode, targetAgentCodes));
        } else {
            agentAccounts = Collections.emptyList();
        }
        int targetSize = CollectionUtils.isEmpty(agentAccounts) ? 0 : agentAccounts.size();
        if (CollectionUtils.isEmpty(agentAccounts)) {
            log.info("Async task skipped: addSubAgentProducts productCode={} parentAgentCode={} targetCount={} (empty agent list)",
                    productCode, parentAgentCode, targetSize);
            return;
        }
        log.info("Async task[start]: addSubAgentProducts productCode={} parentAgentCode={} dispatchAll={} targetCount={}",
                productCode,
                parentAgentCode,
                Objects.equals(isAllAgent, BaseConstant.ONE_INT),
                targetSize);
        try {
            agentProductService.addSubAgentProducts(agentAccounts, parentAgentAccount, product, productCommission);
            log.info("Async task[success]: addSubAgentProducts productCode={} parentAgentCode={} processedCount={}",
                    productCode,
                    parentAgentCode,
                    targetSize);
        } catch (Exception ex) {
            log.error("Async task[failed]: addSubAgentProducts productCode={} parentAgentCode={} targetCount={} error={}",
                    productCode,
                    parentAgentCode,
                    targetSize,
                    ex.getMessage(),
                    ex);
        }
    }

    @Override
    @Async("addAgentExecutor")
    @Transactional(rollbackFor = Exception.class)
    public void asyncUpdateAgentProductStatus(String productCode,
                                              String parentAgentCode,
                                              Integer productStatus) throws BizException {
        if (!StringUtils.hasText(productCode) || !StringUtils.hasText(parentAgentCode) || productStatus == null) {
            log.warn("Async task skipped: asyncUpdateAgentProductStatus invalid args productCode={} parentAgentCode={} status={}",
                    productCode, parentAgentCode, productStatus);
            return;
        }
        long start = System.currentTimeMillis();
        try {
            int affectedAgents = cascadeAgentProductStatus(productCode, parentAgentCode, productStatus);
            log.info("Async task[success]: cascade agent product status productCode={} parentAgentCode={} status={} affectedAgents={} cost={}ms",
                    productCode,
                    parentAgentCode,
                    productStatus,
                    affectedAgents,
                    System.currentTimeMillis() - start);
        } catch (Exception ex) {
            log.error("Async task[failed]: cascade agent product status productCode={} parentAgentCode={} status={} error={}",
                    productCode,
                    parentAgentCode,
                    productStatus,
                    ex.getMessage(),
                    ex);
            throw ex;
        }
    }

    private int cascadeAgentProductStatus(String productCode,
                                          String rootParentAgentCode,
                                          Integer productStatus) {
        if (!StringUtils.hasText(rootParentAgentCode)) {
            return 0;
        }
        Set<String> visitedAgentCodes = new HashSet<>();
        List<String> currentLayer = new ArrayList<>();
        currentLayer.add(rootParentAgentCode);
        int affected = 0;

        while (!CollectionUtils.isEmpty(currentLayer)) {
            List<String> nextLayer = new ArrayList<>();
            for (List<String> batch : partitionList(currentLayer, CASCADE_BATCH_SIZE)) {
                List<String> effectiveParents = batch.stream()
                        .filter(StringUtils::hasText)
                        .filter(visitedAgentCodes::add)
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(effectiveParents)) {
                    continue;
                }
                long now = System.currentTimeMillis();
                agentProductService.lambdaUpdate()
                        .set(AgentProduct::getProductStatus, productStatus)
                        .set(AgentProduct::getUpdateTime, now)
                        .eq(AgentProduct::getParentProductCode, productCode)
                        .in(AgentProduct::getParentAgentCode, effectiveParents)
                        .update();
                affected += effectiveParents.size();

                List<AgentAccount> downstreamAgents = agentAccountService.list(
                        new LambdaQueryWrapper<AgentAccount>().in(AgentAccount::getParentAgentCode, effectiveParents));
                if (!CollectionUtils.isEmpty(downstreamAgents)) {
                    downstreamAgents.stream()
                            .map(AgentAccount::getAgentCode)
                            .filter(StringUtils::hasText)
                            .forEach(nextLayer::add);
                }
            }
            currentLayer = nextLayer;
        }
        return affected;
    }

    private <T> List<List<T>> partitionList(List<T> source, int batchSize) {
        if (CollectionUtils.isEmpty(source) || batchSize <= 0) {
            return Collections.emptyList();
        }
        List<List<T>> partitions = new ArrayList<>();
        int total = source.size();
        for (int i = 0; i < total; i += batchSize) {
            int end = Math.min(total, i + batchSize);
            partitions.add(source.subList(i, end));
        }
        return partitions;
    }
}
