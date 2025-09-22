package com.ruoyi.common.order.reuqest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * 获取号池列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("获取号池列表请求")
public class GetNumberListRequest {

    /**
     * 渠道id
     */
    @ApiModelProperty(value = "渠道号", required = true)
    @NotNull(message = "渠道号不能为空")
    private Integer channelId;

    /**
     * 指定商品ID
     */
    @ApiModelProperty(value = "产品ID", required = true)
    @NotNull(message = "产品ID不能为空")
    @Length(min = 1, message = "产品ID不能为空")
    private String productId;

    /**
     * 查询分页条数
     */
    @ApiModelProperty(value = "查询分页条数", required = true, notes = "部分运营商如联通通用接口,回传记录数可能和请求不一致")
    @NotNull(message = "查询分页条数不能为空")
    @Min(value = 1, message = "查询分页条数不能小于1")
    @Max(value = 30, message = "查询分页条数不能大于30")
    private Integer pageSize;


    /**
     * 页码
     */
    @ApiModelProperty(value = "查询页号", required = true)
    @NotNull(message = "页号不能为空")
    @Min(value = 1, message = "页号不能小于1")
    private Integer currentPage;

    /**
     * 全量模糊匹配
     * 选填
     */
    @ApiModelProperty(value = "全量模糊匹配")
    private String likeNum;

    /**
     * 归属地省份
     * 选填
     */
    @ApiModelProperty(value = "号码归属地省份编码", notes = "部分运营商必填,如联通通用接口")
    private String provinceCode;

    /**
     * 归属地城市
     * 选填
     */
    @ApiModelProperty(value = "号码归属地省份编码", notes = "部分运营商必填,如联通通用接口")
    private String cityCode;

    /**
     * json参数
     * 选填
     */
    @ApiModelProperty(value = "json参数", notes = "提交额外参数,转换成JSON传入")
    private String jsonParam;

}
