package com.ruoyi.common.apis.xuanka;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 炫咖空号检测返回值
 * @author 陈思伟
 */
@Data
@ApiModel("炫咖空号检测返回值")
public class XkEmptyNumberQueryResponse {


    /**
     * 运营商类型   1移动 2联通 3电信
     */
    private String numberType;

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


}
