package com.ruoyi.console.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.ImportOrderConstant;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.UploadNumberListExcelBO;
import com.ruoyi.common.order.entity.OrderCommission;
import com.ruoyi.console.mapper.OrderCommissionMapper;
import com.ruoyi.console.service.ExportService;
import com.ruoyi.console.service.OrderCommissionImportService;
import com.ruoyi.console.service.OrderCommissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:13
 */
@Service
@Slf4j
public class OrderCommissionImportServiceImpl  implements OrderCommissionImportService {

    @Resource
    ExportService exportService;

    @Resource
    OrderCommissionMapper orderCommissionMapper;

    @Resource
    OrderCommissionService orderCommissionService;


    /**
     * 导入佣金结算数据
     *
     * @param uploadOrderListExcelBO
     * @throws Exception
     */
    @Override
    public void uploadOrderCommissionExcel(UploadNumberListExcelBO uploadOrderListExcelBO) throws Exception {
        if(uploadOrderListExcelBO==null||uploadOrderListExcelBO.getFile()==null){
            throw new BizException("缺少必要的请求参数");
        }
        List<String> orderList = readOrderListExcel(uploadOrderListExcelBO.getFile());
        if (CollectionUtils.isEmpty(orderList)) {
            return;
        }
        //判断是否存在佣金记录
        List<OrderCommission>  orderCommissionList = orderCommissionMapper.selectList(new LambdaQueryWrapper<OrderCommission>().in(OrderCommission::getOrderId,orderList));
        if(!CollectionUtils.isEmpty(orderCommissionList)){
            String orderIdsString = orderCommissionList.stream().map(OrderCommission::getOrderId).map(String::valueOf).collect(Collectors.joining(","));
            throw new BizException("{} 已存在佣金记录",orderIdsString);
        }
        //异步生成订单佣金记录
        orderCommissionService.saveOrderCommission(orderList);
    }

    /**
     * 读取文件数据
     *
     * @param file 文件
     * @return 需要返回的数据模板
     */
    private List<String> readOrderListExcel(MultipartFile file) throws IOException, BizException {
        List<String> phoneList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            ExcelReader reader = ExcelUtil.getReader(inputStream, 0);
            List<Map<String, Object>> readAll = reader.readAll();
            int rowIndex = 0;
            for (Map<String, Object> map : readAll) {
                rowIndex++;
                checkOrderData(map, rowIndex);
                phoneList.add(exportService.getCSVText(Convert.toStr(map.get(ImportOrderConstant.ORDER_ID))));
            }
        } catch (IOException | BizException e) {
            log.info("read excel error :{}", e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return phoneList;
    }

    private void checkOrderData(Map<String, Object> map, Integer line) throws BizException {
        exportService.checkNotNull(1, exportService.getCSVText(Convert.toStr(map.get(ImportOrderConstant.ORDER_ID))), line);
    }
}
