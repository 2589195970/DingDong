package com.ruoyi.common.apis.xuanka;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 炫咖公共返回值
 * @author 陈思伟
 */
@Data
@ApiModel("公共返回值")
public class XkBaseResponse {

    /**
     * code 正确值 200
     */
    private Integer code;

    /**
     *
     */
    private String msg;

    private Object data;

    /**
     * 执行耗时
     */
    private String exec_time;

    /**
     * ip
     */
    private String user_ip;
}
