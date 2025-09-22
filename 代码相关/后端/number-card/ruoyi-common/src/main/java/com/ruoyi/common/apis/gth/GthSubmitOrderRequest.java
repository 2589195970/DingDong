package com.ruoyi.common.apis.gth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 陈思伟
 */
@Data
@ApiModel("感叹号第三方提交订单")
public class GthSubmitOrderRequest extends GthBaseRequest{

    /**
     * 商品名称（敢探号系统产品销售中心模块商品的名字）
     */
    @ApiModelProperty(value = "商品名称（敢探号系统产品销售中心模块商品的名字）", required = true)
    private String sku;
    /**
     * 商品编码（敢探号系统产品销售中心模块商品的编码，编码和名称选一个传即可）
     */
    @ApiModelProperty(value = "商品编码（敢探号系统产品销售中心模块商品的编码，编码和名称选一个传即可）", required = true)
    private String source_sku;

    @ApiModelProperty(value = "来源id(唯一)自己定义，确保唯一，后续可依据这个id查询订单", required = true)
    private String source_id;

    @ApiModelProperty(value = "发展人id", required = true)
    private String share_id;

    @ApiModelProperty(value = "身份证姓名 aes加密", required = true)
    private String id_name;

    @ApiModelProperty(value = "身份证号 aes加密", required = true)
    private String id_num;

    @ApiModelProperty(value = "收货手机号 aes加密", required = true)
    private String mobile;

    @ApiModelProperty(value = "收货人姓名 aes加密", required = true)
    private String name;

    @ApiModelProperty(value = "收货地址-省编码（建议上传）", required = true)
    private String province_code;

    @ApiModelProperty(value = "收货地址-省", required = true)
    private String province;

    @ApiModelProperty(value = "收货地址-市编码 （建议上传）", required = true)
    private String city_code;

    @ApiModelProperty(value = "收货地址-市", required = true)
    private String city;

    @ApiModelProperty(value = "收货地址-区/县编码 （建议上传）", required = true)
    private String district_code;

    @ApiModelProperty(value = "收货地址-区/县", required = true)
    private String district;

    @ApiModelProperty(value = "收货地址-街道", required = true)
    private String town;

    @ApiModelProperty(value = "详细地址 aes加密", required = true)
    private String address;

    @ApiModelProperty(value = "详购买号码", required = true)
    private String pretty_number;

    /**
     * share_id、sku、source_id 3个字段，根据字段名key=val 格式进行&连接，追加api_token之后， md5加密（加密结果32位小写） 。注意：三个字段的顺序不能变。
     * 原串例：share_id=123&sku=321&source_id=123321jW5DoNjzP7NXb1nvf4ZKGb4bH0fWbbiwokzoyo8QX7cmSuy4l6
     */
    @ApiModelProperty(value = "签名", required = true)
    private String sign;
}
