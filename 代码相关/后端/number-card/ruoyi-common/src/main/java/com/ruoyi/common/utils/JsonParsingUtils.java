package com.ruoyi.common.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;


@Slf4j
public class JsonParsingUtils {

    public static String fileToJson(String fileUrl, String fileName) throws BizException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Resource resource = new ClassPathResource(fileUrl + fileName);
            InputStream inputStream = resource.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            boolean firstLine = true;
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (!firstLine) {
                    stringBuilder.append(System.getProperty("line.separator"));
                } else {
                    firstLine = false;
                }
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(StrUtil.format("json解析失败"));
        }
    }


    public static List<T> fileToJsonList(String fileUrl, String fileName, Class<T> clz) throws BizException {
        String json = fileToJson(fileUrl, fileName);
        if (!StringUtils.hasLength(json)) {
            throw new BizException(StrUtil.format("json解析内容为空"));
        }
        try {
            List<T> list = JSONObject.parseArray(json, clz);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(StrUtil.format("json格式异常"));
        }
    }

    public static Map<String, Object> fileToJsonMap(String fileUrl, String fileName) throws BizException {
        String json = fileToJson(fileUrl, fileName);
        if (!StringUtils.hasLength(json)) {
            throw new BizException(StrUtil.format("json解析内容为空"));
        }
        try {
            Map<String, Object> map = JSONObject.parseObject(json, Map.class);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(StrUtil.format("json格式异常"));
        }
    }

    public static void main(String[] args) throws BizException {

    }
}
