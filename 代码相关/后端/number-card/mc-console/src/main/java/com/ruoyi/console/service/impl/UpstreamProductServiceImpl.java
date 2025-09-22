package com.ruoyi.console.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.enums.ProductEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.UpstreamProductAddAndUpdateBO;
import com.ruoyi.common.order.bo.UpstreamProductSelectBO;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.vo.OrderSelectVO;
import com.ruoyi.common.order.vo.UpstreamProductVO;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.ProductMapper;
import com.ruoyi.console.mapper.UpstreamApiMapper;
import com.ruoyi.console.mapper.UpstreamProductMapper;
import com.ruoyi.console.service.UpstreamProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
public class UpstreamProductServiceImpl extends ServiceImpl<UpstreamProductMapper, UpstreamProduct> implements UpstreamProductService {

    @Resource
    UpstreamApiMapper upstreamApiMapper;

    @Resource
    ProductMapper productMapper;

    /**
     * 分页查询上游产品列表
     *
     * @return
     * @throws BizException
     */
    public PageResult<UpstreamProductVO> selectUpstreamProductListPage(UpstreamProductSelectBO upstreamProductSelectBO) throws BizException {
        //读取分页
        Page page = new Page(upstreamProductSelectBO.getPageNo(), upstreamProductSelectBO.getPageSize());
        Page<UpstreamProduct> upstreamApiPage = baseMapper.selectPage(page, new LambdaQueryWrapper<UpstreamProduct>()
                .eq(UpstreamProduct::getUpstreamApiId,upstreamProductSelectBO.getUpstreamApiId())
                .eq(StringUtils.isNotEmpty(upstreamProductSelectBO.getUpstreamProductCode()), UpstreamProduct::getUpstreamProductCode, upstreamProductSelectBO.getUpstreamProductCode())
                .like(StringUtils.isNotEmpty(upstreamProductSelectBO.getUpstreamProductName()), UpstreamProduct::getUpstreamProductName, upstreamProductSelectBO.getUpstreamProductName())
        );
        Page<UpstreamProductVO> upstreamProductVOPage = new Page<>();
        BeanUtil.copyProperties(upstreamApiPage, upstreamProductVOPage);
        if (!CollectionUtils.isEmpty(upstreamApiPage.getRecords())) {
            List<UpstreamProductVO> upstreamProductVOList = new ArrayList<>();
            for (UpstreamProduct upstreamProduct : upstreamApiPage.getRecords()) {
                UpstreamProductVO upstreamProductVO = new UpstreamProductVO();
                BeanUtil.copyProperties(upstreamProduct, upstreamProductVO);
                //查询展示列
                List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getUpstreamProductCode,upstreamProduct.getUpstreamProductCode()));
                if(!CollectionUtils.isEmpty(productList)){
                    List<String> productStrList = new ArrayList<>();
                    //标记一下是否上架
                    for (Product product:productList){
                        productStrList.add(product.getProductName()+","+ ProductEnum.ProductStatusEnum.getProductByStatus(product.getProductStatus()));
                    }
                    upstreamProductVO.setProductList(productStrList);
                }
                upstreamProductVOList.add(upstreamProductVO);
            }
            upstreamProductVOPage.setRecords(upstreamProductVOList);
        }
        return PageResultFactory.createPageResult(upstreamProductVOPage);
    }


    /**
     * 新增上游产品
     *
     * @return
     * @throws BizException
     */
    @Override
    public void addUpstreamProduct(UpstreamProductAddAndUpdateBO upstreamProductAddAndUpdateBO) throws BizException {
        if (upstreamProductAddAndUpdateBO.getUpstreamApiId() == null) {
            throw new BizException("上游API不能为空");
        }
        UpstreamApi upstreamApi = upstreamApiMapper.selectById(upstreamProductAddAndUpdateBO.getUpstreamApiId());
        if (upstreamApi == null) {
            throw new BizException("未查询到上游API信息");
        }
        UpstreamProduct upstreamProduct = new UpstreamProduct();
        BeanUtil.copyProperties(upstreamProductAddAndUpdateBO, upstreamProduct);
        upstreamProduct.setUpstreamProductId(null);
        //生成产品编码 6位数
        upstreamProduct.setUpstreamProductCode(RandomUtil.randomString(BaseConstant.SEX_INT));
        upstreamProduct.setUpstreamProductName(upstreamProductAddAndUpdateBO.getUpstreamProductName());
        upstreamProduct.setUpstreamApiId(upstreamProduct.getUpstreamApiId());
        upstreamProduct.setUpstreamApiCode(upstreamApi.getUpstreamApiCode());
        upstreamProduct.setUpstreamApiName(upstreamApi.getUpstreamApiName());
        upstreamProduct.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(upstreamProduct);
    }


    /**
     * 更新上游产品
     *
     * @return
     * @throws BizException
     */
    @Override
    public void updateUpstreamProduct(UpstreamProductAddAndUpdateBO upstreamProductAddAndUpdateBO) throws BizException {
        if (upstreamProductAddAndUpdateBO == null || upstreamProductAddAndUpdateBO.getUpstreamProductId() == null) {
            throw new BizException("上游产品ID不能为空");
        }
        UpstreamProduct upstreamProduct = baseMapper.selectById(upstreamProductAddAndUpdateBO.getUpstreamProductId());
        if (upstreamProduct == null) {
            throw new BizException("产品ID{}不存在", upstreamProductAddAndUpdateBO.getUpstreamProductId());
        }
        String name = upstreamProduct.getUpstreamProductName();
        upstreamProduct.setUpstreamProductName(StringUtils.isNotEmpty(upstreamProductAddAndUpdateBO.getUpstreamProductName())?upstreamProductAddAndUpdateBO.getUpstreamProductName():upstreamProduct.getUpstreamProductName());
        //更新参数
        upstreamProduct.setArgument1(upstreamProductAddAndUpdateBO.getArgument1());
        upstreamProduct.setArgument2(upstreamProductAddAndUpdateBO.getArgument2());
        upstreamProduct.setArgument3(upstreamProductAddAndUpdateBO.getArgument3());
        upstreamProduct.setArgument4(upstreamProductAddAndUpdateBO.getArgument4());
        upstreamProduct.setArgument5(upstreamProductAddAndUpdateBO.getArgument5());
        upstreamProduct.setArgument6(upstreamProductAddAndUpdateBO.getArgument6());
        upstreamProduct.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(upstreamProduct);
        //判断名称是否更新 如果更新 同步更新产品中名称
        if(StringUtils.isNotEmpty(name)&&!name.equals(upstreamProductAddAndUpdateBO.getUpstreamProductName())){
            List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getUpstreamProductCode,upstreamProduct.getUpstreamProductCode()));
            if(!CollectionUtils.isEmpty(productList)){
                for(Product product:productList){
                    product.setUpstreamProductName(upstreamProductAddAndUpdateBO.getUpstreamProductName());
                    productMapper.updateById(product);
                }
            }
        }
    }

    /**
     * 删除上游产品
     *
     * @return
     * @throws BizException
     */
    @Override
    public void deleteUpstreamProduct(Integer upstreamProductId) throws BizException {
        if (upstreamProductId == null) {
            throw new BizException("ID不能为空");
        }
        UpstreamProduct upstreamProduct = baseMapper.selectById(upstreamProductId);
        if (upstreamProduct == null) {
            throw new BizException("产品ID{}不存在", upstreamProductId);
        }
        baseMapper.deleteById(upstreamProductId);
        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getUpstreamProductCode,upstreamProduct.getUpstreamProductCode()));
        if(!CollectionUtils.isEmpty(productList)){
            for(Product product:productList){
                product.setUpstreamProductName("已删除");
                productMapper.updateById(product);
            }
        }
    }


}
