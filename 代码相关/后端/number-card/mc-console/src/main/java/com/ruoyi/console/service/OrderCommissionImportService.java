package com.ruoyi.console.service;


import com.ruoyi.common.order.bo.UploadNumberListExcelBO;

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

}
