package com.ruoyi.console.service;


import com.ruoyi.common.order.bo.UploadNumberListExcelBO;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface OrderCommissionImportService  {



    /**
     * 导入佣金
     *
     * @param uploadOrderListExcelBO
     * @throws Exception
     */
    void uploadOrderCommissionExcel(UploadNumberListExcelBO uploadOrderListExcelBO) throws Exception;

    /**
     * 下载佣金导入模板
     *
     * @param response 响应
     * @throws Exception 异常
     */
    void downloadOrderCommissionTemplate(HttpServletResponse response) throws Exception;

}
