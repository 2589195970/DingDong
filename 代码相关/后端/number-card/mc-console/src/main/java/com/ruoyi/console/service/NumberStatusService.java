package com.ruoyi.console.service;


import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.OrderNumberStatusLogSelectBO;
import com.ruoyi.common.order.bo.UploadNumberListExcelBO;
import com.ruoyi.common.order.entity.NumberStatusLog;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
public interface NumberStatusService {


    /**
     * 根据类型查询号码数据
     */
    NumberStatusLog getPhoneByType(String phone,Integer type) throws Exception;


    /**
     * 余额查询日志查询
     *
     * @return
     * @throws BizException
     */
    PageResult<NumberStatusLog> selectNumberStatusLogListPage(OrderNumberStatusLogSelectBO orderNumberStatusLogSelectBO) throws BizException;


    /**
     * 余额查询日志导出
     *
     * @return
     * @throws BizException
     */
    void exportNumberStatusLogList(OrderNumberStatusLogSelectBO orderNumberStatusLogSelectBO, HttpServletResponse response) throws Exception;


    /**
     * 号码查询导入订单
     *
     * @param uploadOrderListExcelBO
     * @throws Exception
     */

    void uploadNumberListExcel(UploadNumberListExcelBO uploadOrderListExcelBO) throws Exception;


    /**
     * 炫咖 移动号码话费余额查询
     */
    NumberStatusLog getMobileBalance(String phone) throws Exception;


    /**
     * 炫咖 联通号码话费余额查询
     */
    NumberStatusLog getUnicomBalance(String phone) throws Exception;


    /**
     * 炫咖 电信号码话费余额查询
     */
    NumberStatusLog getTelecomBalance(String phone) throws Exception;


    /**
     * 炫咖 携号转网查询
     *
     */
    NumberStatusLog getNumberShift(String phone) throws Exception;


    /**
     * 炫咖 号码查询查询
     *
     */
    NumberStatusLog getNumberQuery(String phone) throws Exception;

    /**
     * 炫咖 空号检测
     *
     */
    NumberStatusLog getEmptyNumberQuery(String phone) throws Exception;

    /**
     * 额查查 号码话费余额查询
     *
     */
    NumberStatusLog getEccBalance(String phone) throws Exception;

    /**
     * 额查查 携号转网查询
     *
     */
    NumberStatusLog getEccNumberShift(String phone) throws Exception;

    /**
     * 额查查 空号检测
     *
     */
    NumberStatusLog getEccEmptyNumberQuery(String phone) throws Exception;
}
