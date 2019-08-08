package com.yuehai.entity;

import lombok.Getter;

/**
 * Created by zhaoyuehai 2019/4/15
 */
@Getter
public enum ResultEnum {

    ERROR("00000", "未知错误"),
    SUCCESS("10000", "success"),

    ACCESS_DENIED("20002", "没有权限"),
    USERNAME_EXIST("30001", "该用户名已注册"),
    PHONE_EXIST("30002", "该手机号已注册"),
    EMAIL_EXIST("30003", "该邮箱已注册"),
    USER_LOCKED("40001", "用户被封禁"),
    USER_DISABLED("40002", "用户不可用"),
    PASSWORD_ERROR("40003", "密码错误"),
    USER_NOT_FOUND("40004", "用户不存在"),
    LOGIN_INFO_ERROR("50001", "登录信息异常"),
    LOGIN_INFO_EXPIRED("50002", "登录信息已过期"),

    DELETE_ROOT("60001", "不能删除管理员"),
    DELETE_YOURSELF("60002", "不能删除自己"),
    MODIFY_OTHERS("70001", "不能修改他人信息");


    private String code;
    private String message;

    ResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
