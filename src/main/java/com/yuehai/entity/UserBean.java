package com.yuehai.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhaoyuehai 2019/8/5
 */
@Data
public class UserBean {
    private long id;
    private String userName;
    private String email;
    private String nickName;
    private String phone;
    private String avatar;
}
