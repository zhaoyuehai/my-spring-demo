package com.yuehai.exception;

import com.yuehai.entity.ResultEnum;

/**
 * Created by zhaoyuehai 2019/4/15
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 6608464561762611035L;
    private String code;

    public BaseException() {
    }

    public BaseException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public String getCode() {
        return code;
    }
}
