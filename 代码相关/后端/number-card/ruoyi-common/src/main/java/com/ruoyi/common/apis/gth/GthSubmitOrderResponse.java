package com.ruoyi.common.apis.gth;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 *
 * @author 陈思伟
 */
@Data
@ApiModel("感叹号提交订单返回值")
public class GthSubmitOrderResponse {

    /**
     * 下游订单号
     */
    private String sourceId;

    /**
     * 订单发展人id
     */
    private String shareId;

    /**
     * sku
     */
    private String productSku;

    /**
     * 订单状态 120 成功接收订单
     */
    private String status;

    /**
     * 敢探号订单id
     */
    private String id;

    /**
     * 敢探号订单id
     */
    private String createdAt;

    /**
     * 照片秘钥 （上传订单传照片，可以用这个值作为id补传照片 ）
     */
    private String strRand;

}
