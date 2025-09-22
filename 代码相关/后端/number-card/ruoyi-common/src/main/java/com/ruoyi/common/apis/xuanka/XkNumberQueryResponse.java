package com.ruoyi.common.apis.xuanka;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 炫咖号码查询返回值
 * @author 陈思伟
 */
@Data
@ApiModel("炫咖号码查询返回值")
public class XkNumberQueryResponse {


    /**
     * 运营商类型   1移动 2联通 3电信
     */
    private String numberType;

    /**
     * numberType的中文说明
     */
    private String Type_name;

    /**
     * 见下方表
     */
    private String status;

    /**
     * status的中文说明
     */
    private String status_name;

    /**
     * 查询号码
     */
    private String mobile;

    /**
     * 归属地
     */
    private String area;

    /**
     * 查询时间
     */
    private String handleTime;

    /**
     * 是否携号转网   1-是 0-否
     */
    private Integer mnpStatus;

}
