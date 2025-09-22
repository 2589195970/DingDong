package com.ruoyi.order.service.impl.order;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.order.service.ProductCheckConfigService;
import com.ruoyi.order.service.order.OrderCheckService;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.reuqest.BaseSubmitOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:13
 */
@Service
@Slf4j
public class OrderCheckServiceImpl implements OrderCheckService {

    @Resource
    ProductCheckConfigService productCheckConfigService;

    /**
     * 订单前置拦截校验
     * @param request
     * @param product
     * @param upstreamInfo
     * @param order
     * @throws BizException
     */
    @Override
    public void orderBeforeCheck(BaseSubmitOrderRequest request, Product product, UpstreamInfo upstreamInfo, Order order) throws BizException {
        //省市屏蔽地域拦截
        provinceAndCityCodeCheck(request,product);
        //年龄拦截
        ageCheck(request,product);
    }


    /**
     * 省市编码拦截
     */
    public void provinceAndCityCodeCheck(BaseSubmitOrderRequest request, Product product) throws BizException {
        try {
            List<ProductCheckConfig> productCheckConfigList = productCheckConfigService.getProductCheckConfigList(product.getProductCode(),BaseConstant.ONE_INT);
            if (CollectionUtils.isEmpty(productCheckConfigList)) {
                return;
            }
            for(ProductCheckConfig productCheckConfig:productCheckConfigList){
                //省级
                if(productCheckConfig.getTerritoryCheckType() == BaseConstant.ZERO_INT){
                    List<AddressCode> territoryProvinceList = JSONObject.parseArray(productCheckConfig.getTerritoryProvinceJson(),AddressCode.class);
                    //判断策略 策略 0 允许 1禁止
                    if(productCheckConfig.getTactics()==BaseConstant.ONE_INT){
                        //判断禁止
                        for(AddressCode addressCode:territoryProvinceList){
                            if(request.getProvinceCode().equals(addressCode.getProvinceCode())){
                                throw new BizException("订单省拦截校验不通过,禁发地域:{}",request.getProvinceName());
                            }
                        }
                    }
                    //判断 允许
                    if(productCheckConfig.getTactics()==BaseConstant.ZERO_INT){
                        boolean b = false;
                        for(AddressCode addressCode:territoryProvinceList){
                            if(request.getProvinceCode().equals(addressCode.getProvinceCode())){
                                b = true;
                            }
                        }
                        if(!b){
                            throw new BizException("订单省拦截校验不通过,非允许省份名单:{}",request.getProvinceName());
                        }
                    }
                }
                //市级
                if(productCheckConfig.getTerritoryCheckType() == BaseConstant.ONE_INT){
                    List<AddressCode> territoryProvinceList = JSONObject.parseArray(productCheckConfig.getTerritoryProvinceJson(),AddressCode.class);
                    //判断策略 0 允许 1禁止
                    if(productCheckConfig.getTactics()==BaseConstant.ONE_INT){
                        for(AddressCode addressCode:territoryProvinceList){
                            if(request.getCityCode().equals(addressCode.getCityCode())){
                                throw new BizException("订单市拦截校验不通过,禁发地域:{}",request.getCityName());
                            }
                        }
                    }
                    // 1 允许进单
                    if(productCheckConfig.getTactics()==BaseConstant.ZERO_INT){
                        boolean b = false;
                        for(AddressCode addressCode:territoryProvinceList){
                            if(request.getCityCode().equals(addressCode.getCityCode())){
                                b = true;
                            }
                        }
                        if(!b){
                            throw new BizException("订单市拦截校验不通过,非允许市名单:{}",request.getCityName());
                        }
                    }
                }
            }
        } catch (BizException b) {
            log.info("订单省市拦截校验不通过:{}", b.getMessage());
            throw b;
        } catch (Exception e) {
            log.info("省市区编码拦截异常:{}", e.getMessage());
        }

    }


    /**
     * 年龄拦截
     */
    public void ageCheck(BaseSubmitOrderRequest request, Product product) throws BizException {
        try {
            if (request == null || !StringUtils.hasLength(request.getCardId())||product.getProductAgeMin()==null||product.getProductAgeMax()==null) {
                return;
            }
            Integer age = getCardIdAge(request.getCardId());
            boolean isTrue = false;
            if (age >= product.getProductAgeMin() && age <= product.getProductAgeMax()) {
                //如果在任意配置的年龄范围内
                isTrue = true;
            }
            if (!isTrue) {
                throw new BizException("年龄拦截校验不通过:{}",age);
            }
        } catch (BizException b) {
            log.info("年龄禁止下单拦截:{}", b.getMessage());
            throw b;
        } catch (Exception e) {
            log.info("年龄拦截异常:{}", e.getMessage());
        }
    }

    /**
     * 计算身份证中年龄
     *
     * @param idCard
     * @return
     */
    public static Integer getCardIdAge(String idCard) {
        if (!StringUtils.hasLength(idCard) || idCard.length() != 18) {
            return null;
        }
        String birthday = idCard.substring(6, 14);
        Calendar now = Calendar.getInstance();
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = (now.get(Calendar.MONTH) + 1);
        int nowDay = now.get(Calendar.DAY_OF_MONTH);

        int year = Integer.valueOf(birthday.substring(0, 4));
        int month = Integer.valueOf(birthday.substring(4, 6));
        int day = Integer.valueOf(birthday.substring(6));

        int age = nowYear - year;
        if (age <= 0 || age >= 100) {
            return null;
        }
        if (nowMonth - month > 0) {
            return age;
        }
        if (nowMonth - month == 0) {
            if (nowDay - day >= 0) {
                return age;
            }
        }
        return age - 1;
    }

}
