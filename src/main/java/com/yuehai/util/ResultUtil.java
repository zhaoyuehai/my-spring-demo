package com.yuehai.util;

import com.yuehai.entity.ResultEntity;
import com.yuehai.entity.ResultEnum;
import com.yuehai.exception.BaseException;

/**
 * Created by zhaoyuehai 2019/4/15
 */
public class ResultUtil {
    private static final String VERSION = "1.0";

    public static ResultEntity success(Object o, String message) {
        ResultEntity result = new ResultEntity();
        result.setServiceCode(VERSION);
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(o);
        return result;
    }

    public static ResultEntity success(Object o) {
        return success(o, ResultEnum.SUCCESS.getMessage());
    }

    public static ResultEntity success(String message) {
        return success(null, message);
    }

    public static ResultEntity success() {
        return success(null, ResultEnum.SUCCESS.getMessage());
    }

    public static ResultEntity error(String code, String message) {
        ResultEntity result = new ResultEntity();
        result.setServiceCode(VERSION);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static ResultEntity error(String message) {
        return error(ResultEnum.ERROR.getCode(), message);
    }

    public static ResultEntity error(ResultEnum resultEnum, Object o) {
        ResultEntity result = error(resultEnum.getCode(), resultEnum.getMessage());
        result.setData(o);
        return result;
    }

    public static ResultEntity error(ResultEnum resultEnum) {
        return error(resultEnum.getCode(), resultEnum.getMessage());
    }

    public static ResultEntity error(BaseException e) {
        return error(e.getCode(), e.getMessage());
    }
}
