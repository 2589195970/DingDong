package com.ruoyi.console.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.console.service.ExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 导入导出工具类
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class ExportServiceImpl  implements ExportService {


    /**
     * 导出工具类
     * @param response
     * @param csvList
     * @throws Exception
     */
    public void writeCsvResponse(HttpServletResponse response, List<String[]> csvList,String fileName) throws Exception {
        response.setContentType("application/x-msdownload;charset=utf-8");
        response.setHeader("Content-Disposition", fileName);
        ServletOutputStream out = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            out = response.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(out);
            CsvWriter csvWriter = CsvUtil.getWriter(outputStreamWriter);
            csvWriter.write(csvList);
            csvWriter.flush();
            csvWriter.close();
        } finally {
            IoUtil.close(outputStreamWriter);
            IoUtil.close(out);
        }
    }

    /**
     * 去掉特殊字符
     * @param text
     * @return
     */
    public String getCSVText(String text) {
        if (!StrUtil.isBlankIfStr(text)) {
            Pattern p = Pattern.compile("\t|\r|\n|\\-");
            Matcher m = p.matcher(text);
            return m.replaceAll("");
        }
        return "";
    }


    /**
     * 验证身份证号码
     *
     * @param str 居民身份证号码18位，第一位不能为0，最后一位可能是数字或字母，中间16位为数字 \d同[0-9]
     */
    public void checkCardNo(int i, String str, Integer line) throws BizException {
        String regex = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        if (!Pattern.matches(regex, str)) {
            throw new BizException("第" + (line + 1) + "行,第" + i + "列,身份证验证失败");
        }
    }

    /**
     * 验证手机号码
     */
    public void checkMobile(int i, String str, Integer line) throws BizException {
        String regex = "(\\+\\d+)?1\\d{10}$";
        if (!Pattern.matches(regex, str)) {
            throw new BizException("第" + (line + 1) + "行,第" + i + "列,手机号码验证失败");
        }
    }

    /**
     * 空校验
     * @param i
     * @param str
     * @param line
     */
    public void checkNotNull(int i, String str, Integer line) throws BizException {
        if (StrUtil.isEmpty(str)) {
            throw new BizException("第" + line + "行,第" + i + "列,数据为空");
        }
    }

    /**
     * 处理时间
     * @param time
     * @return
     */
    public String getCSVDate(Long time) {
        if (time != null && time > 0) {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
            return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
        }
        return "";
    }

}
