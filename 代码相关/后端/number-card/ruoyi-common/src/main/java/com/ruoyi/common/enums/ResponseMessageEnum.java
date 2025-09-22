package com.ruoyi.common.enums;

/**
 * @Description 定义常用的返回消息枚举
 * @Author 陈思伟
 * @Date 2024/08/12 17:34
 */
public enum ResponseMessageEnum {
    /**
     * 请求成功
     */
    SUCCESS(200,"请求成功!"),
    /**
     * 请求失败
     */
    ERROR(500,"请求失败");
    /**
     * 代码
     */
    int code;
    /**
     * 提示
     */
    String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ResponseMessageEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
