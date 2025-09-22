package com.ruoyi.order.utils.douyin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.apis.douyin.DouDianOrderResult;
import com.ruoyi.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;


/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店获取sign、token、Gson有序序列化工具类
 * @date 2025/7/27 15:28
 */
@Slf4j
public class DouDianUtil {
    public static final String TAG = "DouDianUtil";

    /**
     * 构建抖店接口的url
     *
     * @param realmName
     * @param route
     * @param method
     * @param appKey
     * @param date
     * @param v
     * @param sign
     * @param signMethod
     * @param accessToken
     * @return
     */
    public static String urlBuild(String realmName, String route, String method, String appKey, String date, String v, String sign, String signMethod, String accessToken) {
        String url = "";
        if (accessToken == null) {
            url = realmName + route +
                    "?method=" + method + "&app_key=" + appKey +
                    "&timestamp=" + date + "&v=" + v + "&sign=" + sign + "&sign_method=" + signMethod;
        } else {
            url = realmName + route +
                    "?method=" + method + "&app_key=" + appKey +
                    "&timestamp=" + date + "&v=" + v + "&sign=" + sign + "&sign_method=" + signMethod +
                    "&access_token=" + accessToken;
        }
        return url;
    }

    /**
     * 构造签名
     *
     * @param appKey
     * @param appSecret
     * @param method
     * @param timestamp
     * @param paramJson
     * @return
     */
    public static String sign(String appKey, String appSecret, String method, String timestamp, String paramJson) {
        // 按给定规则拼接参数
        String paramPattern = "app_key" + appKey + "method" + method + "param_json" + paramJson + "timestamp" + timestamp + "v2";
        String signPattern = appSecret + paramPattern + appSecret;
        return hmac(signPattern, appSecret);
    }

