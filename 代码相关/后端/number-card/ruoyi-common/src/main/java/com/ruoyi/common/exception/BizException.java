package com.ruoyi.common.exception;


import cn.hutool.core.util.StrUtil;

public class BizException extends Exception {

    public BizException(String message) {
        super(message);
    }

    public BizException(String template, Object... objs) {
        super(StrUtil.format(template, objs));
    }

}
