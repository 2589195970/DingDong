package com.ruoyi.common.apis.wjk;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author 陈思伟
 */
@Data
@ApiModel("公共返回值")
public class WjkBaseResponse {

    /**
     * code 正确值 200
     */
    private String code;

    /**
     *
     */
    private String desc;

    /**
     * 错误信息
     */
    private String message;

    /**
     *
     */
    private Object data;
}
