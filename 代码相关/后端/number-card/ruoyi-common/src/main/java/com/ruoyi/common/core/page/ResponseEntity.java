package com.ruoyi.common.core.page;

import com.ruoyi.common.enums.ResponseMessageEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description 封装返回值
 * @Author 陈思伟
 * @Date 2024/08/12 17:24
 */
@Data
@Accessors(chain = true)
public class ResponseEntity<T> {
    private int code;
    private String message;
    private T data;

    private ResponseEntity() {
    }

    private ResponseEntity(ResponseMessageEnum messageEnum) {
        this.code = messageEnum.getCode();
        this.message = messageEnum.getMessage();
    }

    public static ResponseEntity<Void> success() {
        return new ResponseEntity<>(ResponseMessageEnum.SUCCESS);
    }

    public static ResponseEntity success(String message) {
        return new ResponseEntity<Void>().setCode(ResponseMessageEnum.SUCCESS.getCode()).setMessage(message);
    }

    public static <T> ResponseEntity<T> success(String message, T data) {
        return new ResponseEntity<T>().setCode(ResponseMessageEnum.SUCCESS.getCode()).setMessage(message).setData(data);
    }

    public static <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<T>(ResponseMessageEnum.SUCCESS).setData(data);
    }

    public static ResponseEntity success(int code, String message) {
        return new ResponseEntity<Void>().setCode(code).setMessage(message);
    }

    public static ResponseEntity<Void> error() {
        return new ResponseEntity<>(ResponseMessageEnum.ERROR);
    }

    public static ResponseEntity<Void> error(String message) {
        return new ResponseEntity<Void>().setCode(ResponseMessageEnum.ERROR.getCode()).setMessage(message);
    }

    public static ResponseEntity<Void> error(int code, String message) {
        return new ResponseEntity<Void>().setCode(code).setMessage(message);
    }

    public static <T> ResponseEntity<T> error(int code, String message, T data) {
        return new ResponseEntity<T>().setCode(code).setMessage(message).setData(data);
    }

    public static <T> ResponseEntity<T> error(String message, T data) {
        return new ResponseEntity<T>().setCode(ResponseMessageEnum.ERROR.getCode()).setMessage(message).setData(data);
    }
}
