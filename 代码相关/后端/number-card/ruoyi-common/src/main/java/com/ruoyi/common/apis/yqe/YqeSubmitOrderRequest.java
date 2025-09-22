package com.ruoyi.common.apis.yqe;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruoyi.common.apis.gth.GthBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 陈思伟
 */
@Data
@ApiModel("172第三方提交订单")
public class YqeSubmitOrderRequest extends GthBaseRequest{
    /**
     * 代理在172号卡登录账号
     */
    @ApiModelProperty(value = "商品名称（敢探号系统产品销售中心模块商品的名字）", required = true)
    @JSONField(name = "user_id")
    private String user_id;

    /**
     * 时间戳（长度10位并且与服务器时间不能超5分钟）
     */
    @ApiModelProperty(value = "时间戳（长度10位并且与服务器时间不能超5分钟）", required = true)
    @JSONField(name = "Timestamp")
    private String Timestamp;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", required = true)
    @JSONField(name = "Name")
    private String Name;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", required = true)
    @JSONField(name = "Phone")
    private String Phone;

    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号", required = true)
    @JSONField(name = "IDCard")
    private String IDCard;

    /**
     * 省份 例如山东省
     */
    @ApiModelProperty(value = "省份 例如山东省", required = true)
    @JSONField(name = "Province")
    private String Province;

    /**
     * 城市 例如潍坊市
     */
    @ApiModelProperty(value = "城市 例如潍坊市", required = true)
    @JSONField(name = "City")
    private String City;

    /**
     * 县区 例如青州市
     */
    @ApiModelProperty(value = "县区 例如青州市", required = true)
    @JSONField(name = "Area")
    private String Area;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址", required = true)
    @JSONField(name = "Address")
    private String Address;

    /**
     * 产品ID，在172号卡后台商品列表第一列获取
     */
    @ApiModelProperty(value = "产品ID，在172号卡后台商品列表第一列获取", required = true)
    @JSONField(name = "ProductID")
    private Integer ProductID;

    /**
     * 下游订单id（对接接口方订单ID） 不能重复
     */
    @ApiModelProperty(value = "下游订单id（对接接口方订单ID） 不能重复", required = true)
    @JSONField(name = "DownOrderID")
    private String DownOrderID;

    /**
     * 支持选号的产品套餐可以传通过接口选择的号码，否则传空字符串(不是null)
     */
    @ApiModelProperty(value = "支持选号的产品套餐可以传通过接口选择的号码，否则传空字符串(不是null)", required = true)
    @JSONField(name = "ThirdPhone")
    private String ThirdPhone = "";

    /**
     * 号码ID（新增字段）：选号时若有值，请上传 不参与MD5加密
     */
    @ApiModelProperty(value = "号码ID（新增字段）：选号时若有值，请上传 不参与MD5加密", required = true)
    @JSONField(name = "NumberId")
    private String NumberId;

    /**
     * 所属号池（新增字段）：选号时若有值，请上传 不参与MD5加密
     */
    @ApiModelProperty(value = "所属号池（新增字段）：选号时若有值，请上传 不参与MD5加密", required = true)
    @JSONField(name = "NumberPoolId")
    private String NumberPoolId;

    /**
     * Md5("Address=" + Address + "&Area=" + Area + "&City=" + City + "&DownOrderID=" + DownOrderID + "&IDCard=" + IDCard + "&Name=" + Name + "&Phone=" + Phone + "&ProductID=" + ProductID + "&Province=" + Province + "&ThirdPhone=" + ThirdPhone + "&Timestamp=" + Timestamp+ "&user_id=" + user_id + secret)
     * Secret（秘钥）追加最后即可，前边参数需要进行自然排名
     * md5加密是32小写
     */
    @ApiModelProperty(value = "md5加密是32小写", required = true)
    @JSONField(name = "user_sign")
    private String user_sign;
}
