package com.ruoyi.console.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.ImportOrderConstant;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.enums.ProductEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.order.bo.AgainOrderBO;
import com.ruoyi.common.order.bo.OrderSelectBO;
import com.ruoyi.common.order.bo.UpdateOrderStatusBO;
import com.ruoyi.common.order.bo.UploadOrderListExcelBO;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.reuqest.ApiCommonNotifyRequest;
import com.ruoyi.common.order.reuqest.OrderSubmitRequest;
import com.ruoyi.common.order.vo.OrderSelectVO;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.OrderLogMapper;
import com.ruoyi.console.mapper.OrderMapper;
import com.ruoyi.console.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:13
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    OrderLogMapper orderLogMapper;

    @Value(value = "${submit.url}")
    private String submitOrderUrl;

    @Value(value = "${submit.callback}")
    private String callbackOrderUrl;

    @Resource
    HttpClient httpClient;

    @Resource
    ExportService exportService;

    @Resource
    ProductService productService;

    @Resource
    AgentAccountService agentAccountService;

    @Resource
    ImportOrderService importOrderService;

    @Resource
    NumberStatusService numberStatusService;

    /**
     * 订单查询
     *
     * @param orderSelectBO
     * @return
     * @throws BizException
     */
    public PageResult<OrderSelectVO> selectOrderListPage(OrderSelectBO orderSelectBO) throws BizException {
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(1L, true);
        //读取分页
        Page page = new Page(orderSelectBO.getPageNo(), orderSelectBO.getPageSize());
        LambdaQueryWrapper<Order> lambdaQueryWrapper= new LambdaQueryWrapper<Order>()
                .eq(StringUtils.isNotEmpty(orderSelectBO.getOrderId()), Order::getOrderId, orderSelectBO.getOrderId())
                .eq(StringUtils.isNotEmpty(orderSelectBO.getOrderUpstreamId()), Order::getOrderUpstreamId, orderSelectBO.getOrderUpstreamId())
                .eq(StringUtils.isNotEmpty(orderSelectBO.getOrderDownstreamId()), Order::getOrderDownstreamId, orderSelectBO.getOrderDownstreamId())
                .like(StringUtils.isNotEmpty(orderSelectBO.getCardName()), Order::getCardName, orderSelectBO.getCardName())
                .eq(StringUtils.isNotEmpty(orderSelectBO.getCardPhone()), Order::getCardPhone, orderSelectBO.getCardPhone())
                .eq(StringUtils.isNotEmpty(orderSelectBO.getCardId()), Order::getCardId, orderSelectBO.getCardId())
                .eq(orderSelectBO.getOrderStatus() != null, Order::getOrderStatus, orderSelectBO.getOrderStatus())
                .eq(StringUtils.isNotEmpty(orderSelectBO.getUpstreamApi()), Order::getUpstreamApi, orderSelectBO.getUpstreamApi())
                .eq(orderSelectBO.getIsRecharged() != null, Order::getIsRecharged, orderSelectBO.getIsRecharged())
                .eq(StringUtils.isNotEmpty(orderSelectBO.getAccNumber()), Order::getCardPhone, orderSelectBO.getAccNumber())
                .eq(orderSelectBO.getOrderSource() != null, Order::getOrderSource, orderSelectBO.getOrderSource())
                .eq(orderSelectBO.getOrderCommissionStatus() != null, Order::getOrderCommissionStatus, orderSelectBO.getOrderCommissionStatus())
                //此处查的是账号下归属代理的订单 所以只要归属中出现就要查出来
                .like(StringUtils.isNotEmpty(orderSelectBO.getDownstreamCode()), Order::getDownstreamFatherList, orderSelectBO.getDownstreamCode())
                //查询时间范围
                .between((orderSelectBO.getStarTime() != null && orderSelectBO.getEndTime() != null), Order::getCreateTime, orderSelectBO.getStarTime(), orderSelectBO.getEndTime())
                .orderByDesc(Order::getCreateTime);
        if(orderSelectBO.getIsNotNullOrderUpstreamId()!=null){
            if(BaseConstant.ZERO_INT == orderSelectBO.getIsNotNullOrderUpstreamId()){
                lambdaQueryWrapper.isNull(Order::getOrderUpstreamId);
            }else if( BaseConstant.ONE_INT == orderSelectBO.getIsNotNullOrderUpstreamId()){
                lambdaQueryWrapper.isNotNull(Order::getOrderUpstreamId);
            }
        }
        Page<Order> orderPage = baseMapper.selectPage(page, lambdaQueryWrapper);
        Page<OrderSelectVO> orderSelectVOPage = new Page<>();
        BeanUtil.copyProperties(orderPage, orderSelectVOPage);
        if (!CollectionUtils.isEmpty(orderPage.getRecords())) {
            List<OrderSelectVO> orderSelectVOList = new ArrayList<>();
            for (Order order : orderPage.getRecords()) {
                OrderSelectVO orderSelectVO = new OrderSelectVO();
                BeanUtil.copyProperties(order, orderSelectVO);
                orderSelectVO.setOrderId(order.getOrderId() + "");
                AgentCommissionJson agentCommissionJson = getShowDownstreamInfo(agentAccount, order);
                orderSelectVO.setShowDownstreamCode(agentCommissionJson.getAgentCode());
                orderSelectVO.setShowDownstreamName(agentCommissionJson.getAgentName());
                //查询产品头图
                Product product = productService.getProductNotStatus(order.getProductCode());
                if(product!=null){
                    orderSelectVO.setProductMasterMap(product.getProductMasterMap());
                }
                orderSelectVOList.add(orderSelectVO);
            }
            orderSelectVOPage.setRecords(orderSelectVOList);
        }
        return PageResultFactory.createPageResult(orderSelectVOPage);
    }

    public AgentCommissionJson getShowDownstreamInfo(AgentAccount agentAccount, Order order) throws BizException {
        AgentCommissionJson agentCommissionJson = new AgentCommissionJson();
        if (StringUtils.isNotEmpty(order.getDownstreamCode()) && order.getDownstreamCode().equals(agentAccount.getAgentCode())) {
            //如果是本人进的单 下游代理商展示为本人
            agentCommissionJson.setAgentCode(order.getDownstreamCode());
            agentCommissionJson.setAgentName(order.getDownstreamName());
            return agentCommissionJson;
        }
        //如果不是 判断是哪个下游的 取当前代理下一级作为展示项
        List<AgentCommissionJson> agentCommissionJsons = JSONObject.parseArray(order.getDownstreamFatherList(), AgentCommissionJson.class);
        if (CollectionUtils.isEmpty(agentCommissionJsons)) {
            return agentCommissionJson;
        }
        for (int i = 0; i < agentCommissionJsons.size(); i++) {
            if (agentCommissionJsons.get(i).getAgentCode().equals(agentAccount.getAgentCode())) {
                if ((i + 1) < agentCommissionJsons.size()) {
                    return agentCommissionJsons.get(i + 1);
                }
            }
        }
        return agentCommissionJson;
    }

    /**
     * 订单导出
     *
     * @param orderSelectBO
     * @return
     * @throws BizException
     */
    public void exportOrderList(OrderSelectBO orderSelectBO, HttpServletResponse response) throws Exception {
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(1L, true);

        LambdaQueryWrapper<Order> lambdaQueryWrapper= new LambdaQueryWrapper<Order>()
                .eq(StringUtils.isNotEmpty(orderSelectBO.getOrderId()), Order::getOrderId, orderSelectBO.getOrderId())
                .eq(StringUtils.isNotEmpty(orderSelectBO.getOrderUpstreamId()), Order::getOrderUpstreamId, orderSelectBO.getOrderUpstreamId())
                .eq(StringUtils.isNotEmpty(orderSelectBO.getOrderDownstreamId()), Order::getOrderDownstreamId, orderSelectBO.getOrderDownstreamId())
                .like(StringUtils.isNotEmpty(orderSelectBO.getCardName()), Order::getCardName, orderSelectBO.getCardName())
                .eq(StringUtils.isNotEmpty(orderSelectBO.getCardPhone()), Order::getCardPhone, orderSelectBO.getCardPhone())
                .eq(StringUtils.isNotEmpty(orderSelectBO.getCardId()), Order::getCardId, orderSelectBO.getCardId())
                .eq(orderSelectBO.getOrderStatus() != null, Order::getOrderStatus, orderSelectBO.getOrderStatus())
                .eq(StringUtils.isNotEmpty(orderSelectBO.getUpstreamApi()), Order::getUpstreamApi, orderSelectBO.getUpstreamApi())
                .eq(orderSelectBO.getIsRecharged() != null, Order::getIsRecharged, orderSelectBO.getIsRecharged())
                .eq(StringUtils.isNotEmpty(orderSelectBO.getAccNumber()), Order::getCardPhone, orderSelectBO.getAccNumber())
                .eq(orderSelectBO.getOrderSource() != null, Order::getOrderSource, orderSelectBO.getOrderSource())
                .eq(orderSelectBO.getOrderCommissionStatus() != null, Order::getOrderCommissionStatus, orderSelectBO.getOrderCommissionStatus())
                //此处查的是账号下归属代理的订单 所以只要归属中出现就要查出来
                .like(StringUtils.isNotEmpty(orderSelectBO.getDownstreamCode()), Order::getDownstreamFatherList, orderSelectBO.getDownstreamCode())
                //查询时间范围
                .between((orderSelectBO.getStarTime() != null && orderSelectBO.getEndTime() != null), Order::getCreateTime, orderSelectBO.getStarTime(), orderSelectBO.getEndTime())
                .orderByDesc(Order::getCreateTime);

        if(orderSelectBO.getIsNotNullOrderUpstreamId() == BaseConstant.ZERO_INT){
            lambdaQueryWrapper.isNull(Order::getOrderUpstreamId);
        }else if(orderSelectBO.getIsNotNullOrderUpstreamId() == BaseConstant.ONE_INT){
            lambdaQueryWrapper.isNotNull(Order::getOrderUpstreamId);
        }
        List<Order> orderList = baseMapper.selectList(lambdaQueryWrapper);
        List<OrderSelectVO> orderSelectVOList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(orderList)){
            for (Order order : orderList) {
                OrderSelectVO orderSelectVO = new OrderSelectVO();
                BeanUtil.copyProperties(order, orderSelectVO);
                orderSelectVO.setOrderId(order.getOrderId() + "");
                AgentCommissionJson agentCommissionJson = getShowDownstreamInfo(agentAccount, order);
                orderSelectVO.setShowDownstreamCode(agentCommissionJson.getAgentCode());
                orderSelectVO.setShowDownstreamName(agentCommissionJson.getAgentName());
                Product product = productService.getProductNotStatus(order.getProductCode());
                if(product!=null){
                    orderSelectVO.setProductDemand(product.getProductDemand());
                    orderSelectVO.setProductCommission(product.getProductCommission());
                }
                orderSelectVOList.add(orderSelectVO);
            }
        }
        //数据导出
        exportService.writeCsvResponse(response, orderListToCsvList(orderSelectVOList),"attachment;filename=order.csv");

    }

    /**
     * `order_message` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '订单说明信息',
     * `product_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '产品编码',
     * `product_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '产品名称',
     * `product_type` int DEFAULT NULL COMMENT '产品类型',
     * `upstream_api` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '上游API',
     * `upstream_api_name` varchar(255) DEFAULT NULL COMMENT '上游API名称',
     * `upstream_product_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '上游产品CODE',
     * `upstream_product_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '上游产品名称',
     * `upstream_order_status` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '上游订单状态',
     * `upstream_order_status_message` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '上游订单状态说明',
     * `upstream_push_time` bigint DEFAULT NULL COMMENT '订单向上游推送时间',
     * `downstream_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '下游代理商code',
     * `downstream_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '下游代理商名称',
     * `downstream_father_list` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '下游代理商归属列表 json格式 记录订单各级代理及佣金信息',
     * `is_recharged` int DEFAULT NULL COMMENT '是否充值 0 未充值 1已充值',
     * `recharge_amount` varchar(255) DEFAULT NULL COMMENT '充值信息',
     * `recharge_time` bigint DEFAULT NULL COMMENT '充值时间',
     * `express` varchar(255) DEFAULT NULL COMMENT '快递公司',
     * `tracking_number` varchar(255) DEFAULT NULL COMMENT '快递单号',
     *
     * @param list
     * @return
     * @throws BizException
     */
    private List<String[]> orderListToCsvList(List<OrderSelectVO> list) throws BizException {
        List<String[]> csvList = new LinkedList<>();
        //构造表头
        csvList.add(new String[]{"订单ID", "上游订单号", "下游订单号", "用户姓名", "用户手机号", "用户身份证", "省编码", "省名称", "市编码", "市名称", "区编码", "区名称", "详细地址",
                "订单状态", "订单说明信息", "产品编码", "产品名称", "产品类型", "是否充值", "充值信息", "充值时间", "快递公司", "快递单号", "发货时间", "激活时间", "生产号码", "运营商类型"
                ,"订单来源","归属代理商名称","进单代理商名称","产品推广要求","产品佣金","佣金状态","佣金说明"});
        //添加文本格式化，去掉回车，换行，TAB，防止csv中出错
        for (OrderSelectVO order : list) {
            csvList.add(new String[]{
                    exportService.getCSVText(order.getOrderId() != null ? String.valueOf(order.getOrderId()) : null),
                    exportService.getCSVText(order.getOrderUpstreamId() != null ? String.valueOf(order.getOrderUpstreamId()) : null),
                    exportService.getCSVText(order.getOrderDownstreamId() != null ? String.valueOf(order.getOrderDownstreamId()) : null),
                    exportService.getCSVText(order.getCardName()),
                    exportService.getCSVText(order.getCardPhone()),
                    exportService.getCSVText(order.getCardId()),
                    exportService.getCSVText(order.getProvinceCode()),
                    exportService.getCSVText(order.getProvinceName()),
                    exportService.getCSVText(order.getCityCode()),
                    exportService.getCSVText(order.getCityName()),
                    exportService.getCSVText(order.getCountyCode()),
                    exportService.getCSVText(order.getCountyName()),
                    exportService.getCSVText(order.getCardAddress()),
                    exportService.getCSVText(OrderEnum.getOrderMessageByStatus(order.getOrderStatus())),
                    exportService.getCSVText(order.getOrderMessage()),
                    exportService.getCSVText(order.getProductCode()),
                    exportService.getCSVText(order.getProductName()),
                    exportService.getCSVText(ProductEnum.getProductMessageByStatus(Integer.valueOf(order.getProductType()))),
                    exportService.getCSVText(order.getIsRecharged() == BaseConstant.ONE_INT ? "已充值" : "未充值"),
                    exportService.getCSVText(order.getRechargeAmount()),
                    exportService.getCSVDate(order.getRechargeTime()),
                    exportService.getCSVText(order.getExpress()),
                    exportService.getCSVText(order.getTrackingNumber()),
                    exportService.getCSVDate(order.getDeliveryTime()),
                    exportService.getCSVDate(order.getActiveTime()),
                    exportService.getCSVText(order.getAccNumber()),
                    exportService.getCSVText(ProductEnum.OperatorTypeEnum.getOperatorTypeByStatus(order.getOperatorType())),
                    exportService.getCSVText(OrderEnum.OrderSourceEnum.getSourceName(order.getOrderSource())),
                    exportService.getCSVText(order.getShowDownstreamName()),
                    exportService.getCSVText(order.getDownstreamName()),
                    exportService.getCSVText(order.getProductDemand()),
                    exportService.getCSVText(order.getProductCommission()+""),
                    exportService.getCSVText(OrderEnum.OrderCommissionEnum.getCommissionByStatus(order.getOrderCommissionStatus())),
                    exportService.getCSVText(order.getOrderCommissionMessage())
            });
        }
        return csvList;
    }


    /**
     * 重推订单
     *
     * @return
     * @throws BizException
     */
    @Async("orderSaveExecutor")
    public void againOrderSubmit(AgainOrderBO againOrderBO) throws Exception {
        Order order = baseMapper.selectById(againOrderBO.getOrderId());
        OrderSubmitRequest orderSubmitRequest = new OrderSubmitRequest();
        BeanUtil.copyProperties(order, orderSubmitRequest);
        orderSubmitRequest.setProductCode(againOrderBO.getProductCode());
        orderSubmitRequest.setAgentCode(order.getDownstreamCode());
        orderSubmitRequest.setOrderSource(OrderEnum.OrderSourceEnum.RE_PUSH.getSourceType());
        orderSubmitRequest.setCardName(StringUtils.isNotEmpty(againOrderBO.getCardName())?againOrderBO.getCardName():order.getCardName());
        orderSubmitRequest.setCardPhone(StringUtils.isNotEmpty(againOrderBO.getCardPhone())?againOrderBO.getCardPhone():order.getCardPhone());
        orderSubmitRequest.setCardId(StringUtils.isNotEmpty(againOrderBO.getCardId())?againOrderBO.getCardId():order.getCardId());
        //此处http调用一次order服务
        String msg = httpClient.postJsonForString(submitOrderUrl, orderSubmitRequest, null);
        log.info("重推订单返回:{},重推前订单号:{}",msg,order.getOrderId());
    }


    /**
     * 订单日志信息
     *
     * @return
     * @throws BizException
     */
    public List<OrderLog> selectOrderLogList(String orderId) throws BizException {
        List<OrderLog> orderLogList = orderLogMapper.selectList(new LambdaQueryWrapper<OrderLog>().eq(OrderLog::getOrderId, orderId)
                .orderByDesc(OrderLog::getCreateTime)
        );
        return orderLogList;
    }


    /**
     * 订单状态变更
     *
     * @return
     * @throws BizException
     */
    public void updateOrderStatus(UpdateOrderStatusBO updateOrderStatusBO) throws Exception {
        //调用一次回调 发货激活等
        Order order = baseMapper.selectById(updateOrderStatusBO.getOrderId());
        if (order == null) {
            throw new BizException("订单{}不存在", updateOrderStatusBO.getOrderId());
        }
        if (OrderEnum.INVALID.getStatus().equals(Integer.valueOf(updateOrderStatusBO.getOrderStatus()))) {
            //失败状态
            ApiCommonNotifyRequest request = new ApiCommonNotifyRequest();
            request.setOrderId(order.getOrderId().toString());
            request.setOrderStatus(OrderEnum.INVALID.getStatus().toString());
            request.setMessage(updateOrderStatusBO.getOrderMessage());
            //此处http调用一次order服务
            httpClient.postJsonForString(callbackOrderUrl, request, null);
        }

        if (OrderEnum.SHIPPED.getStatus().equals(Integer.valueOf(updateOrderStatusBO.getOrderStatus()))) {
            //发货状态
            if (StringUtils.isEmpty(updateOrderStatusBO.getExpress()) || StringUtils.isEmpty(updateOrderStatusBO.getTrackingNumber())) {
                throw new BizException("发货物流信息不能为空");
            }
            ApiCommonNotifyRequest request = new ApiCommonNotifyRequest();
            request.setOrderId(order.getOrderId().toString());
            request.setOrderStatus(OrderEnum.SHIPPED.getStatus().toString());
            request.setLogisticsName(updateOrderStatusBO.getExpress());
            request.setTrackingNumber(updateOrderStatusBO.getTrackingNumber());
            //此处http调用一次order服务
            httpClient.postJsonForString(callbackOrderUrl, request, null);
        }

        if (OrderEnum.ACTIVATED.getStatus().equals(Integer.valueOf(updateOrderStatusBO.getOrderStatus()))) {
            //激活状态
            ApiCommonNotifyRequest request = new ApiCommonNotifyRequest();
            request.setOrderId(order.getOrderId().toString());
            request.setOrderStatus(OrderEnum.ACTIVATED.getStatus().toString());
            //此处http调用一次order服务
            httpClient.postJsonForString(callbackOrderUrl, request, null);
        }

        if (BaseConstant.ONE_STRING.equals(updateOrderStatusBO.getIsRecharged())) {
            //充值状态
            if (StringUtils.isEmpty(updateOrderStatusBO.getRechargeAmount())) {
                throw new BizException("充值信息不能为空");
            }
            ApiCommonNotifyRequest request = new ApiCommonNotifyRequest();
            request.setOrderId(order.getOrderId().toString());
            request.setOrderStatus(OrderEnum.APPLY.getStatus().toString());
            request.setIsRecharged(updateOrderStatusBO.getIsRecharged());
            request.setRechargeAmount(updateOrderStatusBO.getRechargeAmount());
            //此处http调用一次order服务
            httpClient.postJsonForString(callbackOrderUrl, request, null);
        }
    }


    /**
     * 导入订单数据
     *
     * @param uploadOrderListExcelBO
     * @throws Exception
     */

    @Override
    public void uploadOrderListExcel(UploadOrderListExcelBO uploadOrderListExcelBO) throws Exception {
        Product product = productService.getProduct(uploadOrderListExcelBO.getProductCode());
        AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(uploadOrderListExcelBO.getDownstreamCode(), true);
        List<OrderSubmitRequest> orderSubmitRequestList = readOrderListExcel(uploadOrderListExcelBO.getFile(), product.getProductCode(), agentAccount.getAgentCode());
        if (CollectionUtils.isEmpty(orderSubmitRequestList)) {
            return;
        }
        //调用导入订单方法
        importOrderService.importOrderListSubmit(orderSubmitRequestList);
    }

    /**
     * 读取文件数据
     *
     * @param file 文件
     * @return 需要返回的数据模板
     */
    private List<OrderSubmitRequest> readOrderListExcel(MultipartFile file, String productCode, String agentCode) {
        List<OrderSubmitRequest> orderSubmitRequestList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            ExcelReader reader = ExcelUtil.getReader(inputStream, 0);
            List<Map<String, Object>> readAll = reader.readAll();
            int rowIndex = 0;
            for (Map<String, Object> map : readAll) {
                rowIndex++;
                checkOrderData(map, rowIndex);
                OrderSubmitRequest orderSubmitRequest = new OrderSubmitRequest();
                orderSubmitRequest.setCardName(StrUtil.toString(map.get(ImportOrderConstant.ORDER_CUSTOM_NAME)));
                orderSubmitRequest.setCardPhone(exportService.getCSVText(Convert.toStr(map.get(ImportOrderConstant.ORDER_CUSTOM_PHONE))));
                orderSubmitRequest.setCardId(exportService.getCSVText(Convert.toStr(map.get(ImportOrderConstant.ORDER_CARD_ID)).toUpperCase().trim()));
                orderSubmitRequest.setProvinceCode(StrUtil.toString(map.get(ImportOrderConstant.ORDER_PROVINCE)));
                orderSubmitRequest.setProvinceName(StrUtil.toString(map.get(ImportOrderConstant.ORDER_PROVINCE_NAME)));
                orderSubmitRequest.setCityCode(StrUtil.toString(map.get(ImportOrderConstant.ORDER_CITY)));
                orderSubmitRequest.setCityName(StrUtil.toString(map.get(ImportOrderConstant.ORDER_CITY_NAME)));
                orderSubmitRequest.setCountyCode(StrUtil.toString(map.get(ImportOrderConstant.ORDER_COUNTY)));
                orderSubmitRequest.setCountyName(StrUtil.toString(map.get(ImportOrderConstant.ORDER_COUNTY_NAME)));
                orderSubmitRequest.setCardAddress(StrUtil.toString(map.get(ImportOrderConstant.ORDER_ADDRESS)));
                if (null != map.get(ImportOrderConstant.ORDER_DOWN)) {
                    orderSubmitRequest.setOrderDownstreamId(StrUtil.toString(map.get(ImportOrderConstant.ORDER_DOWN)));
                }
                orderSubmitRequest.setProductCode(productCode);
                orderSubmitRequest.setAgentCode(agentCode);
                orderSubmitRequest.setOrderSource(OrderEnum.OrderSourceEnum.IMPORT.getSourceType());
                orderSubmitRequestList.add(orderSubmitRequest);
            }
        } catch (IOException | BizException e) {
            log.info("read excel error :{}", e.getMessage());
            e.printStackTrace();
        }
        return orderSubmitRequestList;
    }

    private void checkOrderData(Map<String, Object> map, Integer line) throws BizException {
        exportService.checkNotNull(1, Convert.toStr(map.get(ImportOrderConstant.ORDER_CUSTOM_NAME)), line);
        exportService.checkMobile(2, exportService.getCSVText(Convert.toStr(map.get(ImportOrderConstant.ORDER_CUSTOM_PHONE))), line);
        exportService.checkCardNo(3, exportService.getCSVText(Convert.toStr(map.get(ImportOrderConstant.ORDER_CARD_ID)).toUpperCase().trim()), line);
        exportService.checkNotNull(4, Convert.toStr(map.get(ImportOrderConstant.ORDER_PROVINCE)), line);
        exportService.checkNotNull(5, Convert.toStr(map.get(ImportOrderConstant.ORDER_PROVINCE_NAME)), line);
        exportService.checkNotNull(6, Convert.toStr(map.get(ImportOrderConstant.ORDER_CITY)), line);
        exportService.checkNotNull(7, Convert.toStr(map.get(ImportOrderConstant.ORDER_CITY_NAME)), line);
        exportService.checkNotNull(8, Convert.toStr(map.get(ImportOrderConstant.ORDER_COUNTY)), line);
        exportService.checkNotNull(9, Convert.toStr(map.get(ImportOrderConstant.ORDER_COUNTY_NAME)), line);
        exportService.checkNotNull(10, Convert.toStr(map.get(ImportOrderConstant.ORDER_ADDRESS)), line);
    }


    /**
     * 订单余额查询-
     */
    public String selectOrderBalance(String orderId) throws Exception {
        if(StringUtils.isEmpty(orderId)){
            throw new BizException("订单ID不能为空");
        }
        Order order = baseMapper.selectById(orderId);
        if(order == null){
            throw new BizException("订单不存在");
        }
        if(StringUtils.isEmpty(order.getAccNumber())){
            throw new BizException("生产号码为空,不能查询");
        }
        //关联产品信息
        Product product = productService.getProduct(order.getProductCode());
        NumberStatusLog numberStatusLog = numberStatusService.getEccBalance(order.getAccNumber());
        //判断是否要记录充值
        if(product.getBalanceConfig()!=null&&product.getBalanceConfig()>BaseConstant.ZERO_INT){
            //不为空 并且余额高于设置的值 记录为订单充值
            if(StringUtils.isNotEmpty(numberStatusLog.getMobileFee())&&(Double.valueOf(numberStatusLog.getMobileFee())>Double.valueOf(product.getBalanceConfig()))){
                ApiCommonNotifyRequest request = new ApiCommonNotifyRequest();
                request.setOrderId(order.getOrderId().toString());
                request.setOrderStatus(order.getOrderStatus()+"");
                request.setIsRecharged("1");
                request.setRechargeAmount(numberStatusLog.getMobileFee());
                //此处http调用一次order服务
                httpClient.postJsonForString(callbackOrderUrl, request, null);
            }
        }
        return "号码"+order.getAccNumber()+"当前余额为"+numberStatusLog.getMobileFee();
    }


}
