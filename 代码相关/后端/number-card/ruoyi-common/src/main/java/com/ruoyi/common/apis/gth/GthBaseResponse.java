package com.ruoyi.common.apis.gth;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author 陈思伟
 */
@Data
@ApiModel("公共返回值")
public class GthBaseResponse {

    /**
     *
     */
    private GthMsg msg;

    /**
     *
     */
    private Object data;

}
