package com.ruoyi.common.exception;

import cn.hutool.core.util.StrUtil;

/**
 * @author Dots
 */
public class RetryException extends Exception {
    public RetryException(String msg, Object...args) {
        super(StrUtil.format(msg,args));
    }
}
