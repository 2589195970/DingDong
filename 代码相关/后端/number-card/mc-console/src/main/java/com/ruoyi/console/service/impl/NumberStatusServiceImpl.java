package com.ruoyi.console.service.impl;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.apis.ecc.EccBalanceResponse;
import com.ruoyi.common.apis.ecc.EccEmptyNumberQueryResponse;
import com.ruoyi.common.apis.ecc.EccNumberShiftResponse;
import com.ruoyi.common.apis.xuanka.*;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.ImportOrderConstant;
import com.ruoyi.common.constant.NumberStatusConstant;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.enums.ProductEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.order.bo.OrderNumberStatusLogSelectBO;
import com.ruoyi.common.order.bo.UploadNumberListExcelBO;
import com.ruoyi.common.order.entity.NumberStatusLog;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.NumberStatusLogMapper;
import com.ruoyi.console.service.ExportService;
import com.ruoyi.console.service.NumberStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * 号码状态查询相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class NumberStatusServiceImpl implements NumberStatusService {

    @Resource
    HttpClient httpClient;

    @Resource
    NumberStatusLogMapper numberStatusLogMapper;

    @Resource
    ExportService exportService;

    /**
     * 根据类型查询号码数据
     */
    public NumberStatusLog getPhoneByType(String phone,Integer type) throws Exception {
        if(ProductEnum.NumberRequestStatusTypeEnum.ZERO.getStatus() == type){
            return getMobileBalance(phone);
        }
        if(ProductEnum.NumberRequestStatusTypeEnum.ONE.getStatus() == type){
            return getUnicomBalance(phone);
        }
        if(ProductEnum.NumberRequestStatusTypeEnum.TWO.getStatus() == type){
            return getTelecomBalance(phone);
        }
        if(ProductEnum.NumberRequestStatusTypeEnum.THREE.getStatus() == type){
            return getNumberShift(phone);
        }
        if(ProductEnum.NumberRequestStatusTypeEnum.FOUR.getStatus() == type){
            return getNumberQuery(phone);
        }
        if(ProductEnum.NumberRequestStatusTypeEnum.FIVE.getStatus() == type){
            return getEmptyNumberQuery(phone);
        }
        if(ProductEnum.NumberRequestStatusTypeEnum.SEX.getStatus() == type){
            return getEccBalance(phone);
        }
        if(ProductEnum.NumberRequestStatusTypeEnum.SEVEN.getStatus() == type){
            return getEccNumberShift(phone);
        }
        if(ProductEnum.NumberRequestStatusTypeEnum.EIGHT.getStatus() == type){
            return getEccEmptyNumberQuery(phone);
        }
        return null;
    }


    /**
     * 余额查询日志查询
     *
     * @return
     * @throws BizException
     */
    public PageResult<NumberStatusLog> selectNumberStatusLogListPage(OrderNumberStatusLogSelectBO orderNumberStatusLogSelectBO) throws BizException {
        //读取分页
        Page page = new Page(orderNumberStatusLogSelectBO.getPageNo(), orderNumberStatusLogSelectBO.getPageSize());
        Page<NumberStatusLog> orderCommissionPage = numberStatusLogMapper.selectPage(page, new LambdaQueryWrapper<NumberStatusLog>()
                .like(StringUtils.isNotEmpty(orderNumberStatusLogSelectBO.getPhone()), NumberStatusLog::getPhone, orderNumberStatusLogSelectBO.getPhone())
                .eq(orderNumberStatusLogSelectBO.getType() != null, NumberStatusLog::getRequestType, orderNumberStatusLogSelectBO.getType())
                .between((orderNumberStatusLogSelectBO.getStarTime() != null && orderNumberStatusLogSelectBO.getEndTime() != null), NumberStatusLog::getCreateTime, orderNumberStatusLogSelectBO.getStarTime(), orderNumberStatusLogSelectBO.getEndTime())
        );
        return PageResultFactory.createPageResult(orderCommissionPage);
    }




    /**
     * 余额查询日志导出
     *
     * @return
     * @throws BizException
     */
    public void exportNumberStatusLogList(OrderNumberStatusLogSelectBO orderNumberStatusLogSelectBO, HttpServletResponse response) throws Exception {
        List<NumberStatusLog> numberStatusLogList = numberStatusLogMapper.selectList(new LambdaQueryWrapper<NumberStatusLog>()
                .like(StringUtils.isNotEmpty(orderNumberStatusLogSelectBO.getPhone()), NumberStatusLog::getPhone, orderNumberStatusLogSelectBO.getPhone())
                .eq(orderNumberStatusLogSelectBO.getType() != null, NumberStatusLog::getPhone, orderNumberStatusLogSelectBO.getType())
                .between((orderNumberStatusLogSelectBO.getStarTime() != null && orderNumberStatusLogSelectBO.getEndTime() != null), NumberStatusLog::getCreateTime, orderNumberStatusLogSelectBO.getStarTime(), orderNumberStatusLogSelectBO.getEndTime())
                .orderByDesc(NumberStatusLog::getCreateTime)
        );
        //数据导出
        exportService.writeCsvResponse(response, numberStatusLogListToCsvList(numberStatusLogList),"attachment;filename=numberLog.csv");

    }


    /**
     * `phone` varchar(255) NOT NULL COMMENT '检测号码',
     *   `message` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '消息',
     *   `request_type` int DEFAULT NULL COMMENT '请求类型 0 炫咖移动余额查询 1炫咖联通余额查询 2炫咖电信余额查询 3炫咖携号转网 4 炫咖号码查询 5 炫咖空号检测 6 额查查余额查询 7 额查查携号查询 8 额查查空号查询',
     *   `request_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '请求url',
     *   `request_body` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '请求参数',
     *   `request_msg` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '请求返回',
     *   `mobile_fee` varchar(255) DEFAULT NULL COMMENT '号码余额',
     *   `area` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '号码归属地',
     *   `province` varchar(255) DEFAULT NULL COMMENT '号码归属省',
     *   `city` varchar(255) DEFAULT NULL COMMENT '号码归属市',
     *   `pri_isp_name` varchar(255) DEFAULT NULL COMMENT '原运营商名称',
     *   `new_isp_name` varchar(255) DEFAULT NULL COMMENT '现运营商名称',
     *   `is_change` int DEFAULT NULL COMMENT '是否携号转网，0：否；1:是 ',
     *   `number_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '运营商类型',
     *   `status_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '状态名称',
     *   `create_time` bigint NOT NULL COMMENT '创建时间',
     * @param numberStatusLogList
     * @return
     * @throws BizException
     */
    private List<String[]> numberStatusLogListToCsvList(List<NumberStatusLog> numberStatusLogList) throws BizException {
        List<String[]> csvList = new LinkedList<>();
        //构造表头
        csvList.add(new String[]{"检测号码", "消息", "请求类型", "号码余额", "号码归属地", "号码归属省", "号码归属市", "原运营商名称", "现运营商名称", "携号转网",
                "运营商类型", "状态名称", "创建时间", "请求地址", "请求参数", "请求返回"});
        //添加文本格式化，去掉回车，换行，TAB，防止csv中出错
        for (NumberStatusLog numberStatusLog : numberStatusLogList) {
            csvList.add(new String[]{
                    exportService.getCSVText(numberStatusLog.getPhone()),
                    exportService.getCSVText(numberStatusLog.getMessage()),
                    exportService.getCSVText(ProductEnum.NumberRequestStatusTypeEnum.getNumberRequestStatusType(numberStatusLog.getRequestType())),
                    exportService.getCSVText(numberStatusLog.getMobileFee()),
                    exportService.getCSVText(numberStatusLog.getArea()),
                    exportService.getCSVText(numberStatusLog.getProvince()),
                    exportService.getCSVText(numberStatusLog.getCity()),
                    exportService.getCSVText(numberStatusLog.getPriIspName()),
                    exportService.getCSVText(numberStatusLog.getNewIspName()),
                    exportService.getCSVText(numberStatusLog.getIsChange() == null ? "" : (numberStatusLog.getIsChange() == BaseConstant.ZERO_INT?"否":"是")),
                    exportService.getCSVText(numberStatusLog.getNumberType()),
                    exportService.getCSVText(numberStatusLog.getStatusName()),
                    exportService.getCSVDate(numberStatusLog.getCreateTime()),
                    exportService.getCSVText(numberStatusLog.getRequestUrl()),
                    exportService.getCSVText(numberStatusLog.getRequestBody()),
                    exportService.getCSVText(numberStatusLog.getRequestMsg())
            });
        }
        return csvList;
    }




    /**
     * 号码查询导入订单
     *
     * @param uploadOrderListExcelBO
     * @throws Exception
     */

    @Override
    public void uploadNumberListExcel(UploadNumberListExcelBO uploadOrderListExcelBO) throws Exception {
        if(uploadOrderListExcelBO==null||uploadOrderListExcelBO.getType()==null||uploadOrderListExcelBO.getFile()==null){
            throw new BizException("缺少必要的请求参数");
        }
        List<String> phoneList = readOrderListExcel(uploadOrderListExcelBO.getFile());
        if (CollectionUtils.isEmpty(phoneList)) {
            return;
        }
        //循环调用查询方法
        for(String phone:phoneList){
            try {
                getPhoneByType(phone,uploadOrderListExcelBO.getType());
            }catch (Exception e){
                log.info("号码{}查询失败,查询类型:{},错误:{}",phone,uploadOrderListExcelBO.getType(),e.getMessage());
            }
        }
    }

    /**
     * 读取文件数据
     *
     * @param file 文件
     * @return 需要返回的数据模板
     */
    private List<String> readOrderListExcel(MultipartFile file) {
        List<String> phoneList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            ExcelReader reader = ExcelUtil.getReader(inputStream, 0);
            List<Map<String, Object>> readAll = reader.readAll();
            int rowIndex = 0;
            for (Map<String, Object> map : readAll) {
                rowIndex++;
                checkOrderData(map, rowIndex);
                phoneList.add(exportService.getCSVText(Convert.toStr(map.get(ImportOrderConstant.ORDER_CUSTOM_PHONE))));
            }
        } catch (IOException | BizException e) {
            log.info("read excel error :{}", e.getMessage());
            e.printStackTrace();
        }
        return phoneList;
    }

    private void checkOrderData(Map<String, Object> map, Integer line) throws BizException {
        exportService.checkMobile(1, exportService.getCSVText(Convert.toStr(map.get(ImportOrderConstant.ORDER_CUSTOM_PHONE))), line);
    }

    /**
     * 炫咖 移动号码话费余额查询
     */
    public NumberStatusLog getMobileBalance(String phone) throws Exception {
        if(StringUtils.isEmpty(phone)){
            throw new BizException("号码不能为空");
        }
        Map<String, Object> form = new HashMap<>();
        form.put("key",NumberStatusConstant.KRY);
        form.put("mobile",phone);
        NumberStatusLog numberStatusLog = getNumberStatusLog(phone,NumberStatusConstant.MOBILE_BALANCE_URL, JSONObject.toJSONString(form),"","中国移动", BaseConstant.ZERO_INT);
        String msg = null;
        XkBalanceResponse xkBalanceResponse = null;
        try {
             msg = httpClient.postFormForString(NumberStatusConstant.MOBILE_BALANCE_URL,form,null);
             //msg = "{\"code\":200,\"msg\":\"获取成功\",\"data\":{\"mobile\":\"18257546964\",\"curFee\":55.36,\"mobile_fee\":55.36},\"exec_time\":3.636696,\"ip\":\"122.231.138.235\"}";
            //处理返回值 并保存日志
            xkBalanceResponse = buildResponseStr(msg, XkBalanceResponse.class);
        }catch (Exception e){
            numberStatusLog.setRequestMsg(msg);
            numberStatusLog.setMessage("查询失败:"+e.getMessage());
            numberStatusLogMapper.insert(numberStatusLog);
            throw e;
        }

        numberStatusLog.setMobileFee(xkBalanceResponse.getMobile_fee());
        numberStatusLog.setMessage("调用成功");
        numberStatusLog.setRequestMsg(msg);
        numberStatusLogMapper.insert(numberStatusLog);
        return numberStatusLog;
    }

    /**
     * 炫咖 联通号码话费余额查询
     */
    public NumberStatusLog getUnicomBalance(String phone) throws Exception {
        if(StringUtils.isEmpty(phone)){
            throw new BizException("号码不能为空");
        }
        Map<String, Object> form = new HashMap<>();
        form.put("key",NumberStatusConstant.KRY);
        form.put("mobile",phone);
        NumberStatusLog numberStatusLog = getNumberStatusLog(phone,NumberStatusConstant.UNICOM_BALANCE_URL, JSONObject.toJSONString(form),"","中国联通", BaseConstant.ONE_INT);
        String msg = null;
        XkBalanceResponse xkBalanceResponse =null;
        try {
            msg = httpClient.postFormForString(NumberStatusConstant.UNICOM_BALANCE_URL,form,null);
            //msg = "{\"code\":200,\"msg\":\"获取成功\",\"data\":{\"mobile\":\"18257546964\",\"curFee\":55.36,\"mobile_fee\":55.36},\"exec_time\":3.636696,\"ip\":\"122.231.138.235\"}";
            //处理返回值 并保存日志
            xkBalanceResponse = buildResponseStr(msg, XkBalanceResponse.class);
        }catch (Exception e){
            numberStatusLog.setRequestMsg(msg);
            numberStatusLog.setMessage("查询失败:"+e.getMessage());
            numberStatusLogMapper.insert(numberStatusLog);
            throw e;
        }

        numberStatusLog.setMobileFee(xkBalanceResponse.getMobile_fee());
        numberStatusLog.setMessage("调用成功");
        numberStatusLog.setRequestMsg(msg);
        numberStatusLogMapper.insert(numberStatusLog);
        return numberStatusLog;
    }

    /**
     * 炫咖 电信号码话费余额查询
     */
    public NumberStatusLog getTelecomBalance(String phone) throws Exception {
        if(StringUtils.isEmpty(phone)){
            throw new BizException("号码不能为空");
        }
        Map<String, Object> form = new HashMap<>();
        form.put("key",NumberStatusConstant.KRY);
        form.put("mobile",phone);
        NumberStatusLog numberStatusLog = getNumberStatusLog(phone,NumberStatusConstant.TELECOM_BALANCE_URL, JSONObject.toJSONString(form),"","中国电信", BaseConstant.TWO_INT);
        String msg = null;
        XkBalanceResponse xkBalanceResponse = null;
        try {
            msg = httpClient.postFormForString(NumberStatusConstant.TELECOM_BALANCE_URL,form,null);
            //msg = "{\"code\":200,\"msg\":\"获取成功\",\"data\":{\"mobile\":\"18257546964\",\"curFee\":55.36,\"mobile_fee\":55.36},\"exec_time\":3.636696,\"ip\":\"122.231.138.235\"}";
            xkBalanceResponse = buildResponseStr(msg, XkBalanceResponse.class);
        }catch (Exception e){
            numberStatusLog.setRequestMsg(msg);
            numberStatusLog.setMessage("查询失败:"+e.getMessage());
            numberStatusLogMapper.insert(numberStatusLog);
            throw e;
        }
        //处理返回值 并保存日志
        numberStatusLog.setMobileFee(xkBalanceResponse.getMobile_fee());
        numberStatusLog.setMessage("调用成功");
        numberStatusLog.setRequestMsg(msg);
        numberStatusLogMapper.insert(numberStatusLog);
        return numberStatusLog;
    }


    /**
     * 炫咖 携号转网查询
     *
     */
    public NumberStatusLog getNumberShift(String phone) throws Exception {
        if(StringUtils.isEmpty(phone)){
            throw new BizException("号码不能为空");
        }
        Map<String, Object> form = new HashMap<>();
        form.put("key",NumberStatusConstant.KRY);
        form.put("number",phone);
        NumberStatusLog numberStatusLog = getNumberStatusLog(phone,NumberStatusConstant.NUMBER_SHIFT_URL, JSONObject.toJSONString(form),"","", BaseConstant.THREE_INT);
        String msg = null;
        XkNumberShiftResponse xkNumberShiftResponse = null;
        try {
            msg = httpClient.getForString(NumberStatusConstant.NUMBER_SHIFT_URL,form,null);
            //msg = "{\"code\":200,\"msg\":\"获取成功\",\"data\":{\"number\":\"17342048826\",\"pri_isp\":\"china_telecom\",\"pri_isp_name\":\"中国电信\",\"new_isp\":\"china_telecom\",\"new_isp_name\":\"中国电信\",\"change\":\"0\",\"province\":\"浙江\",\"city\":\"杭州\"},\"exec_time\":0.847546,\"ip\":\"122.231.138.235\"}";
            //处理返回值 并保存日志
            xkNumberShiftResponse = buildResponseStr(msg, XkNumberShiftResponse.class);
        }catch (Exception e){
            numberStatusLog.setRequestMsg(msg);
            numberStatusLog.setMessage("查询失败:"+e.getMessage());
            numberStatusLogMapper.insert(numberStatusLog);
            throw e;
        }
        numberStatusLog.setProvince(xkNumberShiftResponse.getProvince());
        numberStatusLog.setCity(xkNumberShiftResponse.getCity());
        numberStatusLog.setPriIspName(xkNumberShiftResponse.getPri_isp_name());
        numberStatusLog.setNewIspName(xkNumberShiftResponse.getNew_isp_name());
        numberStatusLog.setIsChange(xkNumberShiftResponse.getChange());
        numberStatusLog.setMessage("调用成功");
        numberStatusLog.setRequestMsg(msg);
        numberStatusLogMapper.insert(numberStatusLog);
        return numberStatusLog;
    }



    /**
     * 炫咖 号码查询查询
     *
     */
    public NumberStatusLog getNumberQuery(String phone) throws Exception {
        if(StringUtils.isEmpty(phone)){
            throw new BizException("号码不能为空");
        }
        Map<String, Object> form = new HashMap<>();
        form.put("key",NumberStatusConstant.KRY);
        form.put("number",phone);
        NumberStatusLog numberStatusLog = getNumberStatusLog(phone,NumberStatusConstant.NUMBER_QUERY_URL, JSONObject.toJSONString(form),"","", BaseConstant.FOUR_INT);
        String msg = null;
        XkNumberQueryResponse xkNumberQueryResponse = null;
        try {
            msg = httpClient.postFormForString(NumberStatusConstant.NUMBER_QUERY_URL,form,null);
            //msg = "{\"code\":200,\"msg\":\"获取成功\",\"data\":{\"mobile\":\"18257546964\",\"curFee\":55.36,\"mobile_fee\":55.36},\"exec_time\":3.636696,\"ip\":\"122.231.138.235\"}";
            //处理返回值 并保存日志
            xkNumberQueryResponse = buildResponseStr(msg, XkNumberQueryResponse.class);
        }catch (Exception e){
            numberStatusLog.setRequestMsg(msg);
            numberStatusLog.setMessage("查询失败:"+e.getMessage());
            numberStatusLogMapper.insert(numberStatusLog);
            throw e;
        }
        numberStatusLog.setNumberType(xkNumberQueryResponse.getType_name());
        numberStatusLog.setStatusName(xkNumberQueryResponse.getStatus_name());
        numberStatusLog.setArea(xkNumberQueryResponse.getArea());
        numberStatusLog.setIsChange(xkNumberQueryResponse.getMnpStatus());
        numberStatusLog.setMessage("调用成功");
        numberStatusLog.setRequestMsg(msg);
        numberStatusLogMapper.insert(numberStatusLog);
        return numberStatusLog;
    }


    /**
     * 炫咖 空号检测
     *
     */
    public NumberStatusLog getEmptyNumberQuery(String phone) throws Exception {
        if(StringUtils.isEmpty(phone)){
            throw new BizException("号码不能为空");
        }
        Map<String, Object> form = new HashMap<>();
        form.put("key",NumberStatusConstant.KRY);
        form.put("number",phone);
        NumberStatusLog numberStatusLog = getNumberStatusLog(phone,NumberStatusConstant.EMPTY_NUMBER_QUERY_URL, JSONObject.toJSONString(form),"","", BaseConstant.FIVE_INT);
        String msg = null;
        XkEmptyNumberQueryResponse xkEmptyNumberQueryResponse = null;
        try {
            msg = httpClient.postFormForString(NumberStatusConstant.EMPTY_NUMBER_QUERY_URL,form,null);
            //msg = "{\"code\":200,\"msg\":\"获取成功\",\"data\":{\"mobile\":\"18257546964\",\"curFee\":55.36,\"mobile_fee\":55.36},\"exec_time\":3.636696,\"ip\":\"122.231.138.235\"}";
            //处理返回值 并保存日志
            xkEmptyNumberQueryResponse = buildResponseStr(msg, XkEmptyNumberQueryResponse.class);
        }catch (Exception e){
            numberStatusLog.setRequestMsg(msg);
            numberStatusLog.setMessage("查询失败:"+e.getMessage());
            numberStatusLogMapper.insert(numberStatusLog);
            throw e;
        }
        numberStatusLog.setNumberType(xkEmptyNumberQueryResponse.getNumberType());
        numberStatusLog.setStatusName(xkEmptyNumberQueryResponse.getStatus_name());
        numberStatusLog.setArea(xkEmptyNumberQueryResponse.getArea());
        numberStatusLog.setMessage("调用成功");
        numberStatusLog.setRequestMsg(msg);
        numberStatusLogMapper.insert(numberStatusLog);
        return numberStatusLog;
    }


    public NumberStatusLog getNumberStatusLog(String phone,String url,String requestBody,String requestMsg,String numberType,Integer requestType){
        NumberStatusLog numberStatusLog = new NumberStatusLog();
        numberStatusLog.setPhone(phone);
        numberStatusLog.setRequestUrl(url);
        numberStatusLog.setRequestBody(requestBody);
        numberStatusLog.setRequestMsg(requestMsg);
        numberStatusLog.setNumberType(numberType);
        numberStatusLog.setRequestType(requestType);
        numberStatusLog.setCreateTime(System.currentTimeMillis());
        return numberStatusLog;
    }

    /**
     * 返回值统一解析类
     *
     * @param response
     * @param clz
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> T buildResponseStr(String response, Class<T> clz) throws Exception {
        if (StrUtil.isBlankIfStr(response)) {
            throw new BizException("接口返回空");
        }
        XkBaseResponse baseResponse = JSONObject.parseObject(response, XkBaseResponse.class);
        if (baseResponse == null) {
            throw new BizException(StrUtil.format("接口返回解码失败"));
        }
        if (!(NumberStatusConstant.RESPONSE_SUCCESS == baseResponse.getCode())) {
            //code值非成功
            throw new BizException(StrUtil.format("接口返回错误:{}", baseResponse.getMsg()));
        }
        T realResponse = JSONObject.parseObject(JSONObject.toJSONString(baseResponse.getData()), clz);
        if (realResponse == null) {
            throw new BizException(StrUtil.format("接口返回body空"));
        }
        return realResponse;
    }


    //==============额查查===============================//


    /**
     * 额查查 号码话费余额查询
     */
    public NumberStatusLog getEccBalance(String phone) throws Exception {
        if(StringUtils.isEmpty(phone)){
            throw new BizException("号码不能为空");
        }
        Map<String, Object> form = new HashMap<>();
        form.put("appid",NumberStatusConstant.ECC_APP_ID);
        form.put("appkey",NumberStatusConstant.ECC_APP_KEY);
        form.put("phone",phone);
        form.put("phonetype",getEccPhoneGsd(phone));
        NumberStatusLog numberStatusLog = getNumberStatusLog(phone,NumberStatusConstant.ECC_BALANCE_URL, JSONObject.toJSONString(form),"","", BaseConstant.SEX_INT);
        String msg = null;
        EccBalanceResponse eccBalanceResponse =null;
        try {
            msg = httpClient.getForString(NumberStatusConstant.ECC_BALANCE_URL,form,null);
            //msg = "{\"code\":200,\"msg\":\"获取成功\",\"data\":{\"mobile\":\"18257546964\",\"curFee\":55.36,\"mobile_fee\":55.36},\"exec_time\":3.636696,\"ip\":\"122.231.138.235\"}";
            //处理返回值 并保存日志
            eccBalanceResponse = buildResponseStr(msg, EccBalanceResponse.class);
        }catch (Exception e){
            numberStatusLog.setRequestMsg(msg);
            numberStatusLog.setMessage("查询失败:"+e.getMessage());
            numberStatusLogMapper.insert(numberStatusLog);
            throw e;
        }
        numberStatusLog.setMobileFee(eccBalanceResponse.getCurFee());
        numberStatusLog.setProvince(eccBalanceResponse.getProv());
        numberStatusLog.setCity(eccBalanceResponse.getCity());
        numberStatusLog.setPriIspName(eccBalanceResponse.getOri_carrier());
        numberStatusLog.setNewIspName(eccBalanceResponse.getNew_carrier());
        if(StringUtils.isNotEmpty(eccBalanceResponse.getOri_carrier())&&StringUtils.isNotEmpty(eccBalanceResponse.getNew_carrier())){
            if(eccBalanceResponse.getOri_carrier().equals(eccBalanceResponse.getNew_carrier())){
                numberStatusLog.setIsChange(0);
            }else {
                numberStatusLog.setIsChange(1);
            }
        }
        numberStatusLog.setMessage("调用成功");
        numberStatusLog.setRequestMsg(msg);
        numberStatusLogMapper.insert(numberStatusLog);
        return numberStatusLog;
    }

    /**
     * 额查查 获取号码归属地
     * @param phone
     * @return
     */
    public String getEccPhoneGsd(String phone) throws BizException {
        if(StringUtils.isEmpty(phone)){
            throw new BizException("号码不能为空");
        }
        Map<String, Object> form = new HashMap<>();
        form.put("appid",NumberStatusConstant.ECC_APP_ID);
        form.put("appkey",NumberStatusConstant.ECC_APP_KEY);
        form.put("phone",phone);
        String msg = null;
        EccEmptyNumberQueryResponse eccBalanceResponse =null;
        try {
            msg = httpClient.getForString(NumberStatusConstant.ECC_GSD_URL,form,null);
            //处理返回值 并保存日志
            eccBalanceResponse = buildResponseStr(msg, EccEmptyNumberQueryResponse.class);
            return eccBalanceResponse.getIsp();
        }catch (Exception e){
            throw new BizException("获取号码归属地失败");
        }
    }



    /**
     * 额查查 携号转网查询
     *
     */
    public NumberStatusLog getEccNumberShift(String phone) throws Exception {
        if(StringUtils.isEmpty(phone)){
            throw new BizException("号码不能为空");
        }
        Map<String, Object> form = new HashMap<>();
        form.put("appid",NumberStatusConstant.ECC_APP_ID);
        form.put("appkey",NumberStatusConstant.ECC_APP_KEY);
        form.put("phone",phone);
        NumberStatusLog numberStatusLog = getNumberStatusLog(phone,NumberStatusConstant.ECC_NUMBER_SHIFT_URL, JSONObject.toJSONString(form),"","", BaseConstant.SEVEN_INT);
        String msg = null;
        EccNumberShiftResponse eccNumberShiftResponse  = null;
        try {
            msg = httpClient.getForString(NumberStatusConstant.ECC_NUMBER_SHIFT_URL,form,null);
            //处理返回值 并保存日志
            eccNumberShiftResponse = buildResponseStr(msg, EccNumberShiftResponse.class);
        }catch (Exception e){
            numberStatusLog.setRequestMsg(msg);
            numberStatusLog.setMessage("查询失败:"+e.getMessage());
            numberStatusLogMapper.insert(numberStatusLog);
            throw e;
        }
        numberStatusLog.setProvince(eccNumberShiftResponse.getProv());
        numberStatusLog.setCity(eccNumberShiftResponse.getCity());
        numberStatusLog.setPriIspName(eccNumberShiftResponse.getOri_carrier());
        numberStatusLog.setNewIspName(eccNumberShiftResponse.getNew_carrier());
        if(StringUtils.isNotEmpty(eccNumberShiftResponse.getOri_carrier())&&StringUtils.isNotEmpty(eccNumberShiftResponse.getNew_carrier())){
            if(eccNumberShiftResponse.getOri_carrier().equals(eccNumberShiftResponse.getNew_carrier())){
                numberStatusLog.setIsChange(0);
            }else {
                numberStatusLog.setIsChange(1);
            }
        }
        numberStatusLog.setMessage("调用成功");
        numberStatusLog.setRequestMsg(msg);
        numberStatusLogMapper.insert(numberStatusLog);
        return numberStatusLog;
    }



    /**
     * 额查查 空号检测
     *
     http://api.qiaosuan.net/api/queryKH?appid=&appkey=&phone=
     *
     */
    public NumberStatusLog getEccEmptyNumberQuery(String phone) throws Exception {
        if(StringUtils.isEmpty(phone)){
            throw new BizException("号码不能为空");
        }
        Map<String, Object> form = new HashMap<>();
        form.put("appid",NumberStatusConstant.ECC_APP_ID);
        form.put("appkey",NumberStatusConstant.ECC_APP_KEY);
        form.put("phone",phone);
        NumberStatusLog numberStatusLog = getNumberStatusLog(phone,NumberStatusConstant.EMPTY_NUMBER_QUERY_URL, JSONObject.toJSONString(form),"","", BaseConstant.EIGHT_INT);
        String msg = null;
        EccEmptyNumberQueryResponse eccEmptyNumberQueryResponse = null;
        try {
            msg = httpClient.getForString(NumberStatusConstant.ECC_EMPTY_NUMBER_QUERY_URL,form,null);
            //处理返回值 并保存日志
            eccEmptyNumberQueryResponse = buildResponseStr(msg, EccEmptyNumberQueryResponse.class);
        }catch (Exception e){
            numberStatusLog.setRequestMsg(msg);
            numberStatusLog.setMessage("查询失败:"+e.getMessage());
            numberStatusLogMapper.insert(numberStatusLog);
            throw e;
        }
        numberStatusLog.setNumberType(eccEmptyNumberQueryResponse.getIsp_name());
        numberStatusLog.setStatusName(eccEmptyNumberQueryResponse.getDesc());
        numberStatusLog.setArea(eccEmptyNumberQueryResponse.getProv());
        numberStatusLog.setMessage("调用成功");
        numberStatusLog.setRequestMsg(msg);
        numberStatusLogMapper.insert(numberStatusLog);
        return numberStatusLog;
    }

    //================================================//

}
