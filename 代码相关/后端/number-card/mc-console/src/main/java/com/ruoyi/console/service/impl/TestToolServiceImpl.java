package com.ruoyi.console.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.console.service.TestToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 测试方法相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class TestToolServiceImpl implements TestToolService {

    /**
     * String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName
     * ADDRESS_011("1", "1","1","1","1","1")
     * @param str
     */
    @Override
    public void getDistrict(String str) {
        List<Map> mapList = JSONObject.parseArray(str,Map.class);
        //生成省市区枚举
        List<String> stringList = new ArrayList<>();
        for(Map provinceMap :mapList){
            String provinceCode = provinceMap.get("code").toString();
            String provinceName = provinceMap.get("label").toString();
            List<Map> cityMapList = JSONObject.parseArray(provinceMap.get("list").toString(),Map.class);
            for(Map cityMap :cityMapList){
                //市一级
                String cityCode = cityMap.get("code").toString();
                String cityName = cityMap.get("label").toString();
                List<Map> countyMapList = JSONObject.parseArray(cityMap.get("list").toString(),Map.class);
                for(Map countyMap :countyMapList){
                    //市一级
                    String countyCode = countyMap.get("code").toString();
                    String countyName = countyMap.get("label").toString();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("ADDRESS_").append(countyCode).append("(\"").append(provinceCode).append("\",").append("\"").append(provinceName).append("\",")
                            .append("\"").append(cityCode).append("\",").append("\"").append(cityName).append("\",")
                            .append("\"").append(countyCode).append("\",").append("\"").append(countyName).append("\")")
                    ;
                    stringList.add(stringBuilder.toString());
                }
            }

        }
        for(int i =0;i<stringList.size();i++){
            log.info(stringList.get(i));
        }
    }


}
