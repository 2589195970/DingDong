package com.ruoyi.console.service.impl.agent;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentProductSelectBO;
import com.ruoyi.common.order.bo.AgentProductUpdateBO;
import com.ruoyi.common.order.bo.AgentProductUpdateCommissionBO;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.vo.AgentProductVO;
import com.ruoyi.console.mapper.AgentProductMapper;
import com.ruoyi.console.mapper.ProductMapper;
import com.ruoyi.console.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 代理商产品相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/1/24 17:13
 */
@Service
@Slf4j
public class AgentProductServiceImpl extends ServiceImpl<AgentProductMapper, AgentProduct> implements AgentProductService {


    @Resource
    ProductMapper productMapper;

    @Resource(name = "configStringRedisTemplate")
    StringRedisTemplate configStringRedisTemplate;

    @Resource
    AgentAccountService agentAccountService;

    @Resource
    CommissionConfigService commissionConfigService;

    @Resource
    QrCodeService qrCodeService;

    @Resource
    PictureService pictureService;

    @Value(value = "${submit.h5}")
    private String h5url;

    @Resource
    AgentProductInitService agentProductInitService;

    @Resource
    ToolConfigService toolConfigService;

    /**
     * 代理商分页产品查询
     *
     * @param agentProductSelectBO
     * @param loginUser
     * @return
     * @throws BizException
     */
    @Override
    public PageResult<AgentProductVO> agentSelectProductListPage(AgentProductSelectBO agentProductSelectBO, LoginUser loginUser) throws BizException {
        //获取代理账户信息
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        //查询总数
        Integer totalRows = productMapper.selectAgentProductListCount(agentProductSelectBO.getOperatorType(), agentProductSelectBO.getProductStatus(), agentProductSelectBO.getProductType(), agentProductSelectBO.getProductName(), agentAccount.getAgentCode());
        if (totalRows == null || totalRows == BaseConstant.ZERO_INT) {
            return PageResultFactory.createPageResult(new ArrayList<>(), 0L, agentProductSelectBO.getPageSize(), agentProductSelectBO.getPageNo());
        }
        //分页查询代理商产品
        List<AgentProductVO> agentProductSelectVOS = productMapper.selectAgentProductList(agentProductSelectBO.getOperatorType(), agentProductSelectBO.getProductStatus(), agentProductSelectBO.getProductType(),
                agentProductSelectBO.getProductName(), agentAccount.getAgentCode(), (agentProductSelectBO.getPageNo() - 1) * agentProductSelectBO.getPageSize(), agentProductSelectBO.getPageSize());
        for(AgentProductVO agentProductVO:agentProductSelectVOS){
            StringBuffer stringBuffer = new StringBuffer(h5url);
            stringBuffer.append("?productCode=").append(agentProductVO.getProductCode()).append("&agentCode=").append(agentAccount.getAgentCode());
            agentProductVO.setH5Url(stringBuffer.toString());
            List<AgentProduct> agentProductList = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,agentProductVO.getProductCode()).eq(AgentProduct::getParentAgentCode,agentAccount.getAgentCode()));
            if(!CollectionUtils.isEmpty(agentProductList)){
                List<String> agentCodeList = agentProductList.stream().map(t -> t.getAgentCode()).collect(Collectors.toList());
                agentProductVO.setAgentCodeList(agentCodeList);
            }else {
                agentProductVO.setAgentCodeList(new ArrayList<>());
            }
        }
        return PageResultFactory.createPageResult(agentProductSelectVOS, Long.valueOf(totalRows), agentProductSelectBO.getPageSize(), agentProductSelectBO.getPageNo());
    }


    /**
     * 新增代理商产品记录
     *
     * @param agentProduct
     */
    public void addAgentProduct(AgentProduct agentProduct) throws BizException {
        if (agentProduct == null) {
            throw new BizException("参数不能为空");
        }
        if (agentProduct.getProductStatus() == null) {
            agentProduct.setProductStatus(BaseConstant.ONE_INT);
        }
        if (agentProduct.getProductCommission() == null) {
            agentProduct.setProductCommission(BaseConstant.ZERO_INT);
        }
        if (agentProduct.getDistributionProductCommission() == null) {
            agentProduct.setDistributionProductCommission(BaseConstant.ZERO_INT);
        }
        if (agentProduct.getRevenueProductCommission() == null) {
            agentProduct.setRevenueProductCommission(agentProduct.getProductCommission()-agentProduct.getDistributionProductCommission());
        }
        if (agentProduct.getIsAllAgent() == null) {
            agentProduct.setIsAllAgent(BaseConstant.ZERO_INT);
        }
        agentProduct.setProductSort(BaseConstant.ZERO_INT);
        agentProduct.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(agentProduct);
    }

    /**
     * 根据父代理商新增
     * 代理商账号注册默认产品
     * admin 需要判断默认上架开放产品
     */
    public void addRegisterAccountProduct(AgentAccount agentAccount) throws BizException {
        AgentAccount parentAgent = agentAccountService.getAgentAccountByCode(agentAccount.getParentAgentCode(),false);
        if(parentAgent.getSysUserId() == 1L){
            //查询所有上架产品
            List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getIsAllAgent,BaseConstant.ONE_INT).eq(Product::getProductStatus,BaseConstant.ONE_INT));
            if(CollectionUtils.isEmpty(productList)){
                return;
            }
            //添加默认产品
            for(Product product:productList){
                AgentProduct addAgentProduct = new AgentProduct();
                addAgentProduct.setParentProductCode(product.getProductCode());
                addAgentProduct.setParentProductName(product.getProductName());
                addAgentProduct.setParentAgentCode(parentAgent.getAgentCode());
                addAgentProduct.setAgentCode(agentAccount.getAgentCode());
                addAgentProduct.setAgentName(agentAccount.getAgentName());
                addAgentProduct.setProductStatus(BaseConstant.ONE_INT);
                addAgentProduct.setProductCommission(product.getProductCommission());
                addAgentProduct.setDistributionProductCommission(BaseConstant.ZERO_INT);
                addAgentProduct.setRevenueProductCommission(addAgentProduct.getProductCommission() - addAgentProduct.getDistributionProductCommission());
                addAgentProduct(addAgentProduct);
                //生成海报图
                addSubAgentProductPoster(agentAccount,product);
            }

            return;
        }
        //查询父代理的产品 只要上架的
        List<AgentProduct> agentProductList = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getAgentCode,agentAccount.getParentAgentCode()).eq(AgentProduct::getProductStatus,BaseConstant.ONE_INT));
        if(CollectionUtils.isEmpty(agentProductList)){
            return;
        }
        //添加默认产品
        for(AgentProduct agentProduct:agentProductList){
            AgentProduct addAgentProduct = new AgentProduct();
            addAgentProduct.setParentProductCode(agentProduct.getParentProductCode());
            addAgentProduct.setParentProductName(agentProduct.getParentProductName());
            addAgentProduct.setParentAgentCode(agentProduct.getAgentCode());
            addAgentProduct.setAgentCode(agentAccount.getAgentCode());
            addAgentProduct.setAgentName(agentAccount.getAgentName());
            addAgentProduct.setProductStatus(agentProduct.getProductStatus());
            addAgentProduct.setProductCommission(agentProduct.getDistributionProductCommission());
            addAgentProduct.setDistributionProductCommission(BaseConstant.ZERO_INT);
            addAgentProduct.setRevenueProductCommission(addAgentProduct.getProductCommission() - addAgentProduct.getDistributionProductCommission());
            addAgentProduct(addAgentProduct);
            //重新计算佣金
            updateAgentProductCommission(agentProduct.getParentProductCode(),agentProduct.getAgentCode(),agentProduct.getProductCommission());
            //生成海报图
            Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>().eq(Product::getProductCode,agentProduct.getParentProductCode()));
            addSubAgentProductPoster(agentAccount,product);
        }

    }


    /**
     * 删除代理商产品
     */
    public void deleteAgentProduct(Integer agentProductId) {
        baseMapper.deleteById(agentProductId);

    }


    /**
     * 修改子代理商 产品佣金
     * 佣金需全部重新计算
     */
    public void updateAgentProductCommission(String productCode, String agentCode, Integer commission) throws BizException {
        AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(agentCode,true);
        //更新自身佣金
        AgentProduct agentProduct = baseMapper.selectOne(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,productCode).eq(AgentProduct::getAgentCode,agentAccount.getAgentCode()));
        if(agentProduct==null){
            return;
        }
        //查询佣金保留多少
        Integer distributionProductCommission = commissionConfigService.computeCommission(agentCode, commission);
        agentProduct.setProductCommission(commission);
        agentProduct.setDistributionProductCommission(distributionProductCommission);
        agentProduct.setRevenueProductCommission(agentProduct.getProductCommission() - agentProduct.getDistributionProductCommission());
        agentProduct.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(agentProduct);
        //查询下游所有子代理 重新计算佣金
        List<AgentAccount> childAgentAccountList = agentAccountService.list(new LambdaQueryWrapper<AgentAccount>().like(AgentAccount::getParentAgentCode,agentAccount.getAgentCode()));
        if(!CollectionUtils.isEmpty(childAgentAccountList)){
            for (AgentAccount childAgentAccount:childAgentAccountList){
                agentProductInitService.updateAgentProductCommission(productCode,childAgentAccount.getAgentCode(),agentProduct.getDistributionProductCommission());
            }
        }
    }




    /**
     * 代理商 分配产品下游
     * @return
     * @throws BizException
     */
    @Override
    public void updateAgentProduct(AgentProductUpdateBO agentProductUpdateBO, LoginUser loginUser) throws BizException {
        if(agentProductUpdateBO==null||agentProductUpdateBO.getProductId()==null){
            throw new BizException("产品ID不能为空");
        }
        Product product = productMapper.selectById(agentProductUpdateBO.getProductId());
        if(product==null){
            throw new BizException("产品{}不存在",agentProductUpdateBO.getProductId());
        }

        //判断是否新增或删除产品
        AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        //传过来是空值 只需要考虑删除即可
        if(CollectionUtils.isEmpty(agentProductUpdateBO.getAgentCodeList())){
            List<AgentProduct> agentProductList = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()).eq(AgentProduct::getParentAgentCode,parentAgentAccount.getAgentCode()));
            for(AgentProduct agentProduct:agentProductList){
                deleteAgentProduct(agentProduct.getAgentProductId());
            }
        }
        //不为空 计算差值 新增或删除产品
        if(!CollectionUtils.isEmpty(agentProductUpdateBO.getAgentCodeList())){
            //转为map
            List<AgentProduct> agentProductList = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()).eq(AgentProduct::getParentAgentCode,parentAgentAccount.getAgentCode()));
            Map<String,AgentProduct> agentProductListMap = agentProductList.stream().collect(Collectors.toMap(AgentProduct::getAgentCode, Function.identity()));
            Map<String,String> agentProductStrMap =agentProductUpdateBO.getAgentCodeList().stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
            //需新增代理商产品
            for (String agentCode:agentProductUpdateBO.getAgentCodeList()){
                if(agentProductListMap.get(agentCode)==null){
                    AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(agentCode,true);
                    AgentProduct agentProduct = new AgentProduct();
                    agentProduct.setParentProductCode(product.getProductCode());
                    agentProduct.setParentProductName(product.getProductName());
                    agentProduct.setParentAgentCode(parentAgentAccount.getAgentCode());
                    agentProduct.setAgentCode(agentAccount.getAgentCode());
                    agentProduct.setAgentName(agentAccount.getAgentName());
                    agentProduct.setProductStatus(BaseConstant.ONE_INT);
                    addAgentProduct(agentProduct);
                }
            }
            //需删除代理产品
            for(AgentProduct agentProduct:agentProductList){
                if(agentProductStrMap.get(agentProduct.getAgentCode())==null){
                    deleteAgentProduct(agentProduct.getAgentProductId());
                }
            }
        }
    }


    /**
     * 此方法会创建 代理商下所有子代理的产品包括本身
     * @param agentAccount
     * @param parentAgentAccount
     * @param product
     */
    @Override
    public void addSubAgentProduct(AgentAccount agentAccount, AgentAccount parentAgentAccount, Product product,Integer productCommission) throws BizException {
        //创建子代理商产品信息
        AgentProduct agentProduct = createAgentProduct(agentAccount,parentAgentAccount,product,productCommission);
        // 获取子代理商编码
        String parentAgentCode = agentAccount.getAgentCode();
        if (parentAgentCode != null && !parentAgentCode.isEmpty()) {
            // 根据父代理商编码查询父代理商账号信息
            List<AgentAccount> childAgentAccountList = agentAccountService.list(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getParentAgentCode,parentAgentCode));
            if (!CollectionUtils.isEmpty(childAgentAccountList)) {
                for(AgentAccount childAgentAccount:childAgentAccountList){
                    // 递归调用创建父代理商的产品信息
                    addSubAgentProduct(childAgentAccount,agentAccount,product,agentProduct.getDistributionProductCommission());
                }
            }
        }
    }

    /**
     * 创建代理商产品信息
     * @param agentAccount 代理商账号信息
     * @return 代理商产品信息
     */
    private AgentProduct createAgentProduct(AgentAccount agentAccount,AgentAccount parentAgentAccount,Product product,Integer productCommission) throws BizException {
        //判断一下是否产品已存在过
        List<AgentProduct> agentProductList = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getAgentCode,agentAccount.getAgentCode()).eq(AgentProduct::getParentProductCode,product.getProductCode())
                .eq(AgentProduct::getParentAgentCode,parentAgentAccount.getAgentCode()));
        if(!CollectionUtils.isEmpty(agentProductList)){
            return agentProductList.get(BaseConstant.ZERO_INT);
        }
        AgentProduct agentProduct = new AgentProduct();
        agentProduct.setParentProductCode(product.getProductCode());
        agentProduct.setParentProductName(product.getProductName());
        agentProduct.setParentAgentCode(parentAgentAccount.getAgentCode());
        agentProduct.setProductCommission(product.getProductCommission());
        agentProduct.setAgentCode(agentAccount.getAgentCode());
        agentProduct.setAgentName(agentAccount.getAgentName());
        agentProduct.setProductStatus(product.getProductStatus());
        addAgentProduct(agentProduct);

        //拼接二维码包含的url信息
        StringBuffer stringBuffer = new StringBuffer(h5url);
        stringBuffer.append("?productCode=").append(product.getProductCode()).append("&agentCode=").append(agentAccount.getAgentCode());
        //创建子代理商产品海报图
        createAgentProductPoster(agentProduct,product.getProductMasterMap(),stringBuffer.toString());
        //重新计算佣金
        updateAgentProductCommission(agentProduct.getParentProductCode(),agentProduct.getAgentCode(),productCommission);
        //重新查询一遍数据 作为递归入参
        agentProduct = baseMapper.selectById(agentProduct.getAgentProductId());
        return agentProduct;
    }


    /**
     * 此方法会删除代理下所有子代理的产品 包括本身
     * @param product
     */
    @Override
    public void deleteSubAgentProduct(AgentAccount agentAccount, Product product) {
        //创建子代理商产品信息
       List<AgentProduct> agentProductList = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getAgentCode,agentAccount.getAgentCode()).eq(AgentProduct::getParentProductCode,product.getProductCode()));
       if(!CollectionUtils.isEmpty(agentProductList)){
           for (AgentProduct agentProduct:agentProductList){
               deleteAgentProduct(agentProduct.getAgentProductId());
           }
       }
        // 获取子代理商编码
        String parentAgentCode = agentAccount.getAgentCode();
        if (parentAgentCode != null && !parentAgentCode.isEmpty()) {
            // 根据父代理商编码查询父代理商账号信息
            List<AgentAccount> childAgentAccountList = agentAccountService.list(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getParentAgentCode,parentAgentCode));
            if (!CollectionUtils.isEmpty(childAgentAccountList)) {
                for(AgentAccount childAgentAccount:childAgentAccountList){
                    // 递归调用创建父代理商的产品信息
                    deleteSubAgentProduct(childAgentAccount,product);
                }
            }
        }
    }



    /**
     * 此方法会生成 代理商下所有子代理的产品海报图
     * @param agentAccount
     * @param product
     */
    @Override
    public void addSubAgentProductPoster(AgentAccount agentAccount, Product product) throws BizException {
        if(!StringUtils.hasLength(product.getProductMasterMap())){
            return;
        }
        AgentProduct agentProduct = baseMapper.selectOne(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()).eq(AgentProduct::getAgentCode,agentAccount.getAgentCode()));
        if(agentProduct ==null){
            return;
        }
        //拼接二维码包含的url信息
        StringBuffer stringBuffer = new StringBuffer(h5url);
        stringBuffer.append("?productCode=").append(product.getProductCode()).append("&agentCode=").append(agentAccount.getAgentCode());
        //创建子代理商产品海报图
        createAgentProductPoster(agentProduct,product.getProductMasterMap(),stringBuffer.toString());
        // 获取子代理商编码
        String parentAgentCode = agentAccount.getAgentCode();
        if (parentAgentCode != null && !parentAgentCode.isEmpty()) {
            // 根据父代理商编码查询父代理商账号信息
            List<AgentAccount> childAgentAccountList = agentAccountService.list(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getParentAgentCode,parentAgentCode));
            if (!CollectionUtils.isEmpty(childAgentAccountList)) {
                for(AgentAccount childAgentAccount:childAgentAccountList){
                    // 递归调用创建父代理商的产品信息
                    agentProductInitService.addSubAgentProductPoster(childAgentAccount,product);
                }
            }
        }
    }

    /**
     * 创建代理商产品海报图并保存
     */
    private void createAgentProductPoster(AgentProduct agentProduct,String productPictureUrl,String content) {
        try {
            String url = qrCodeService.getProductPoster(productPictureUrl,BaseConstant.BASE_PICTURE_URL,content);
            //成功生成了海报图 删除原图并保存新图
            if(StringUtils.hasLength(url)){
                String old = agentProduct.getProductQrcodeMap();
                agentProduct.setProductQrcodeMap(url);
                baseMapper.updateById(agentProduct);
                pictureService.deletePicture(old);
            }
        }catch (Exception e){
            log.info("生成产品海报异常:{},{}",content,e.getMessage());
        }
    }

    /**
     * 生成产品海报图
     * @return
     */
    public void addProductPoster(AgentAccount agentAccount, Product product){
        if(!StringUtils.hasLength(product.getProductMasterMap())){
            return;
        }
        //拼接二维码包含的url信息
        StringBuffer stringBuffer = new StringBuffer(h5url);
        stringBuffer.append("?productCode=").append(product.getProductCode()).append("&agentCode=").append(agentAccount.getAgentCode());
        String url = qrCodeService.getProductPoster(product.getProductMasterMap(),BaseConstant.BASE_PICTURE_URL,stringBuffer.toString());
        product.setProductQrcodeMap(url);
        productMapper.updateById(product);
    }



    /**
     * 生成推广海报图
     * @return
     */
    public void addRegisterQrcodeMap(AgentAccount agentAccount,String url, Integer number) throws BizException {
        if(agentAccount == null){
            return;
        }
        //拼接二维码包含的url信息
        String code = "?agentCode="+agentAccount.getAgentCode();
        ToolConfig toolConfig = toolConfigService.getToolConfig(BaseConstant.THREE_INT);
        String extendUrl = toolConfig.getDomainNameUrl()+code;
        if(number == BaseConstant.ONE_INT){
            String registerQrcodeMapUrl = qrCodeService.getRegisterQrcodeMap1Url(url,extendUrl);
            agentAccount.setRegisterQrcodeMap1(registerQrcodeMapUrl);
        }
        if(number == BaseConstant.TWO_INT){
            String registerQrcodeMapUrl = qrCodeService.getRegisterQrcodeMap1Url(url,extendUrl);
            agentAccount.setRegisterQrcodeMap2(registerQrcodeMapUrl);
        }
        if(number == BaseConstant.THREE_INT){
            String registerQrcodeMapUrl = qrCodeService.getRegisterQrcodeMap3Url(url,extendUrl);
            agentAccount.setRegisterQrcodeMap3(registerQrcodeMapUrl);
        }
        agentAccountService.updateById(agentAccount);
    }

}
