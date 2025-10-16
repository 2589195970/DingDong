package com.ruoyi.console.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.ruoyi.common.order.bo.OrderUpdateBO;
import com.ruoyi.common.order.bo.PhotoAuditBO;
import com.ruoyi.common.order.bo.PhotoUploadBO;
import com.ruoyi.common.order.bo.UpdateOrderStatusBO;
import com.ruoyi.common.order.bo.UploadOrderListExcelBO;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.reuqest.ApiCommonNotifyRequest;
import com.ruoyi.common.order.reuqest.OrderSubmitRequest;
import com.ruoyi.common.order.vo.OrderSelectVO;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.AgentAccountMapper;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Resource
    AgentAccountMapper agentAccountMapper;

  
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
                //查询产品头图和照片配置
                Product product = productService.getProductNotStatus(order.getProductCode());
                if(product!=null){
                    orderSelectVO.setProductMasterMap(product.getProductMasterMap());
                    orderSelectVO.setPhotoConfig(product.getPhotoConfig());
                }
                //设置照片审核状态名称
                if(order.getPhotoStatus() != null){
                    orderSelectVO.setPhotoStatusName(OrderEnum.PhotoAuditEnum.getPhotoAuditMessageByStatus(order.getPhotoStatus()));
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
        log.info("开始导出订单，查询参数: {}", orderSelectBO);
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

        if(orderSelectBO.getIsNotNullOrderUpstreamId() != null){
            if(orderSelectBO.getIsNotNullOrderUpstreamId() == BaseConstant.ZERO_INT){
                lambdaQueryWrapper.isNull(Order::getOrderUpstreamId);
            }else if(orderSelectBO.getIsNotNullOrderUpstreamId() == BaseConstant.ONE_INT){
                lambdaQueryWrapper.isNotNull(Order::getOrderUpstreamId);
            }
        }
        List<Order> orderList = baseMapper.selectList(lambdaQueryWrapper);
        log.info("查询到订单数量: {}", orderList.size());
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
                    orderSelectVO.setPhotoConfig(product.getPhotoConfig());
                }
                orderSelectVOList.add(orderSelectVO);
            }
        }
        log.info("准备导出订单数量: {}", orderSelectVOList.size());
        //数据导出
        exportService.writeCsvResponse(response, orderListToCsvList(orderSelectVOList),"attachment;filename=order.csv");
        log.info("订单导出完成");

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

    /**
     * 获取订单统计数据
     *
     * @return 订单统计数据Map
     */
    @Override
    public Map<String, Object> getOrderStatistics() throws BizException {
        Map<String, Object> result = new HashMap<>();
        
        // 获取当前时间戳
        long now = System.currentTimeMillis();
        
        // 今日开始和结束时间戳
        long todayStart = getTodayStartTimestamp();
        long todayEnd = getTodayEndTimestamp();
        
        // 昨日开始和结束时间戳
        long yesterdayStart = todayStart - 24 * 60 * 60 * 1000L;
        long yesterdayEnd = todayEnd - 24 * 60 * 60 * 1000L;
        
        // 本月开始和结束时间戳
        long thisMonthStart = getMonthStartTimestamp(now);
        long thisMonthEnd = getMonthEndTimestamp(now);
        
        // 上月开始和结束时间戳
        long lastMonthStart = getLastMonthStartTimestamp(now);
        long lastMonthEnd = getLastMonthEndTimestamp(now);
        
        // 本年度开始和结束时间戳
        long thisYearStart = getYearStartTimestamp(now);
        long thisYearEnd = getYearEndTimestamp(now);
        
        try {
            // 今日统计
            Map<String, Object> todayStats = getPeriodStatistics(todayStart, todayEnd);
            result.put("today", todayStats);
            
            // 昨日统计
            Map<String, Object> yesterdayStats = getPeriodStatistics(yesterdayStart, yesterdayEnd);
            result.put("yesterday", yesterdayStats);
            
            // 本月统计
            Map<String, Object> thisMonthStats = getPeriodStatistics(thisMonthStart, thisMonthEnd);
            result.put("thisMonth", thisMonthStats);
            
            // 上月统计
            Map<String, Object> lastMonthStats = getPeriodStatistics(lastMonthStart, lastMonthEnd);
            result.put("lastMonth", lastMonthStats);
            
            // 本年度统计
            Map<String, Object> thisYearStats = getPeriodStatistics(thisYearStart, thisYearEnd);
            result.put("thisYear", thisYearStats);
            
        } catch (Exception e) {
            log.error("获取订单统计数据异常: {}", e.getMessage(), e);
            throw new BizException("获取订单统计数据失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取今日代理订单排名
     *
     * @return 今日代理订单排名数据Map
     */
    @Override
    public Map<String, Object> getTodayAgentOrderRanking() throws BizException {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取今日开始和结束时间戳
            long todayStart = getTodayStartTimestamp();
            long todayEnd = getTodayEndTimestamp();
            
            // 查询今日所有订单，按downstream_name分组统计
            // 使用QueryWrapper进行原生SQL查询
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.select(
                "downstream_name as downstreamName",
                "COUNT(*) as totalOrders", 
                "SUM(CASE WHEN order_status = " + OrderEnum.ACTIVATED.getStatus() + " THEN 1 ELSE 0 END) as activatedOrders"
            )
            .ge("create_time", todayStart)
            .le("create_time", todayEnd)
            .isNotNull("downstream_name")
            .ne("downstream_name", "")
            .groupBy("downstream_name")
            .orderByDesc("activatedOrders")
            .last("LIMIT 10");  // 限制返回Top10
            
            List<Map<String, Object>> agentRankingList = baseMapper.selectMaps(queryWrapper);
            
            result.put("agentRankingList", agentRankingList);
            result.put("totalAgents", agentRankingList.size());
            
        } catch (Exception e) {
            log.error("获取今日代理订单排名异常: {}", e.getMessage(), e);
            throw new BizException("获取今日代理订单排名失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取订单趋势数据（30天）
     *
     * @return 订单趋势数据数组
     */
    @Override
    public List<Map<String, Object>> getOrderTrend() throws BizException {
        
        try {
            // 获取30天前的时间戳
            long thirtyDaysAgo = getThirtyDaysAgoTimestamp();
            long todayEnd = getTodayEndTimestamp();
            
            // 查询30天内的订单数据，按日期分组统计
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.select(
                "DATE(FROM_UNIXTIME(create_time/1000)) as orderDate",
                "COUNT(*) as totalOrders",
                "SUM(CASE WHEN order_status = " + OrderEnum.ACTIVATED.getStatus() + " THEN 1 ELSE 0 END) as activatedOrders",
                "SUM(CASE WHEN order_status != " + OrderEnum.INVALID.getStatus() + " THEN 1 ELSE 0 END) as validOrders"
            )
            .ge("create_time", thirtyDaysAgo)
            .le("create_time", todayEnd)
            .groupBy("DATE(FROM_UNIXTIME(create_time/1000))")
            .orderByAsc("orderDate");
            
            List<Map<String, Object>> trendData = baseMapper.selectMaps(queryWrapper);
            
            // 填充缺失的日期数据
            List<Map<String, Object>> filledTrendData = fillMissingDates(trendData, thirtyDaysAgo, todayEnd);
            
            // 直接返回数组数据
            return filledTrendData;
            
        } catch (Exception e) {
            log.error("获取订单趋势数据异常: {}", e.getMessage(), e);
            throw new BizException("获取订单趋势数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取30天前的时间戳
     */
    private long getThirtyDaysAgoTimestamp() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        return thirtyDaysAgo.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    /**
     * 填充缺失的日期数据
     */
    private List<Map<String, Object>> fillMissingDates(List<Map<String, Object>> trendData, long startTimestamp, long endTimestamp) {
        List<Map<String, Object>> filledData = new ArrayList<>();
        
        // 创建日期到数据的映射
        Map<String, Map<String, Object>> dataMap = new HashMap<>();
        for (Map<String, Object> data : trendData) {
            Object orderDateObj = data.get("orderDate");
            String orderDate;
            if (orderDateObj == null) {
                log.warn("orderDate为null，跳过该数据记录: {}", data);
                continue;
            }
            if (orderDateObj instanceof java.sql.Date) {
                orderDate = ((java.sql.Date) orderDateObj).toString();
            } else if (orderDateObj instanceof java.util.Date) {
                orderDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format((java.util.Date) orderDateObj);
            } else {
                orderDate = orderDateObj.toString();
            }
            dataMap.put(orderDate, data);
        }
        
        // 生成30天的日期范围
        LocalDate startDate = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(startTimestamp),
            java.time.ZoneId.systemDefault()
        ).toLocalDate();
        LocalDate endDate = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(endTimestamp),
            java.time.ZoneId.systemDefault()
        ).toLocalDate();
        
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            String dateStr = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            Map<String, Object> dayData = dataMap.getOrDefault(dateStr, new HashMap<>());
            dayData.put("orderDate", dateStr);
            dayData.putIfAbsent("totalOrders", 0L);
            dayData.putIfAbsent("activatedOrders", 0L);
            dayData.putIfAbsent("validOrders", 0L);
            
            filledData.add(dayData);
            currentDate = currentDate.plusDays(1);
        }
        
        return filledData;
    }
    
    /**
     * 获取指定时间段的统计数据
     */
    private Map<String, Object> getPeriodStatistics(long startTimestamp, long endTimestamp) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // 订单总数
            Long totalOrders = baseMapper.selectCount(
                new LambdaQueryWrapper<Order>()
                    .ge(Order::getCreateTime, startTimestamp)
                    .le(Order::getCreateTime, endTimestamp)
            );
            stats.put("totalOrders", totalOrders);
            
            // 激活订单数（订单状态为已激活）
            Long activatedOrders = baseMapper.selectCount(
                new LambdaQueryWrapper<Order>()
                    .ge(Order::getCreateTime, startTimestamp)
                    .le(Order::getCreateTime, endTimestamp)
                    .eq(Order::getOrderStatus, OrderEnum.ACTIVATED.getStatus())
            );
            stats.put("activatedOrders", activatedOrders);
            
            // 佣金相关统计（根据订单佣金状态统计）
            // 已结算订单数
            Long settledOrders = baseMapper.selectCount(
                new LambdaQueryWrapper<Order>()
                    .ge(Order::getCreateTime, startTimestamp)
                    .le(Order::getCreateTime, endTimestamp)
                    .eq(Order::getOrderStatus, OrderEnum.ACTIVATED.getStatus())
                    .eq(Order::getOrderCommissionStatus, OrderEnum.OrderCommissionEnum.TYPE_3.getCommissionType())
            );
            stats.put("settledOrders", settledOrders);
            
            // 待结算订单数
            Long pendingSettlementOrders = baseMapper.selectCount(
                new LambdaQueryWrapper<Order>()
                    .ge(Order::getCreateTime, startTimestamp)
                    .le(Order::getCreateTime, endTimestamp)
                    .eq(Order::getOrderStatus, OrderEnum.ACTIVATED.getStatus())
                    .eq(Order::getOrderCommissionStatus, OrderEnum.OrderCommissionEnum.TYPE_1.getCommissionType())
            );
            stats.put("pendingSettlementOrders", pendingSettlementOrders);
            
            // 代理数（统计指定时间段内创建的代理账户数量）
            // 从t_agent_account表中查询create_time在指定时间段内的代理账户数量
            Long agentCount = agentAccountMapper.selectCount(
                new LambdaQueryWrapper<AgentAccount>()
                    .ge(AgentAccount::getCreateTime, startTimestamp)
                    .le(AgentAccount::getCreateTime, endTimestamp)
            );
            stats.put("agentCount", agentCount);
            
        } catch (Exception e) {
            log.error("获取时间段统计数据异常: startTimestamp={}, endTimestamp={}, error={}", 
                startTimestamp, endTimestamp, e.getMessage(), e);
            // 返回默认值
            stats.put("totalOrders", 0L);
            stats.put("activatedOrders", 0L);
            stats.put("settledOrders", 0L);
            stats.put("pendingSettlementOrders", 0L);
            stats.put("agentCount", 0);
        }
        
        return stats;
    }
    
    /**
     * 获取今日开始时间戳（00:00:00）
     */
    private long getTodayStartTimestamp() {
        LocalDate today = LocalDate.now();
        return today.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    /**
     * 获取今日结束时间戳（23:59:59.999）
     */
    private long getTodayEndTimestamp() {
        LocalDate today = LocalDate.now();
        return today.atTime(23, 59, 59, 999_000_000).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    /**
     * 获取指定时间戳所在月份的开始时间戳
     */
    private long getMonthStartTimestamp(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp), 
            java.time.ZoneId.systemDefault()
        );
        return dateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
            .atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    /**
     * 获取指定时间戳所在月份的结束时间戳
     */
    private long getMonthEndTimestamp(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp), 
            java.time.ZoneId.systemDefault()
        );
        return dateTime.withDayOfMonth(dateTime.toLocalDate().lengthOfMonth())
            .withHour(23).withMinute(59).withSecond(59).withNano(999_000_000)
            .atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    /**
     * 获取指定时间戳所在月份的上个月开始时间戳
     */
    private long getLastMonthStartTimestamp(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp), 
            java.time.ZoneId.systemDefault()
        );
        LocalDateTime lastMonth = dateTime.minusMonths(1);
        return lastMonth.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
            .atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    /**
     * 获取指定时间戳所在月份的上个月结束时间戳
     */
    private long getLastMonthEndTimestamp(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp), 
            java.time.ZoneId.systemDefault()
        );
        LocalDateTime lastMonth = dateTime.minusMonths(1);
        return lastMonth.withDayOfMonth(lastMonth.toLocalDate().lengthOfMonth())
            .withHour(23).withMinute(59).withSecond(59).withNano(999_000_000)
            .atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    /**
     * 获取指定时间戳所在年度的开始时间戳
     */
    private long getYearStartTimestamp(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp), 
            java.time.ZoneId.systemDefault()
        );
        return dateTime.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
            .atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    /**
     * 获取指定时间戳所在年度的结束时间戳
     */
    private long getYearEndTimestamp(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp), 
            java.time.ZoneId.systemDefault()
        );
        return dateTime.withDayOfYear(dateTime.toLocalDate().lengthOfYear())
            .withHour(23).withMinute(59).withSecond(59).withNano(999_000_000)
            .atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 上传订单照片
     */
    @Override
    public void uploadOrderPhotos(PhotoUploadBO photoUploadBO) throws BizException {
        if (photoUploadBO.getOrderId() == null) {
            throw new BizException("订单ID不能为空");
        }

        Order order = baseMapper.selectById(photoUploadBO.getOrderId());
        if (order == null) {
            throw new BizException("订单不存在");
        }

        // 检查产品是否需要照片审核
        Product product = productService.getProduct(order.getProductCode());
        if (product == null || product.getPhotoRequired() == null || product.getPhotoRequired() != 1) {
            throw new BizException("该订单对应的产品不需要照片审核");
        }

        // 检查照片审核状态
        if (order.getPhotoStatus() == null ||
            !OrderEnum.PhotoAuditEnum.UPLOAD_PENDING.getStatus().equals(order.getPhotoStatus()) &&
            !OrderEnum.PhotoAuditEnum.REJECTED.getStatus().equals(order.getPhotoStatus())) {
            throw new BizException("当前订单状态不允许上传照片");
        }

        // 更新照片信息
        Order updateOrder = new Order();
        updateOrder.setOrderId(photoUploadBO.getOrderId());
        updateOrder.setIdCardFrontUrl(photoUploadBO.getIdCardFrontUrl());
        updateOrder.setIdCardBackUrl(photoUploadBO.getIdCardBackUrl());
        updateOrder.setPersonPhotoUrl(photoUploadBO.getPersonPhotoUrl());
        updateOrder.setCustomPhotoUrl(photoUploadBO.getCustomPhotoUrl());
        updateOrder.setPhotoUploadTime(System.currentTimeMillis());
        updateOrder.setUpdateTime(System.currentTimeMillis());

        // 状态变更为代理商待提交
        updateOrder.setPhotoStatus(OrderEnum.PhotoAuditEnum.AGENT_PENDING.getStatus());

        baseMapper.updateById(updateOrder);

        // 记录订单日志
        addOrderLog(photoUploadBO.getOrderId(), "上传照片", "用户上传照片，待代理商提交审核");
    }

    /**
     * 代理商提交照片审核
     */
    @Override
    public void submitPhotoForAudit(PhotoUploadBO photoUploadBO) throws BizException {
        if (photoUploadBO.getOrderId() == null) {
            throw new BizException("订单ID不能为空");
        }

        Order order = baseMapper.selectById(photoUploadBO.getOrderId());
        if (order == null) {
            throw new BizException("订单不存在");
        }

        // 检查产品是否需要照片审核
        Product product = productService.getProduct(order.getProductCode());
        if (product == null || product.getPhotoRequired() == null || product.getPhotoRequired() != 1) {
            throw new BizException("该订单对应的产品不需要照片审核");
        }

        // 检查照片审核状态
        if (order.getPhotoStatus() == null ||
            !OrderEnum.PhotoAuditEnum.AGENT_PENDING.getStatus().equals(order.getPhotoStatus())) {
            throw new BizException("当前订单状态不允许提交审核");
        }

        // 检查照片是否完整
        if (StringUtils.isEmpty(order.getIdCardFrontUrl()) ||
            StringUtils.isEmpty(order.getIdCardBackUrl()) ||
            StringUtils.isEmpty(order.getPersonPhotoUrl())) {
            throw new BizException("请上传完整的照片信息（身份证正面、反面、免冠照片）");
        }

        // 更新订单状态
        Order updateOrder = new Order();
        updateOrder.setOrderId(photoUploadBO.getOrderId());
        updateOrder.setPhotoStatus(OrderEnum.PhotoAuditEnum.ADMIN_PENDING.getStatus());
        updateOrder.setUpdateTime(System.currentTimeMillis());

        // 如果有重新上传的照片，也要更新
        if (StringUtils.isNotEmpty(photoUploadBO.getIdCardFrontUrl())) {
            updateOrder.setIdCardFrontUrl(photoUploadBO.getIdCardFrontUrl());
        }
        if (StringUtils.isNotEmpty(photoUploadBO.getIdCardBackUrl())) {
            updateOrder.setIdCardBackUrl(photoUploadBO.getIdCardBackUrl());
        }
        if (StringUtils.isNotEmpty(photoUploadBO.getPersonPhotoUrl())) {
            updateOrder.setPersonPhotoUrl(photoUploadBO.getPersonPhotoUrl());
        }
        if (StringUtils.isNotEmpty(photoUploadBO.getCustomPhotoUrl())) {
            updateOrder.setCustomPhotoUrl(photoUploadBO.getCustomPhotoUrl());
        }

        baseMapper.updateById(updateOrder);

        // 记录订单日志
        addOrderLog(photoUploadBO.getOrderId(), "提交照片审核", "代理商提交照片审核，待管理员审核");
    }

    /**
     * 管理员审核照片
     */
    @Override
    public void auditOrderPhotos(PhotoAuditBO photoAuditBO) throws BizException {
        if (photoAuditBO.getOrderId() == null) {
            throw new BizException("订单ID不能为空");
        }

        if (photoAuditBO.getAuditAction() == null) {
            throw new BizException("审核操作不能为空");
        }

        Order order = baseMapper.selectById(photoAuditBO.getOrderId());
        if (order == null) {
            throw new BizException("订单不存在");
        }

        // 检查产品是否需要照片审核
        Product product = productService.getProduct(order.getProductCode());
        if (product == null || product.getPhotoRequired() == null || product.getPhotoRequired() != 1) {
            throw new BizException("该订单对应的产品不需要照片审核");
        }

        // 检查照片审核状态
        if (order.getPhotoStatus() == null ||
            !OrderEnum.PhotoAuditEnum.ADMIN_PENDING.getStatus().equals(order.getPhotoStatus())) {
            throw new BizException("当前订单状态不允许审核");
        }

        // 更新订单状态
        Order updateOrder = new Order();
        updateOrder.setOrderId(photoAuditBO.getOrderId());
        updateOrder.setUpdateTime(System.currentTimeMillis());
        updateOrder.setPhotoAuditTime(System.currentTimeMillis());
        updateOrder.setPhotoAuditRemark(photoAuditBO.getAuditRemark());

        if (photoAuditBO.getAuditAction() == 1) {
            // 审核通过
            updateOrder.setPhotoStatus(OrderEnum.PhotoAuditEnum.APPROVED.getStatus());

            // 记录订单日志
            addOrderLog(photoAuditBO.getOrderId(), "照片审核通过", "管理员审核通过，备注：" + photoAuditBO.getAuditRemark());

            // 如果照片审核通过，可以继续订单流程（根据业务需求决定是否自动推送到上游）
            // 这里暂时只记录日志，不自动推送

        } else if (photoAuditBO.getAuditAction() == 2) {
            // 审核拒绝
            updateOrder.setPhotoStatus(OrderEnum.PhotoAuditEnum.REJECTED.getStatus());

            // 记录订单日志
            addOrderLog(photoAuditBO.getOrderId(), "照片审核拒绝", "管理员审核拒绝，备注：" + photoAuditBO.getAuditRemark());
        } else {
            throw new BizException("无效的审核操作");
        }

        baseMapper.updateById(updateOrder);
    }

    /**
     * 查询订单照片状态
     */
    @Override
    public OrderSelectVO getOrderPhotoStatus(Long orderId) throws BizException {
        if (orderId == null) {
            throw new BizException("订单ID不能为空");
        }

        Order order = baseMapper.selectById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }

        OrderSelectVO orderSelectVO = new OrderSelectVO();
        BeanUtil.copyProperties(order, orderSelectVO);
        orderSelectVO.setOrderId(order.getOrderId() + "");

        // 设置照片审核状态名称
        if (order.getPhotoStatus() != null) {
            orderSelectVO.setPhotoStatusName(OrderEnum.PhotoAuditEnum.getPhotoAuditMessageByStatus(order.getPhotoStatus()));
        }

        // 设置产品照片配置
        Product product = productService.getProduct(order.getProductCode());
        if (product != null) {
            orderSelectVO.setPhotoConfig(product.getPhotoConfig());
        }

        return orderSelectVO;
    }

    /**
     * 添加订单日志
     */
    private void addOrderLog(Long orderId, String operation, String remark) {
        try {
            OrderLog orderLog = new OrderLog();
            orderLog.setOrderId(orderId.toString());

            // 只填充明确的信息
            orderLog.setRequestUrl("照片审核操作"); // 简单标识操作类型
            orderLog.setRequestBody(operation);    // 操作类型作为请求参数
            orderLog.setRequestMsg(remark);       // 备注信息作为返回消息

            // 尝试获取产品编码
            try {
                Order order = baseMapper.selectById(orderId);
                if (order != null && StringUtils.isNotEmpty(order.getProductCode())) {
                    orderLog.setProductCode(order.getProductCode());
                }
            } catch (Exception e) {
                log.debug("获取订单产品编码失败，跳过: {}", e.getMessage());
            }

            orderLog.setCreateTime(System.currentTimeMillis());
            orderLogMapper.insert(orderLog);
        } catch (Exception e) {
            log.error("添加订单日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 查询订单信息（用于照片修改）
     *
     * @param orderId 订单ID
     * @return 订单信息
     * @throws BizException 业务异常
     */
    @Override
    public Order selectOrderById(Long orderId) throws BizException {
        if (orderId == null) {
            throw new BizException("订单ID不能为空");
        }

        Order order = baseMapper.selectById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }

        return order;
    }

    /**
     * 更新订单信息（照片修改）
     *
     * @param updateBO 订单更新业务对象
     * @throws BizException 业务异常
     */
    @Override
    public void updateOrderInfo(OrderUpdateBO updateBO) throws BizException {
        if (updateBO == null || updateBO.getOrderId() == null) {
            throw new BizException("订单ID不能为空");
        }

        Order order = baseMapper.selectById(updateBO.getOrderId());
        if (order == null) {
            throw new BizException("订单不存在");
        }

        // 验证订单状态（只有特定状态才能修改照片）
        if (order.getPhotoStatus() == null || order.getPhotoStatus() == 0) {
            throw new BizException("该订单不支持照片修改");
        }

        // 检查产品是否需要照片审核
        Product product = productService.getProduct(order.getProductCode());
        if (product == null || product.getPhotoRequired() == null || product.getPhotoRequired() != 1) {
            throw new BizException("该订单对应的产品不需要照片审核");
        }

        // 更新照片信息
        order.setIdCardFrontUrl(updateBO.getIdCardFrontUrl());
        order.setIdCardBackUrl(updateBO.getIdCardBackUrl());
        order.setPersonPhotoUrl(updateBO.getPersonPhotoUrl());
        order.setCustomPhotoUrl(updateBO.getCustomPhotoUrl());

        // 更新地址信息（可选）
        if (StringUtils.isNotBlank(updateBO.getCardAddress())) {
            order.setCardAddress(updateBO.getCardAddress());
        }
        if (StringUtils.isNotBlank(updateBO.getProvinceCode())) {
            order.setProvinceCode(updateBO.getProvinceCode());
        }
        if (StringUtils.isNotBlank(updateBO.getProvinceName())) {
            order.setProvinceName(updateBO.getProvinceName());
        }
        if (StringUtils.isNotBlank(updateBO.getCityCode())) {
            order.setCityCode(updateBO.getCityCode());
        }
        if (StringUtils.isNotBlank(updateBO.getCityName())) {
            order.setCityName(updateBO.getCityName());
        }
        if (StringUtils.isNotBlank(updateBO.getCountyCode())) {
            order.setCountyCode(updateBO.getCountyCode());
        }
        if (StringUtils.isNotBlank(updateBO.getCountyName())) {
            order.setCountyName(updateBO.getCountyName());
        }

        // 自动状态流转：照片修改完成 -> 管理员待审核
        order.setPhotoStatus(OrderEnum.PhotoAuditEnum.ADMIN_PENDING.getStatus());
        order.setPhotoUploadTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());

        // 记录操作日志
        addOrderLog(order.getOrderId(), "用户修改照片", "通过H5页面修改订单照片");

        baseMapper.updateById(order);

        // 调用上游API更新照片信息
        try {
            updateOrderPhotosToUpstream(order);
        } catch (Exception e) {
            log.error("调用上游API更新照片失败，订单ID: {}, 错误: {}", updateBO.getOrderId(), e.getMessage(), e);
            // 不抛出异常，避免影响本地数据库更新
        }
    }

    /**
     * 调用上游API更新照片信息
     * 注意：此方法暂时禁用，因为需要跨服务调用mc-order模块
     *
     * @param order 订单信息
     * @throws Exception
     */
    private void updateOrderPhotosToUpstream(Order order) throws Exception {
        log.warn("订单{} 的上游照片更新功能暂时禁用，需要通过HTTP API调用mc-order服务", order.getOrderId());
        // TODO: 通过HTTP API调用mc-order服务的照片更新功能
        // 目前只记录日志，不执行上游更新
    }

}
