package com.yuehai.controller;

import com.yuehai.entity.ResultEnum;
import com.yuehai.exception.BaseException;
import com.yuehai.util.ResultUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 拦截异常
 * Created by zhaoyuehai 2019/4/15
 */
public abstract class BaseController {

    @ExceptionHandler
    public Object exceptionHandler(Exception e) {
        if (e instanceof BaseException) {
            BaseException baseException = (BaseException) e;
            return ResultUtil.error(baseException);
        } else if (e instanceof AccessDeniedException) {
            return ResultUtil.error(ResultEnum.ACCESS_DENIED);
        } else {
            return ResultUtil.error(ResultEnum.ERROR.getCode(), e.getMessage());
        }
    }
}
