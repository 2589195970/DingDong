package com.ruoyi.common.apis.douyin;

import lombok.Data;

/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店订阅消息接受成功返回结果
 * @date 2025/7/18 10:48
 */
@Data
public class DouDianSuccessResponse {
    private Long code = 0L;
    private String msg = "success";
}