    /**
     * HmacSHA256算法
     *
     * @param plainText
     * @param appSecret
     * @return
     */
    public static String hmac(String plainText, String appSecret) {
        Mac mac;
        try {
            byte[] secret = appSecret.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(secret, "HmacSHA256");
            mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("签名失败：{}", e.getMessage());
            return "";
        }
        byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] digest = mac.doFinal(plainBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 序列化参数
     * 保证JSON所有层级上Key的有序性
     * 保证JSON的所有数值不带多余的小数点
     *
     * @param o
     * @return
     */
    public static String marshal(Object o, boolean b) {
        if (b) {
            String raw = CustomGsonL.toJson(o);
            // 执行反序列化，把所有JSON对象转换成LinkedTreeMap
            Map<?, ?> m = CustomGsonL.fromJson(raw, LinkedTreeMap.class);
            // 重新序列化，保证JSON所有层级上Key的有序性
            return CustomGsonL.toJson(m);
        }
        String raw = CustomGson.toJson(o);
        // 执行反序列化，把所有JSON对象转换成LinkedTreeMap
        Map<?, ?> m = CustomGson.fromJson(raw, LinkedTreeMap.class);
        // 重新序列化，保证JSON所有层级上Key的有序性
        return CustomGson.toJson(m);
    }

    private static final Gson CustomGson = new GsonBuilder()
            // 定制LinkedTreeMap序列化，确保所有key按字典序排序
            .registerTypeAdapter(LinkedTreeMap.class, newMapSerializer())
            // 定制数值类型的序列化，确保整数输出不带小数点
            .registerTypeAdapter(Integer.class, newNumberSerializer())
            // 同上
            .registerTypeAdapter(Long.class, newNumberSerializer())
            // 同上
            .registerTypeAdapter(Double.class, newNumberSerializer())
            // 禁用Html Escape，确保符号不转义：&<>='
            .disableHtmlEscaping()
            .create();
    private static final Gson CustomGsonL = new GsonBuilder()
            // 定制LinkedTreeMap序列化，确保所有key按字典序排序
            .registerTypeAdapter(LinkedTreeMap.class, newMapSerializer())
            // 定制数值类型的序列化，确保整数输出不带小数点
            .registerTypeAdapter(Integer.class, newNumberSerializer())
            // 同上
            .registerTypeAdapter(Long.class, newNumberSerializer())
            // 同上
            .registerTypeAdapter(Double.class, newNumberSerializer())
            // 禁用Html Escape，确保符号不转义：&<>='
            .disableHtmlEscaping()
            //驼峰转下划线
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();



    /**
     * 为LinkedTreeMap定制的序列化器
     *
     * @return
     */
    public static JsonSerializer<LinkedTreeMap<?, ?>> newMapSerializer() {
        return new JsonSerializer<LinkedTreeMap<?, ?>>() {
            @Override
            public JsonElement serialize(LinkedTreeMap<?, ?> src, Type typeOfSrc, JsonSerializationContext context) {
                List<String> keys = src.keySet().stream()
                        .map(Object::toString)
                        .sorted()
                        .collect(Collectors.toList());
                JsonObject o = new JsonObject();
                for (String k : keys) {
                    o.add(k, context.serialize(src.get(k)));
                }
                return o;
            }
        };
    }


    /**
     * 为Number定制化的序列化器
     *
     * @param <T>
     * @return
     */
    private static <T extends Number> JsonSerializer<T> newNumberSerializer() {
        return new JsonSerializer<T>() {
            @Override
            public JsonElement serialize(T number, Type type, JsonSerializationContext context) {
                if (number instanceof Integer) {
                    return new JsonPrimitive(number.intValue());
                }
                if (number instanceof Long) {
                    return new JsonPrimitive(number.longValue());
                }
                if (number instanceof Double) {
                    long longValue = number.longValue();
                    double doubleValue = number.doubleValue();
                    if (longValue == doubleValue) {
                        return new JsonPrimitive(longValue);
                    }
                }
                return new JsonPrimitive(number);
            }
        };
    }

    /**
     * 处理抖店订单的详情消息工具类
     * 数据转换：订单详情=》订单详情公共类
     */
    public static DouDianOrderResult orderDetailToBean(String order) throws Exception {
        Map<String, Object> orderDetailMap = JSONUtil.toBean(order, Map.class);
        if (orderDetailMap == null) {
            log.error(TAG, "工具类解析订单详情失败");
            throw new BizException("网络错误");
        }
        //验证数据是否缺失
        if (!orderDetailMap.containsKey("order_id") || !orderDetailMap.containsKey("user_id_info") || !orderDetailMap.containsKey("encrypt_post_tel")
                || !orderDetailMap.containsKey("order_status") || !orderDetailMap.containsKey("sku_order_list") || !orderDetailMap.containsKey("post_addr")) {
            log.error(TAG, "工具类解析订单详情失败");
            throw new BizException("order_id,user_id_info,encrypt_post_tel,order_status,sku_order_list,post_addr中数据缺失");
        }
        //订单id
        String o = DouDianMapUtil.getString(orderDetailMap, "order_id");
        if (o == null) {
            log.error(TAG, "工具类解析订单id失败");
            throw new BizException("工具类解析订单order_id失败");
        }
        //身份信息
        String userIdInfo = DouDianMapUtil.getString(orderDetailMap, "user_id_info");
        if (userIdInfo == null) {
            log.error(TAG, "工具类解析身份信息失败");
            throw new BizException("工具类解析user_id_info失败");
        }
        Map<String, Object> userInfoMap = JSONUtil.toBean(userIdInfo, Map.class);
        if (userInfoMap == null || !userInfoMap.containsKey("encrypt_id_card_no") || !userInfoMap.containsKey("encrypt_id_card_name")) {
            log.error(TAG, "工具类解析身份信息失败");
            throw new BizException("工具类解析encrypt_id_card_no,encrypt_id_card_name失败");
        }
        //姓名
        String encryptIdCardName = DouDianMapUtil.getString(userInfoMap, "encrypt_id_card_name");
        if (encryptIdCardName == null) {
            log.error(TAG, "工具类解析姓名失败");
            throw new BizException("工具类解析encrypt_id_card_name失败");
        }
        //身份证号码
        String encryptIdCardNo = DouDianMapUtil.getString(userInfoMap, "encrypt_id_card_no");
        if (encryptIdCardNo == null) {
            log.error(TAG, "工具类解析身份证号失败");
            throw new BizException("工具类解析encrypt_id_card_no失败");
        }
        //收件号码
        String encryptPostTel = DouDianMapUtil.getString(orderDetailMap, "encrypt_post_tel");
        if (encryptPostTel == null) {
            log.error(TAG, "工具类解析电话号码失败");
            throw new BizException("工具类解析encrypt_post_tel失败");
        }
        //订单状态码
        String orderStatus = DouDianMapUtil.getString(orderDetailMap, "order_status");
        if (orderStatus == null) {
            log.error(TAG, "工具类解析订单状态失败");
            throw new BizException("工具类解析order_status失败");
        }
        //商品单信息
        String skuOrderList = DouDianMapUtil.getString(orderDetailMap, "sku_order_list");
        if (skuOrderList == null) {
            log.error(TAG, "工具类解析商品单信息失败");
            throw new BizException("工具类解析sku_order_list信息失败");
        }
        List<String> skuOrderLists = JSONObject.parseArray(skuOrderList, String.class);
        if (skuOrderLists == null || skuOrderLists.size() == 0) {
            log.error(TAG, "工具类解析商品单信息失败");
            throw new BizException("工具类解析商品单信息失败");
        }
        String skuOrder = skuOrderLists.get(0);
        if (StrUtil.isBlankIfStr(skuOrder)) {
            log.error(TAG, "工具类解析商品单信息失败");
            throw new BizException("工具类解析商品单信息失败");
        }
        Map<String, Object> skuOrderMap = JSONUtil.toBean(skuOrder, Map.class);

        if (skuOrderMap == null || !skuOrderMap.containsKey("product_id") || !skuOrderMap.containsKey("product_name")) {
            log.error(TAG, "工具类解析商品单信息失败");
            throw new BizException("工具类解析product_id,product_name信息失败");
        }
        //商品id
        String productId = DouDianMapUtil.getString(skuOrderMap, "product_id");
        if (productId == null) {
            log.error(TAG, "工具类解析商品id失败");
            throw new BizException("工具类解析商品id失败");
        }
        //商品名
        String productName = DouDianMapUtil.getString(skuOrderMap, "product_name");
        if (productName == null) {
            log.error(TAG, "工具类解析商品名失败");
            throw new BizException("工具类解析商品名失败");
        }
        //规格信息
        String specStr = DouDianMapUtil.getString(skuOrderMap, "spec");
        List<Map> spec = null;
        if (StringUtils.hasLength(specStr)) {
            //规格 非必选参数
            spec = JSONObject.parseArray(specStr, Map.class);
        }
        //地址信息
        String postAddr = DouDianMapUtil.getString(orderDetailMap, "post_addr");
        if (postAddr == null) {
            log.error(TAG, "工具类解析发货信息失败");
            throw new BizException("工具类解析发货信息失败");
        }
        Map postAddrMap = JSONUtil.toBean(postAddr, Map.class);
        if (postAddrMap == null || !postAddrMap.containsKey("province") || !postAddrMap.containsKey("city") || !postAddrMap.containsKey("town")
                || !postAddrMap.containsKey("encrypt_detail")) {
            log.error(TAG, "工具类解析发货信息失败");
            throw new BizException("工具类解析province,city,town,encrypt_detail失败");
        }
        //省
        String province = DouDianMapUtil.getString(postAddrMap, "province");
        if (province == null) {
            log.error(TAG, "工具类解析省信息失败");
            throw new BizException("工具类解析省信息失败");
        }
        Map provinceMap = JSONUtil.toBean(province, Map.class);
        String provinceName = DouDianMapUtil.getString(provinceMap, "name");
        //市
        String city = DouDianMapUtil.getString(postAddrMap, "city");
        if (city == null) {
            log.error(TAG, "工具类解析市信息失败");
            throw new BizException("工具类解析市信息失败");
        }
        Map cityMap = JSONUtil.toBean(city, Map.class);
        String cityName = DouDianMapUtil.getString(cityMap, "name");
        //区
        String town = DouDianMapUtil.getString(postAddrMap, "town");
        if (town == null) {
            log.error(TAG, "工具类解析区信息失败");
            throw new BizException("工具类解析区信息失败");
        }
        Map townMap = JSONUtil.toBean(town, Map.class);
        String townName = DouDianMapUtil.getString(townMap, "name");
        //街
        String street = DouDianMapUtil.getString(postAddrMap, "street");
        Map streetMap = JSONUtil.toBean(street, Map.class);
        String streetName = DouDianMapUtil.getString(streetMap, "name", "");
        //详细地址
        String encryptDetail = DouDianMapUtil.getString(postAddrMap, "encrypt_detail");
        if (encryptDetail == null) {
            log.error(TAG, "工具类解析详细地址信息失败");
            throw new BizException("工具类解析详细地址信息失败");
        }
        String doudianOpenId = DouDianMapUtil.getString(orderDetailMap, "doudian_open_id");
        String typeDesc = DouDianMapUtil.getString(orderDetailMap, "sub_b_type_desc");

        //将信息封装入抖店详情返回结果公共类
        DouDianOrderResult orderResult = DouDianOrderResult.builder()
                .oId(o)
                .name(encryptIdCardName)
                .idCard(encryptIdCardNo)
                .tel(encryptPostTel)
                .detailAdd(encryptDetail)
                .province(provinceName)
                .city(cityName)
                .town(townName)
                .street(streetName)
                .productId(productId)
                .productName(productName)
                .orderStatus(orderStatus)
                .spec(spec)
                .doudianOpenId(doudianOpenId)
                .typeDesc(typeDesc)
                .build();
        return orderResult;
    }
}

