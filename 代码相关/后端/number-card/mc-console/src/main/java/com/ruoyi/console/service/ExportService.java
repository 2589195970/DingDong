package com.ruoyi.console.service;


import com.ruoyi.common.exception.BizException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface ExportService  {


    /**
     * 导出工具类
     * @param response
     * @param csvList
     * @throws Exception
     */
    void writeCsvResponse(HttpServletResponse response, List<String[]> csvList,String fileName) throws Exception;

    /**
     * 去掉特殊字符
     * @param text
     * @return
     */
    String getCSVText(String text);

    /**
     * 验证身份证号码
     *
     * @param str 居民身份证号码18位，第一位不能为0，最后一位可能是数字或字母，中间16位为数字 \d同[0-9]
     */
    void checkCardNo(int i, String str, Integer line) throws BizException;

    /**
     * 验证手机号码
     */
    void checkMobile(int i, String str, Integer line) throws BizException;

    /**
     * 验证手机号码
     */
    void checkNotNull(int i, String str, Integer line) throws BizException;


    /**
     * 处理时间
     * @param time
     * @return
     */
    String getCSVDate(Long time);


}
