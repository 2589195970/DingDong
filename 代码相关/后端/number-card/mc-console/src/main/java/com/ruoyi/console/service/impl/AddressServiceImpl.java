package com.ruoyi.console.service.impl;


import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.enums.AddressCodeEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AddressCacheCityBO;
import com.ruoyi.common.order.bo.AddressCacheCountyBO;
import com.ruoyi.common.order.bo.AddressCacheProvinceBO;
import com.ruoyi.common.order.bo.AddressSelectBO;
import com.ruoyi.common.order.entity.AddressCode;
import com.ruoyi.common.order.vo.AddressSelectVO;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 地址相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    @Resource(name = "addressStringRedisTemplate")
    StringRedisTemplate addressStringRedisTemplate;

    /**
     * 获取地址列表
     * @param addressSelectBO
     * @return
     */
    @Override
    public AddressSelectVO selectAddressList(AddressSelectBO addressSelectBO) throws BizException {
        //获取省编码
        Map<String,AddressCacheProvinceBO> provinceCodeMap = new HashMap<>();
        String provinceKey = "";
        if(addressSelectBO!=null&& StringUtils.isNotEmpty(addressSelectBO.getProvinceCode())){
            provinceCodeMap.put(addressSelectBO.getProvinceCode(),new AddressCacheProvinceBO());
            provinceKey = addressSelectBO.getProvinceCode();
        }else {
            for (Map.Entry<String, String> province : BaseConstant.PROVINCES.entrySet()) {
                provinceCodeMap.put(province.getValue(),new AddressCacheProvinceBO());
                provinceKey = provinceKey+province.getValue();
            }
        }
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.ADDRESS_JSON, provinceKey);
        String addressJson = addressStringRedisTemplate.opsForValue().get(cacheKey);
        //返回查询值
        if(StringUtils.isNotEmpty(addressJson)){
            return JSONObject.parseObject(addressJson,AddressSelectVO.class);
        }
        AddressSelectVO addressSelectVO = new AddressSelectVO();
        //遍历省编码 从枚举中组装省市区格式
        for (Map.Entry<String, AddressCacheProvinceBO> provinceMap : provinceCodeMap.entrySet()) {
            AddressCacheProvinceBO addressCacheProvinceBO = provinceMap.getValue();
            String provinceCode = provinceMap.getKey();
            List<AddressCode> addressCodeList = AddressCodeEnum.getAddressCodeByProvinceCode(provinceCode);
            for (AddressCode value : addressCodeList) {
               if(!value.getProvinceCode().equals(provinceCode)){
                   continue;
               }
               //拼装json
                addressCacheProvinceBO.setName(value.getProvinceName());
                addressCacheProvinceBO.setCode(value.getProvinceCode());
                List<AddressCacheCityBO> cityList = CollectionUtils.isEmpty(addressCacheProvinceBO.getCityList())? new ArrayList<>():addressCacheProvinceBO.getCityList();
                boolean cityBoolean = true;
                for(AddressCacheCityBO addressCacheCityBO:cityList){
                    if(value.getCityCode().equals(addressCacheCityBO.getCode())){
                        List<AddressCacheCountyBO> addressCacheCountyBOS = addressCacheCityBO.getCountList();
                        AddressCacheCountyBO addressCacheCountyBO = new AddressCacheCountyBO();
                        addressCacheCountyBO.setName(value.getCountyName());
                        addressCacheCountyBO.setCode(value.getCountyCode());
                        addressCacheCountyBOS.add(addressCacheCountyBO);
                        addressCacheCityBO.setCountList(addressCacheCountyBOS);
                        cityBoolean = false;
                    }
                }
                //未匹配到城市 说明未创建 新建城市
                if(cityBoolean){
                    AddressCacheCityBO addressCacheCityBO = new AddressCacheCityBO();
                    addressCacheCityBO.setCode(value.getCityCode());
                    addressCacheCityBO.setName(value.getCityName());
                    List<AddressCacheCountyBO> addressCacheCountyBOS = new ArrayList<>();
                    AddressCacheCountyBO addressCacheCountyBO = new AddressCacheCountyBO();
                    addressCacheCountyBO.setName(value.getCountyName());
                    addressCacheCountyBO.setCode(value.getCountyCode());
                    addressCacheCountyBOS.add(addressCacheCountyBO);
                    addressCacheCityBO.setCountList(addressCacheCountyBOS);
                    cityList.add(addressCacheCityBO);
                }
                addressCacheProvinceBO.setCityList(cityList);
            }
        }
        //组装返回参数
        List<AddressCacheProvinceBO> addressCacheProvinceBO = new ArrayList<>();
        for (Map.Entry<String, AddressCacheProvinceBO> provinceMap : provinceCodeMap.entrySet()) {
            addressCacheProvinceBO.add(provinceMap.getValue());
        }
        addressCacheProvinceBO = addressCacheProvinceBO.stream().sorted(Comparator.comparing(AddressCacheProvinceBO::getCode)).collect(Collectors.toList());
        addressSelectVO.setAddressCacheProvinceBO(addressCacheProvinceBO);
        //记录缓存
        addressStringRedisTemplate.opsForValue().set(cacheKey, JSONUtil.toJsonStr(addressSelectVO), 1, TimeUnit.DAYS);
        return addressSelectVO;
    }
}
