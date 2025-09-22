package com.ruoyi.common.exception;

import cn.hutool.core.util.StrUtil;

public class HttpException extends Exception {
    private int code;

    public HttpException(int code) {
        super(StrUtil.format("http fail:{}", code));
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
